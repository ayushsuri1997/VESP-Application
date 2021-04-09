package com.example.myapplication;

public class getpathDB {
    private static String path;

    public getpathDB(){

    }

    public getpathDB(String path) {
        this.path = path;
    }

    public static String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
