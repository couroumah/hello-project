package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "criteriaPriority")
@XmlAccessorType(XmlAccessType.FIELD)
public class CriteriaPriority {
    @XmlElement(name = "fieldLabel")
    private String fieldLabel;
    @XmlElement(name = "fieldPriority")
    private int fieldPriority;

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public int getFieldPriority() {
        return fieldPriority;
    }

    public void setFieldPriority(int fieldPriority) {
        this.fieldPriority = fieldPriority;
    }
}
