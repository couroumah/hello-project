package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "results")
@XmlAccessorType(XmlAccessType.FIELD)
public class Results {
    @XmlElement(name = "result")
    private Collection<Result> resultCollection;

    public Collection<Result> getResultCollection() {
        return resultCollection;
    }

    public void setResultCollection(Collection<Result> resultCollection) {
        this.resultCollection = resultCollection;
    }
}
