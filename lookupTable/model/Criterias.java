package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "criterias")
@XmlAccessorType(XmlAccessType.FIELD)
public class Criterias {
    @XmlElement(name = "criteria")
    private Collection<Criteria> criteriaCollection;

    public Collection<Criteria> getCriteriaCollection() {
        return criteriaCollection;
    }

    public void setCriteriaCollection(Collection<Criteria> criteriaCollection) {
        this.criteriaCollection = criteriaCollection;
    }
}
