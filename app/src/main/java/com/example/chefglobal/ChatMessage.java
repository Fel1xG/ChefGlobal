package com.example.chefglobal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessage {
    private String userName;
    private String message;
    private String time;

    public ChatMessage() {
        // Constructor vacío requerido para Firebase
    }

    public ChatMessage(String userName, String message) {
        this.userName = userName;
        this.message = message;
        this.time = getCurrentTimeFormatted();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String getCurrentTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}

