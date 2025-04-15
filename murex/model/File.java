package com.westpac.murex.devops.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "file")
@XmlAccessorType(XmlAccessType.FIELD)
public class File {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "attribute")
    private int attribute;
    @XmlElement(name = "userAttribute")
    private int userAttribute;
    @XmlElement(name = "content")
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public int getUserAttribute() {
        return userAttribute;
    }

    public void setUserAttribute(int userAttribute) {
        this.userAttribute = userAttribute;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
