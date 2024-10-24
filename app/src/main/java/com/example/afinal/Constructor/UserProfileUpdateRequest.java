package com.example.afinal.Constructor;

public class UserProfileUpdateRequest {
    private String email;
    private String name;
    private String address;
    private String contactNumber;

    public UserProfileUpdateRequest(String email, String name, String address, String contactNumber) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
    }

}
