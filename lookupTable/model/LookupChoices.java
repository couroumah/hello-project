package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;

@XmlRootElement(name = "lookupChoices")
@XmlAccessorType(XmlAccessType.FIELD)
public class LookupChoices {
    @XmlElement(name = "lookupChoice")
    private Collection<LookupChoice> lookupChoiceCollection;

    public Collection<LookupChoice> getLookupChoiceCollection() {
        return lookupChoiceCollection;
    }

    public void setLookupChoiceCollection(Collection<LookupChoice> lookupChoiceCollection) {
        this.lookupChoiceCollection = lookupChoiceCollection;
    }
}
