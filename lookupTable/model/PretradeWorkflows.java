package com.westpac.murex.devops.lookupTable.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "pretradeWorkflows")
@XmlAccessorType(XmlAccessType.FIELD)
public class PretradeWorkflows {
    @XmlElement(name = "pretradeWorkflow")
    private Collection<PretradeWorkflow> pretradeWorkflowCollection;

    public Collection<PretradeWorkflow> getPretradeWorkflowCollection() {
        return pretradeWorkflowCollection;
    }

    public void setPretradeWorkflowCollection(Collection<PretradeWorkflow> pretradeWorkflowCollection) {
        this.pretradeWorkflowCollection = pretradeWorkflowCollection;
    }
}
