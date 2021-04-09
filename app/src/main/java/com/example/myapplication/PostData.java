package com.example.myapplication;

public class PostData {
    private String by;
    private String data;

    public PostData(){

    }

    public PostData(String by, String data) {
        this.by = by;
        this.data = data;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
