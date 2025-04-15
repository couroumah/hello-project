package com.westpac.murex.devops.lookupTable.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class Templates {
    @XmlElement(name = "assignments")
    private Assignments assignments;
    @XmlElement(name = "pretradeWorkflows")
    private PretradeWorkflows pretradeWorkflows;

    public Assignments getAssignments() {
        return assignments;
    }

    public void setAssignments(Assignments assignments) {
        this.assignments = assignments;
    }

    public PretradeWorkflows getPretradeWorkflows() {
        return pretradeWorkflows;
    }

    public void setPretradeWorkflows(PretradeWorkflows pretradeWorkflows) {
        this.pretradeWorkflows = pretradeWorkflows;
    }
}
