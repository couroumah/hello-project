package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MxML")
@XmlAccessorType(XmlAccessType.FIELD)
public class LookupTable {
    @XmlElement(name = "templateConfiguration")
    private TemplateConfiguration templateConfiguration;

    public TemplateConfiguration getTemplateConfiguration() {
        return templateConfiguration;
    }

    public void setTemplateConfiguration(TemplateConfiguration templateConfiguration) {
        this.templateConfiguration = templateConfiguration;
    }
}
