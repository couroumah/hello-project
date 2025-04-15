package com.westpac.murex.devops.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "documentProperties")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentProperties {
    @XmlElement(name = "producedBy")
    private ProducedBy producedBy;
    @XmlElement(name = "systemDate")
    private String systemDate;
    @XmlElement(name = "accountingDate")
    private String accountingDate;
    @XmlElement(name = "computerDate")
    private String computerDate;
    @XmlElement(name = "computerTime")
    private String computerTime;

    public ProducedBy getProducedBy() {
        return producedBy;
    }

    public void setProducedBy(ProducedBy producedBy) {
        this.producedBy = producedBy;
    }

    public String getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(String systemDate) {
        this.systemDate = systemDate;
    }

    public String getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(String accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getComputerDate() {
        return computerDate;
    }

    public void setComputerDate(String computerDate) {
        this.computerDate = computerDate;
    }

    public String getComputerTime() {
        return computerTime;
    }

    public void setComputerTime(String computerTime) {
        this.computerTime = computerTime;
    }
}
