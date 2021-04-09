package com.example.myapplication;

public class getpathDBStaff {

    private static String path;

    public getpathDBStaff() {
    }

    public getpathDBStaff(String path) {
        this.path = path;
    }

    public static String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
