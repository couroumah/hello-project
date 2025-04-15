package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "decisionTrees")
@XmlAccessorType(XmlAccessType.FIELD)
public class DecisionTrees {
    @XmlElement(name = "decisionTree")
    private Collection<DecisionTree> decisionTreeCollection;

    public Collection<DecisionTree> getDecisionTreeCollection() {
        return decisionTreeCollection;
    }

    public void setDecisionTreeCollection(Collection<DecisionTree> decisionTreeCollection) {
        this.decisionTreeCollection = decisionTreeCollection;
    }
}
