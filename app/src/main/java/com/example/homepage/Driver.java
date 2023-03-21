package com.example.homepage;

public class Driver {
    private String name;
    private String id;
    private String password;
    private String phone;
    private String dob;

    public Driver() {
        name = null;
        id = null;
        password = null;
        phone = null;
        dob = null;
    }
    public Driver(String name, String id, String password, String phone, String dob) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getPassword() {
        return password;
    }
    public String getId() {
        return id;
    }
    public String getDob() { return this.dob; }

}