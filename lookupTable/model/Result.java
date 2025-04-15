package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result {
    @XmlElement(name = "userDefinedField")
    private UserDefinedField userDefinedField;

    public UserDefinedField getUserDefinedField() {
        return userDefinedField;
    }

    public void setUserDefinedField(UserDefinedField userDefinedField) {
        this.userDefinedField = userDefinedField;
    }
}
