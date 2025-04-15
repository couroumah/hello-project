package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "assignments")
@XmlAccessorType(XmlAccessType.FIELD)
public class Assignments {
    @XmlElement(name = "assignment")
    private Collection<Assignment> assignmentCollection;

    public Collection<Assignment> getAssignmentCollection() {
        return assignmentCollection;
    }

    public void setAssignmentCollection(Collection<Assignment> assignmentCollection) {
        this.assignmentCollection = assignmentCollection;
    }
}
