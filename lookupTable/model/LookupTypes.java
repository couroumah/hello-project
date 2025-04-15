package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "lookupTypes")
@XmlAccessorType(XmlAccessType.FIELD)
public class LookupTypes {
    @XmlElement(name = "lookupType")
    private Collection<LookupType> lookupTypeCollection;

    public Collection<LookupType> getLookupTypeCollection() {
        return lookupTypeCollection;
    }

    public void setLookupTypeCollection(Collection<LookupType> lookupTypeCollection) {
        this.lookupTypeCollection = lookupTypeCollection;
    }
}
