package com.example.bloodbank3.models;

public class AppointmentData {

    private String date,time,userId,userName,appointmentStatus,appointmentId;

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setAppointmentStatus(String appointmentStatus) { this.appointmentStatus = appointmentStatus; }

    public AppointmentData(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public AppointmentData(String date, String time, String userId) {
        this.date = date;
        this.time = time;
        this.userId = userId;
    }

    public AppointmentData(String date, String time, String userId, String appointmentStatus) {
        this.date = date;
        this.time = time;
        this.userId = userId;
        this.appointmentStatus = appointmentStatus;
    }

    public AppointmentData(String date, String time, String userId, String appointmentStatus, String appointmentId) {
        this.date = date;
        this.time = time;
        this.userId = userId;
        this.appointmentStatus = appointmentStatus;
        this.appointmentId = appointmentId;
    }

    public AppointmentData(String date, String time, String userId, String userName, String appointmentStatus, String appointmentId) {
        this.date = date;
        this.time = time;
        this.userId = userId;
        this.userName = userName;
        this.appointmentStatus = appointmentStatus;
        this.appointmentId = appointmentId;
    }

    public AppointmentData() {
    }
}
