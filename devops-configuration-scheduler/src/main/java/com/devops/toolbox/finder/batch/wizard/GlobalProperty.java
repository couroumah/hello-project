package com.devops.toolbox.finder.batch.wizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalProperty {
    @JsonProperty("name")
    private String name;
    private String pathOut;
    private String fileExtensionXML;
    private String fileExtensionCSV;

    @Override
    public String toString() {
        return "GlobalProperty{" +
                "name='" + name + '\'' +
                ", pathOut='" + pathOut + '\'' +
                ", fileExtensionXML='" + fileExtensionXML + '\'' +
                ", fileExtensionCSV='" + fileExtensionCSV + '\'' +
                '}';
    }
}
