package com.westpac.murex.devops.lookupTable.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rule")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rule {
    @XmlElement(name = "criterias")
    private Criterias criterias;
    @XmlElement(name = "results")
    private Results results;

    public Criterias getCriterias() {
        return criterias;
    }

    public void setCriterias(Criterias criterias) {
        this.criterias = criterias;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }
}
