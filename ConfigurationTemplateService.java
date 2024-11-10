package com.devops.toolbox.cmftemplates;

import com.devops.toolbox.config.Settings;
import com.devops.toolbox.util.CmfTemplatesHelper;
import com.devops.toolbox.util.JAXBUtils;
import com.devops.toolbox.util.Messages;
import com.westpac.murex.devops.model.cmftemplates.ConfigurationTemplate;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.devops.toolbox.util.Helper.toUnzipFromFile;
import static com.devops.toolbox.util.StandardConstants.XLSX_FILE_EXTENSION;

@Service
@RequiredArgsConstructor
public class ConfigurationTemplateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationTemplateService.class);

    private final Settings settings;
    private final ConfigurationTemplateMapper mapper;

    private void writeReport(String businessObject,
                             ConfigurationTemplate configurationTemplate,
                             Path pathOutputFile,
                             Map<String, String> mapObjectId) {
        LOGGER.info("--start [{}] BusinessObject[{}]...", Thread.currentThread().getStackTrace()[1].getMethodName(), businessObject);

        List<String> headers = Arrays.asList(Messages.getString("CmfTemplatesHelper.excel.sheet.configurationTemplate.HEADER_TITLE").split(","));
        List<ConfigurationTemplateDto> dtos = mapper.toDtos(configurationTemplate, mapObjectId);
        if (dtos.isEmpty()) {
            return;
        }
        CmfTemplatesHelper.writeConfigurationTemplateToExcel(dtos, headers, pathOutputFile);

        LOGGER.info("--end [{}] BusinessObject [{}]", Thread.currentThread().getStackTrace()[1].getMethodName(), businessObject);
    }

    private ConfigurationTemplate getConfigurationTemplate(Path pathZip, Path pathUnZip) throws IOException, JAXBException {
        LOGGER.debug("--start [{}] getConfigurationTemplate [{}]...", Thread.currentThread().getStackTrace()[1].getMethodName(), pathZip);

        String inputZipFilename = pathZip.getFileName().toString();
        try {
            toUnzipFromFile(pathZip, pathUnZip);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot unzip file [%s]. IOException: %s", inputZipFilename, e.getMessage()));
        }

        Path pathCmfTemplate = CmfTemplatesHelper.getCmfTemplatePath(inputZipFilename, pathUnZip);
        String configurationTemplateAsXML = FileUtils.readFileToString(pathCmfTemplate.toFile(), String.valueOf(Charset.defaultCharset()));
        // Clean up
//        FileSystemUtils.deleteRecursively(pathUnZip);

        LOGGER.debug("--end [{}] getConfigurationTemplate [{}]", Thread.currentThread().getStackTrace()[1].getMethodName(), pathZip);

        return JAXBUtils.convertXMLToObject(ConfigurationTemplate.class, configurationTemplateAsXML);
    }

    public void createConfigurationTemplateReport(@NonNull String input) throws JAXBException, IOException {
        String businessObject = StringUtils.substringBefore(input, ".");
        Path pathZip = settings.getPathInputDirectory().resolve(input);

        if (!pathZip.toFile().exists()) {
            throw new RuntimeException(String.format("FileNotFound [%s].", pathZip));
        }
        // Unpackage CTT
        Path pathUnzip = settings.getPathOutputDirectory().resolve(settings.getUnzipOutputDirectory());
        ConfigurationTemplate configurationTemplate = getConfigurationTemplate(pathZip, pathUnzip);

        writeReport(
                businessObject,
                configurationTemplate,
                settings.getPathOutputDirectory()
                        .resolve(settings.getExportFilename(businessObject, XLSX_FILE_EXTENSION)),
                settings.getMapObjectId());
    }

}
