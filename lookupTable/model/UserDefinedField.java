package com.westpac.murex.devops.lookupTable.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userDefinedField")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDefinedField {
    @XmlElement(name = "fieldLabel")
    private String fieldLabel;
    @XmlElement(name = "fieldValue")
    private String fieldValue;
    @XmlElement(name = "fieldType")
    private String fieldType;

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
