package com.devops.toolbox.finder.batch.wizard;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "propertyGroups")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public class PropertyGroups {
    private List<PropertyGroup> propertyGroups;
}
