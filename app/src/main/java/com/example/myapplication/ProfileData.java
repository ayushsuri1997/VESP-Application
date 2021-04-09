package com.example.myapplication;

public class ProfileData {
    String authID;
    String uniqueID;
    String full_name;
    String emailID;
    String password;
    String phone;

    public ProfileData(){

    }

    public ProfileData(String authID, String uniqueID, String full_name, String emailID, String password, String phone) {
        this.authID = authID;
        this.uniqueID = uniqueID;
        this.full_name = full_name;
        this.emailID = emailID;
        this.password = password;
        this.phone = phone;
    }

    public String getAuthID() {
        return authID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
