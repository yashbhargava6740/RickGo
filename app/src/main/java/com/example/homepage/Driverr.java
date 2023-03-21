package com.example.homepage;

public class Driverr {
    private String uid,pass;

    public Driverr() {
        uid = null;
        pass = null;
    }
    public Driverr(String uid, String pass) {
        this.uid = uid;
        this.pass = pass;
    }

    public String getUid() {
        return this.uid;
    }
    public String getPass() {
        return this.pass;
    }

}
