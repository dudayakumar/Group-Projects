package com.example.bloodbank3.models;

public class AppointmentData {

    private String date,time,userId,appointmentStatus;

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

    public AppointmentData() {
    }
}
