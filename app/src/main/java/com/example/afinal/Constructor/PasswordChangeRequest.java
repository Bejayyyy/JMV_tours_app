package com.example.afinal.Constructor;

public class PasswordChangeRequest {
    private String user_email; // Changed from user_id to user_email
    private String old_password;
    private String new_password;

    // Constructor
    public PasswordChangeRequest(String user_email, String old_password, String new_password) {
        this.user_email = user_email; // Initialize user_email
        this.old_password = old_password;
        this.new_password = new_password;
    }

    // Getters
    public String getUser_email() {
        return user_email;
    }

    public String getOld_password() {
        return old_password;
    }

    public String getNew_password() {
        return new_password;
    }
}
