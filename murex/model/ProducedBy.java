package com.westpac.murex.devops.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "criteria")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProducedBy {
    @XmlElement(name = "partyName")
    private String partyName;
    @XmlElement(name = "user")
    private User user;
}
