package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lookupType")
@XmlAccessorType(XmlAccessType.FIELD)
public class LookupType {
    @XmlElement(name = "lookupTypeLabel")
    private String lookupTypeLabel;
    @XmlElement(name = "lookupTypeDescription")
    private String lookupTypeDescription;
    @XmlElement(name = "lookupTypeSearchMode")
    private String lookupTypeSearchMode;
    @XmlElement(name = "lookupTypeDisplayMode")
    private String lookupTypeDisplayMode;
    @XmlElement(name = "lookupTypeAuditable")
    private String lookupTypeAuditable;
    @XmlElement(name = "lookupTypeColumns")
    private LookupTypeColumns lookupTypeColumns;

    public String getLookupTypeLabel() {
        return lookupTypeLabel;
    }

    public void setLookupTypeLabel(String lookupTypeLabel) {
        this.lookupTypeLabel = lookupTypeLabel;
    }

    public String getLookupTypeDescription() {
        return lookupTypeDescription;
    }

    public void setLookupTypeDescription(String lookupTypeDescription) {
        this.lookupTypeDescription = lookupTypeDescription;
    }

    public String getLookupTypeSearchMode() {
        return lookupTypeSearchMode;
    }

    public void setLookupTypeSearchMode(String lookupTypeSearchMode) {
        this.lookupTypeSearchMode = lookupTypeSearchMode;
    }

    public String getLookupTypeDisplayMode() {
        return lookupTypeDisplayMode;
    }

    public void setLookupTypeDisplayMode(String lookupTypeDisplayMode) {
        this.lookupTypeDisplayMode = lookupTypeDisplayMode;
    }

    public String getLookupTypeAuditable() {
        return lookupTypeAuditable;
    }

    public void setLookupTypeAuditable(String lookupTypeAuditable) {
        this.lookupTypeAuditable = lookupTypeAuditable;
    }

    public LookupTypeColumns getLookupTypeColumns() {
        return lookupTypeColumns;
    }

    public void setLookupTypeColumns(LookupTypeColumns lookupTypeColumns) {
        this.lookupTypeColumns = lookupTypeColumns;
    }
}
