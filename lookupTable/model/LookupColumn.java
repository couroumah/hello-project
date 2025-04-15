package com.westpac.murex.devops.lookupTable.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lookupColumn")
@XmlAccessorType(XmlAccessType.FIELD)
public class LookupColumn {
    @XmlElement(name = "lookupColumnType")
    private String lookupColumnType;
    @XmlElement(name = "lookupColumnLabel")
    private String lookupColumnLabel;
    @XmlElement(name = "lookupColumnTitle")
    private String lookupColumnTitle;
    @XmlElement(name = "lookupColumnLength")
    private int lookupColumnLength;
    @XmlElement(name = "lookupColumnDefaultRank")
    private int lookupColumnDefaultRank;
    @XmlElement(name = "lookupColumnTitleSpan")
    private int lookupColumnTitleSpan;
    @XmlElement(name = "lookupColumnDisplayWidth")
    private int lookupColumnDisplayWidth;
    @XmlElement(name = "lookupColumnZone")
    private String lookupColumnZone;
    @XmlElement(name = "lookupColumnUpperCase")
    private boolean lookupColumnUpperCase;
    @XmlElement(name = "lookupColumnHidden")
    private boolean lookupColumnHidden;

    public String getLookupColumnType() {
        return lookupColumnType;
    }

    public void setLookupColumnType(String lookupColumnType) {
        this.lookupColumnType = lookupColumnType;
    }

    public String getLookupColumnLabel() {
        return lookupColumnLabel;
    }

    public void setLookupColumnLabel(String lookupColumnLabel) {
        this.lookupColumnLabel = lookupColumnLabel;
    }

    public String getLookupColumnTitle() {
        return lookupColumnTitle;
    }

    public void setLookupColumnTitle(String lookupColumnTitle) {
        this.lookupColumnTitle = lookupColumnTitle;
    }

    public int getLookupColumnLength() {
        return lookupColumnLength;
    }

    public void setLookupColumnLength(int lookupColumnLength) {
        this.lookupColumnLength = lookupColumnLength;
    }

    public int getLookupColumnDefaultRank() {
        return lookupColumnDefaultRank;
    }

    public void setLookupColumnDefaultRank(int lookupColumnDefaultRank) {
        this.lookupColumnDefaultRank = lookupColumnDefaultRank;
    }

    public int getLookupColumnTitleSpan() {
        return lookupColumnTitleSpan;
    }

    public void setLookupColumnTitleSpan(int lookupColumnTitleSpan) {
        this.lookupColumnTitleSpan = lookupColumnTitleSpan;
    }

    public int getLookupColumnDisplayWidth() {
        return lookupColumnDisplayWidth;
    }

    public void setLookupColumnDisplayWidth(int lookupColumnDisplayWidth) {
        this.lookupColumnDisplayWidth = lookupColumnDisplayWidth;
    }

    public String getLookupColumnZone() {
        return lookupColumnZone;
    }

    public void setLookupColumnZone(String lookupColumnZone) {
        this.lookupColumnZone = lookupColumnZone;
    }

    public boolean isLookupColumnUpperCase() {
        return lookupColumnUpperCase;
    }

    public void setLookupColumnUpperCase(boolean lookupColumnUpperCase) {
        this.lookupColumnUpperCase = lookupColumnUpperCase;
    }

    public boolean isLookupColumnHidden() {
        return lookupColumnHidden;
    }

    public void setLookupColumnHidden(boolean lookupColumnHidden) {
        this.lookupColumnHidden = lookupColumnHidden;
    }
}
