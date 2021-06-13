package com.se.onlinequizsystem;

public class User {
    private int id;
    private String username;
    private String password;
    private int uType;

    public User() {
        id = -1;
        username = null;
        password = null;
        uType = -1;
    }

    public User(int id, String username, String password, int uType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.uType = uType;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUType() {
        return this.uType;
    }

    public void setUType(int uType) {
        this.uType = uType;
    }

}
