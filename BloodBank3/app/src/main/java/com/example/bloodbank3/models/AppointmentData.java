package com.example.bloodbank3.models;

public class AppointmentData {

    private String date,time;

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

    public AppointmentData(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public AppointmentData() {
    }
}
