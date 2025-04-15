package com.westpac.murex.devops.model;


import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "businessObjectId")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessObjectId {

    @XmlAttribute(name = "mefClassInstanceType")
    private String mefClassInstanceType;
    @XmlAttribute(name = "mefClass")
    private String mefClass;
    @XmlElement(name = "identifier")
    private String identifier;
    @XmlElement(name = "versionIdentifier")
    private VersionIdentifier versionIdentifier;
    @XmlElement(name = "primarySystem")
    private String primarySystem;
    @XmlElement(name = "displayLabel")
    private String displayLabel;

    public String getMefClassInstanceType() {
        return mefClassInstanceType;
    }

    public void setMefClassInstanceType(String mefClassInstanceType) {
        this.mefClassInstanceType = mefClassInstanceType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public VersionIdentifier getVersionIdentifier() {
        return versionIdentifier;
    }

    public void setVersionIdentifier(VersionIdentifier versionIdentifier) {
        this.versionIdentifier = versionIdentifier;
    }

    public String getPrimarySystem() {
        return primarySystem;
    }

    public void setPrimarySystem(String primarySystem) {
        this.primarySystem = primarySystem;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }
}
