package com.example.afinal.Constructor;

public class HistoryBookingApiResponse { // Ensure this matches the name used in the History class
    private String packageName; // Adjusted field name to match constructor
    private String departureDate;
    private String bookedBy;
    private String userEmail;
    private String userContactNumber;
    private String userAddress;
    private String carName;

    // Constructor
    public HistoryBookingApiResponse(String packageName, String departureDate, String bookedBy, String userEmail, String userContactNumber, String userAddress, String carName) {
        this.packageName = packageName; // Ensure field names match constructor parameters
        this.departureDate = departureDate;
        this.bookedBy = bookedBy;
        this.userEmail = userEmail;
        this.userContactNumber = userContactNumber;
        this.userAddress = userAddress;
        this.carName = carName;
    }

    // Getters for all fields
    public String getPackageName() {
        return packageName;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public String getEmail() {
        return userEmail;
    }

    public String getContactNumber() {
        return userContactNumber;
    }

    public String getAddress() {
        return userAddress;
    }

    public String getCar() {
        return carName;
    }
}
