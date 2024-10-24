package com.example.afinal.Constructor;

public class User {
    private  String username;
    private String password;
    private String email;

    public  User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public  User(String email, String password){
        this.email = email;
        this.password = password;
    }
}
