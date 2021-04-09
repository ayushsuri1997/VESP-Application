package com.example.myapplication;

public class getPathDBHOD {
    private static String path;

    public getPathDBHOD() {
    }

    public getPathDBHOD(String path) {
        this.path = path;
    }

    public static String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
