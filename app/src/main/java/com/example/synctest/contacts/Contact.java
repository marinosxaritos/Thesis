package com.example.synctest.contacts;

public class Contact {

    private String License;
    private String Name;
    private String LastName;
    private String Date;
    private String Time;
    private String Address;

    private int Sync_status;


    public Contact(String License, String Name, String LastName, String Date, String Time, String Address, int Sync_status){
        this.setLicense(License);
        this.setName(Name);
        this.setLastName(LastName);
        this.setDate(Date);
        this.setTime(Time);
        this.setAddress(Address);
        this.setSync_status(Sync_status);
    }


    public String getLicense() {
        return License;
    }

    public void setLicense(String license) {
        License = license;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getSync_status() {
        return Sync_status;
    }

    public void setSync_status(int sync_status) {
        Sync_status = sync_status;
    }
}
