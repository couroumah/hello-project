package com.westpac.murex.devops.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Visibility")
@XmlAccessorType(XmlAccessType.FIELD)
public class Visibility {
    @XmlElement(name = "owner")
    private String owner;
    @XmlElement(name = "rights")
    private Rights rights;
    @XmlElement(name = "contextLists")
    private String contextLists;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Rights getRights() {
        return rights;
    }

    public void setRights(Rights rights) {
        this.rights = rights;
    }

    public String getContextLists() {
        return contextLists;
    }

    public void setContextLists(String contextLists) {
        this.contextLists = contextLists;
    }
}
