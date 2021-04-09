package com.example.myapplication;

public class Student_Details {
    String authID;
    String uniqueID;
    String name;
    String mobile;
    String emergency_mob;
    String address;
    String email;
    String password;
    String roll_no;
    String batch;
    String who;
    public Student_Details(){

    }

    public Student_Details(String authID, String uniqueID, String name, String mobile, String emergency_mob, String address, String email, String password, String roll_no, String batch, String who) {
        this.authID = authID;
        this.uniqueID = uniqueID;
        this.name = name;
        this.mobile = mobile;
        this.emergency_mob = emergency_mob;
        this.address = address;
        this.email = email;
        this.password = password;
        this.roll_no = roll_no;
        this.batch = batch;
        this.who = who;
    }

    public String getAuthID() {
        return authID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmergency_mob() {
        return emergency_mob;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public String getBatch() {
        return batch;
    }

    public String getWho() {
        return who;
    }
}
