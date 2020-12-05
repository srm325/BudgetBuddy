package com.srm325.budgetshop.model_classes;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ShoppingSessions {

    private String sessionName;

    @ServerTimestamp
    private Date sessionDate;

    public ShoppingSessions() {
    }

    public ShoppingSessions(String sessionName, Date sessionDate) {
        this.sessionName = sessionName;
        this.sessionDate = sessionDate;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

}
