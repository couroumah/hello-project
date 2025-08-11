import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "globalProperty")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public class GlobalProperty {
    @XmlAttribute
    @XmlID
    private String name;

    private String pathOut;
    private String fileExtensionXML;
    private String fileExtensionCSV;
}
