package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "lookupColumns")
@XmlAccessorType(XmlAccessType.FIELD)
public class LookupColumns {
    @XmlElement(name = "lookupColumn")
    private Collection<LookupColumn> lookupColumnCollection;

    public Collection<LookupColumn> getLookupColumnCollection() {
        return lookupColumnCollection;
    }

    public void setLookupColumnCollection(Collection<LookupColumn> lookupColumnCollection) {
        this.lookupColumnCollection = lookupColumnCollection;
    }
}
