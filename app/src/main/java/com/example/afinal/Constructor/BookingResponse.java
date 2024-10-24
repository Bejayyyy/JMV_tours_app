package com.example.afinal.Constructor;

public class BookingResponse {
    private String package_name;
    private String departure_date;
    private String book_by;
    private String user_email;
    private String user_contactNumber;
    private String user_address;
    private String alt_contactNumber;  // Added alternate contact number
    private String alt_address;        // Added alternate address
    private String car_name;
    private int booking_id;

    // Updated constructor to include alternate fields
    public BookingResponse(int booking_id, String package_name, String departure_date, String book_by,
                           String user_email, String user_contactNumber, String user_address,
                           String alt_contactNumber, String alt_address, String car_name) {
        this.booking_id = booking_id;
        this.package_name = package_name;
        this.departure_date = departure_date;
        this.book_by = book_by;
        this.user_email = user_email;
        this.user_contactNumber = user_contactNumber;
        this.user_address = user_address;
        this.alt_contactNumber = alt_contactNumber;
        this.alt_address = alt_address;
        this.car_name = car_name;
    }

    // Getters for all fields including alternate fields
    public int getBookingId() {
        return booking_id;
    }

    public String getPackageName() {
        return package_name;
    }

    public String getDepartureDate() {
        return departure_date;
    }

    public String getBookedBy() {
        return book_by;
    }

    public String getEmail() {
        return user_email;
    }

    public String getContactNumber() {
        return user_contactNumber;
    }

    public String getAltContactNumber() {
        return alt_contactNumber;
    }

    public String getAddress() {
        return user_address;
    }

    public String getAltAddress() {
        return alt_address;
    }

    public String getCar() {
        return car_name;
    }
}
