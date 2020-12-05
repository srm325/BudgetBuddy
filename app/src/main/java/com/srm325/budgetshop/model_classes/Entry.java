package com.srm325.budgetshop.model_classes;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Entry {
    private String entryDescription;
    private Double entryAmount;
    @ServerTimestamp private Date entryDate;

    public Entry(){}

    public Entry(String entryDescription, Double entryAmount, Date entryDate) {
        this.entryDescription = entryDescription;
        this.entryAmount = entryAmount;
        this.entryDate = entryDate;
    }

    public String getEntryDescription() {
        return entryDescription;
    }

    public void setEntryDescription(String entryDescription) {
        this.entryDescription = entryDescription;
    }

    public Double getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(Double entryAmount) {
        this.entryAmount = entryAmount;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

}
