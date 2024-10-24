package com.example.afinal.Constructor;

import java.util.ArrayList;
import java.util.List;

public class PendingResponse {
    private List<BookingResponse> pendingBookings;

    // Default constructor
    public PendingResponse() {
    }

    public PendingResponse(List<BookingResponse> pendingBookings) {
        this.pendingBookings = pendingBookings;
    }

    public List<BookingResponse> getPendingBookings() {
        return pendingBookings != null ? pendingBookings : new ArrayList<>(); // Return an empty list if null
    }
}
