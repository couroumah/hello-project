package com.devops.toolbox.config;

import com.devops.toolbox.util.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "application.settings")
@Getter
@Setter
public class Settings {

    private String fileExtensionXML;
    private String fileExtensionCSV;
    private Path pathInputDirectory;
    private Path pathOutputDirectory;
    private String repositoryDirectory;
    private String unzipOutputDirectory;
    private Map<String, String> mapObjectId;

    public String getExportFilename(String businessObject, String fileExtension){
        return businessObject + "_" +
                DateUtils.getCurrentDateTime().format(DateTimeFormatter.ofPattern(DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS)) + "." +
                fileExtension;
    }
}
