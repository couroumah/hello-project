package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lookupConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class LookupConfiguration {
    @XmlElement(name = "lookupTypes")
    private LookupTypes lookupTypes;
    @XmlElement(name = "lookupColumns")
    private LookupColumns lookupColumns;
    @XmlElement(name = "lookupChoices")
    private LookupChoices lookupChoices;
    @XmlElement(name = "decisionTrees")
    private DecisionTrees decisionTrees;

    public LookupTypes getLookupTypes() {
        return lookupTypes;
    }

    public void setLookupTypes(LookupTypes lookupTypes) {
        this.lookupTypes = lookupTypes;
    }

    public LookupColumns getLookupColumns() {
        return lookupColumns;
    }

    public void setLookupColumns(LookupColumns lookupColumns) {
        this.lookupColumns = lookupColumns;
    }

    public LookupChoices getLookupChoices() {
        return lookupChoices;
    }

    public void setLookupChoices(LookupChoices lookupChoices) {
        this.lookupChoices = lookupChoices;
    }

    public DecisionTrees getDecisionTrees() {
        return decisionTrees;
    }

    public void setDecisionTrees(DecisionTrees decisionTrees) {
        this.decisionTrees = decisionTrees;
    }
}
