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
@XmlRootElement(name = "versionIdentifier")
@XmlAccessorType(XmlAccessType.FIELD)
public class VersionIdentifier {
    @XmlElement(name = "versionLabel")
    private String versionLabel;
    @XmlElement(name = "versionRevision")
    private VersionRevision versionRevision;
}
