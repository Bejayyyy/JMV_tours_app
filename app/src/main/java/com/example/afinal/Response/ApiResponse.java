package com.example.afinal.Response;

import com.example.afinal.Constructor.HistoryBookingApiResponse;
import java.util.List;

public class ApiResponse {
    private String status;
    private String message;
    private List<HistoryBookingResponse> data;

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<HistoryBookingResponse> getData() {
        return data;
    }

    public void setData(List<HistoryBookingResponse> data) {
        this.data = data;
    }
}
