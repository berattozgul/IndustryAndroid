package com.kut.industryandroid;

public class User {
    private String id;
    private String username;
    private String userType;

    public User() {
    }

    public User(String id, String name, String userType) {
        this.id = id;
        this.username = name;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
