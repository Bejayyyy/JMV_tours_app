package com.example.afinal.Response;

public class UserProfileResponse {
    private boolean success;
    private String userProfileName;
    private String userProfileEmail;
    private String userProfileAddress;
    private String userProfileContactNumber;

    //Getters and Setters


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserProfileName() {
        return userProfileName;
    }

    public void setUserProfileName(String userProfileName) {
        this.userProfileName = userProfileName;
    }

    public String getUserProfileEmail() {
        return userProfileEmail;
    }

    public void setUserProfileEmail(String userProfileEmail) {
        this.userProfileEmail = userProfileEmail;
    }

    public String getUserProfileAddress() {
        return userProfileAddress;
    }

    public void setUserProfileAddress(String userProfileAddress) {
        this.userProfileAddress = userProfileAddress;
    }

    public String getUserProfileContactNumber() {
        return userProfileContactNumber;
    }

    public void setUserProfileContactNumber(String userProfileContactNumber) {
        this.userProfileContactNumber = userProfileContactNumber;
    }
}
