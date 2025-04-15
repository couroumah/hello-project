package com.westpac.murex.devops.lookupTable.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "templateConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateConfiguration {

    @XmlElement(name = "templates")
    private Templates templates;
    @XmlElement(name = "lookupConfiguration")
    private LookupConfiguration lookupConfiguration;

    public Templates getTemplates() {
        return templates;
    }

    public void setTemplates(Templates templates) {
        this.templates = templates;
    }

    public LookupConfiguration getLookupConfiguration() {
        return lookupConfiguration;
    }

    public void setLookupConfiguration(LookupConfiguration lookupConfiguration) {
        this.lookupConfiguration = lookupConfiguration;
    }
}
