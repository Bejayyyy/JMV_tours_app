package com.example.afinal.Constructor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.R;


import java.util.List;

public class HistoryBookingAdapter extends RecyclerView.Adapter<HistoryBookingAdapter.HistoryBookingViewHolder> {
    private List<HistoryBookingApiResponse> bookingList;
    private Context context;

    public HistoryBookingAdapter(List<HistoryBookingApiResponse> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_history, parent, false);
        return new HistoryBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryBookingViewHolder holder, int position) {
        // Get the current booking response
        HistoryBookingApiResponse booking = bookingList.get(position);

        // Set the package name, departure date, and other booking details
        holder.packageNameTextView.setText(booking.getPackageName());
        holder.departureDateTextView.setText(booking.getDepartureDate()); // Ensure this is a formatted date string
        holder.bookedBy.setText(booking.getBookedBy());
        holder.bookEmail.setText(booking.getEmail());
        holder.bookContactNumber.setText(booking.getContactNumber());
        holder.bookAddress.setText(booking.getAddress());
        holder.carTextView.setText(booking.getCar());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    // ViewHolder class to hold the view elements
    static class HistoryBookingViewHolder extends RecyclerView.ViewHolder {
        private TextView packageNameTextView;
        private TextView departureDateTextView;
        private TextView carTextView;
        private TextView historypriceTextView;
        private TextView bookedBy;
        private TextView bookEmail;
        private TextView bookContactNumber;
        private TextView bookAddress;

        public HistoryBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize TextViews using the correct IDs from the layout
            packageNameTextView = itemView.findViewById(R.id.packageNameTextView);
            departureDateTextView = itemView.findViewById(R.id.departureDateTextView);
            carTextView = itemView.findViewById(R.id.carTextView);
            bookedBy = itemView.findViewById(R.id.bookerName); // Assuming you have the correct ID
            bookEmail = itemView.findViewById(R.id.bookEmail); // Assuming you have the correct ID
            bookContactNumber = itemView.findViewById(R.id.bookContactNumber); // Assuming you have the correct ID
            bookAddress = itemView.findViewById(R.id.bookAddress); // Assuming you have the correct ID

        }
    }
}
