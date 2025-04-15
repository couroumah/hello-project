package com.westpac.murex.devops.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;

@Slf4j
public class JAXBUtils {
    private JAXBUtils(){

    }

    public static <T> void convertObjectToXML(T object, Path pathOutput) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    }

    public static <T> T convertXMLToObject(Class clazz, String xmlAsString) throws JAXBException {
        StringReader reader = new StringReader(xmlAsString);
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(reader);
    }

    public static String prettyPrint(String xmlAsString, Boolean omitXmlDeclaration) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(xmlAsString)));
        OutputFormat outputFormat = new OutputFormat(document);
        outputFormat.setIndenting(true);
        outputFormat.setIndent(2);
        outputFormat.setOmitXMLDeclaration(omitXmlDeclaration);
        outputFormat.setLineWidth(Integer.MAX_VALUE);
        Writer writer = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(writer, outputFormat);
        serializer.serialize(document);

        return outputFormat.toString();
    }
}
