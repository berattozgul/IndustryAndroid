package com.kut.industryandroid.Models;

import java.util.Date;

public class Message {
    private String messageID;
    private String fromWho;
    private String toWho;
    private String messageText;
    private String date;

    public Message() {
    }

    public String getMessageID() {
        return messageID;
    }

    public Message(String messageID, String fromWho, String toWho, String messageText, String date) {
        this.messageID = messageID;
        this.fromWho = fromWho;
        this.toWho = toWho;
        this.messageText = messageText;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getFromWho() {
        return fromWho;
    }

    public void setFromWho(String fromWho) {
        this.fromWho = fromWho;
    }

    public String getToWho() {
        return toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
