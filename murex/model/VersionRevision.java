package com.westpac.murex.devops.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@XmlRootElement(name = "versionRevision")
@XmlAccessorType(XmlAccessType.FIELD)
public class VersionRevision {
    @XmlElement(name = "versionNumber")
    private int versionNumber;
    @XmlElement(name = "versionDateAndTime")
    private VersionDateAndTime versionDateAndTime;
}
