package com.devops.toolbox.finder;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class FinderSettings {
    private String pathOutputDirectory;
    private String fileFilterWildCard;
    private boolean showBreakdown;
    private Map<String,String> mapLookupValues;
    private Map<String,String> mapLookupDirectories;

}
