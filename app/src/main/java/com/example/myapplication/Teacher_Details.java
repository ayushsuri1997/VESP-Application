package com.example.myapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Teacher_Details {

    private String authID,uID,name,email,password,address,contact,emergencyContact,teacher_id,who;
    static String decider="Wrong";

    ArrayList<Teacher_Details> arrayList = new ArrayList<>();
    public Teacher_Details()
    {

    }
    public Teacher_Details(String authID,String uID,String name, String email, String password, String address, String contact, String emergencyContact,String teacher_id, String who)
    {
        this.authID = authID;
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.contact = contact;
        this.emergencyContact = emergencyContact;
        this.teacher_id=teacher_id;
        this.who = who;
    }

    Comparator<Teacher_Details> comparator = new Comparator<Teacher_Details>() {
        @Override
        public int compare(Teacher_Details t1, Teacher_Details t2) {

            if(t1.getName().compareTo(t2.getName()) < 0)
            {
                return -1;
            }
            else if(t1.getName().compareTo(t2.getName()) > 0)
            {
                return 1;
            }
            else
                return 0;
        }
    };

    public void compareNames(ArrayList<Teacher_Details> arrayList1)
    {
        Collections.sort(arrayList1,comparator);
    }

    public ArrayList<String> getListName()
    {
        ArrayList temp = new ArrayList();
        for(int i = 0 ; i < arrayList.size() ; i++ )
        {
            temp.add(this.arrayList.get(i).getName());
        }
        return temp;


    }
    public void addObject(Teacher_Details teacher_details)
    {
        arrayList.add(teacher_details);
    }
    public ArrayList<Teacher_Details> returnArrayList()
    {
        return arrayList;
    }
    public void clearList()
    {
        arrayList.clear();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getAuthID() {
        return authID;
    }

    public void setAuthID(String authID) {
        this.authID = authID;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
