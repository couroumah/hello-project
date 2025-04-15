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
@XmlRootElement(name = "versionDateAndTime")
@XmlAccessorType(XmlAccessType.FIELD)
public class VersionDateAndTime {
    @XmlElement(name = "date")
    private String date;
}
