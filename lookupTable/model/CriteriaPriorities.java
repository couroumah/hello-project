package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "criteriaPriorities")
@XmlAccessorType(XmlAccessType.FIELD)
public class CriteriaPriorities {
    @XmlElement(name = "criteriaPriority")
    private Collection<CriteriaPriority> criteriaPriorityCollection;

    public Collection<CriteriaPriority> getCriteriaPriorityCollection() {
        return criteriaPriorityCollection;
    }

    public void setCriteriaPriorityCollection(Collection<CriteriaPriority> criteriaPriorityCollection) {
        this.criteriaPriorityCollection = criteriaPriorityCollection;
    }
}
