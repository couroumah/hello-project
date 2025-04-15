package com.westpac.murex.devops.lookupTable.model;


import com.westpac.murex.devops.model.BusinessObjectId;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "decisionTree")
@XmlAccessorType(XmlAccessType.FIELD)
public class DecisionTree {
    @XmlElement(name = "businessObjectId")
    private BusinessObjectId businessObjectId;
    @XmlElement(name = "label")
    private String label;
    @XmlElement(name = "criteriaPriorities")
    private CriteriaPriorities criteriaPriorities;
    @XmlElement(name = "rules")
    private Rules rules;

    public BusinessObjectId getBusinessObjectId() {
        return businessObjectId;
    }

    public void setBusinessObjectId(BusinessObjectId businessObjectId) {
        this.businessObjectId = businessObjectId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public CriteriaPriorities getCriteriaPriorities() {
        return criteriaPriorities;
    }

    public void setCriteriaPriorities(CriteriaPriorities criteriaPriorities) {
        this.criteriaPriorities = criteriaPriorities;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }
}
