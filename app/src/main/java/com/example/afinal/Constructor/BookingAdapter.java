package com.example.afinal.Constructor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.Booking.Booking;
import com.example.afinal.EditBookings;
import com.example.afinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<BookingResponse> bookingList;
    private Context context;
    private boolean isCanceled;
    private String userEmail;


    // Constructor updated to accept 'isCanceled' flag
    public BookingAdapter(List<BookingResponse> bookingList, Context context, boolean isCanceled, String userEmail) {
        this.bookingList = bookingList;
        this.context = context;
        this.isCanceled = isCanceled;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        // Get the current booking response
        BookingResponse booking = bookingList.get(position);

        // Set the package name, departure date, and other booking details
        holder.packageNameTextView.setText(booking.getPackageName());
        holder.departureDateTextView.setText(booking.getDepartureDate()); // Ensure this is a formatted date string
        holder.bookedBy.setText(booking.getBookedBy());
        holder.bookEmail.setText(booking.getEmail());
        holder.bookContactNumber.setText(booking.getContactNumber());
        holder.bookAddress.setText(booking.getAddress());
        holder.carTextView.setText(booking.getCar());
        holder.packageCancelButton.setVisibility(View.VISIBLE);

        holder.EditetName.setText(booking.getBookedBy());
        holder.EditBookingPhoneNumberInput.setText(booking.getContactNumber());
        holder.EditBookingAddressInput.setText(booking.getAddress());
        holder.EditbookingEmailInput.setText(booking.getEmail());
        holder.editBookingDepartureDateInput.setText(formatDate(booking.getDepartureDate()));

        holder.editBookingDepartureDateInput.setFocusable(false);
        holder.editBookingDepartureDateInput.setClickable(true);

        holder.editBookingDepartureDateInput.setOnClickListener(v -> showDatePicker(holder.editBookingDepartureDateInput));



        // Fetch and populate spinners
        fetchPackageType(holder.EditpackageTypeSpinner);  // Fetch package types for spinner
        fetchCarOptions(holder.EditcarServiceSpinner);    // Fetch car options for spinner


        holder.editBookingDepartureDateInput.setOnClickListener(v -> showDatePicker(holder.editBookingDepartureDateInput));

        // Edit button functionality
        holder.package_edit_button.setOnClickListener(v -> {
            // Set the ConstraintLayout visibility to VISIBLE
            holder.editLayout.setVisibility(View.VISIBLE);
            holder.display_card.setVisibility(View.GONE);
            // Handle editing the specific booking
            editBooking(booking.getBookingId(), holder.getAdapterPosition());

        });
        holder.btnBack.setOnClickListener(view -> {
            holder.editLayout.setVisibility(View.GONE);
            holder.display_card.setVisibility(View.VISIBLE);
        });

        // Inside onBindViewHolder method
        holder.saveChanges.setOnClickListener(view -> {
            String updatedName = holder.EditetName.getText().toString();
            String updatedPhoneNumber = holder.EditBookingPhoneNumberInput.getText().toString();
            String updatedAddress = holder.EditBookingAddressInput.getText().toString();
            String updatedDepartureDate = holder.editBookingDepartureDateInput.getText().toString();
            String updatedPackage = holder.EditpackageTypeSpinner.getSelectedItem().toString();
            String updatedCarService = holder.EditcarServiceSpinner.getSelectedItem().toString();

            // Show confirmation dialog before updating booking
            showConfirmationDialog(updatedName, updatedPhoneNumber, updatedAddress, updatedDepartureDate, updatedPackage, updatedCarService, booking.getBookingId(),holder);

        });

        // Disable the cancel button if isCanceled is true
        if (isCanceled) {
            holder.packageCancelButton.setEnabled(false);
           holder.package_edit_button.setVisibility(View.GONE);
            // Optionally hide it
        } else {

            holder.packageCancelButton.setOnClickListener(v -> {
                // Show confirmation dialog before canceling
                showCancelConfirmationDialog(booking.getBookingId(), holder.getAdapterPosition());
            });
        }
    }
    private void showConfirmationDialog(String name, String phoneNumber, String address, String departureDate, String packageType, String carService, int bookingId,BookingViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.activity_confirm_booking, null);
        builder.setView(dialogView);

        TextView confirmationText = dialogView.findViewById(R.id.tvConfirmationText);
        Button btnYes = dialogView.findViewById(R.id.btnYes);
        Button btnNo = dialogView.findViewById(R.id.btnNo);

        confirmationText.setText("Are you sure you want to update this booking?");

        AlertDialog dialog = builder.create();

        btnYes.setOnClickListener(v -> {
            // Perform booking update logic here
            updateBooking(bookingId, name, phoneNumber, address, departureDate, packageType, carService);

            // Hide the edit layout
            holder.editLayout.setVisibility(View.GONE);
            holder.display_card.setVisibility(View.VISIBLE);

            dialog.dismiss();

        });

        btnNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateBooking(int bookingId, String name, String phoneNumber, String address, String departureDate, String packageType, String carService) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/edit_booking.php"; // Your API URL
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Use a JSON object to send data
        JSONObject params = new JSONObject();
        try {
            params.put("booking_id", bookingId);
            params.put("book_by", name);
            params.put("alt_contact_number", phoneNumber);
            params.put("alt_address", address);
            params.put("departure_date", departureDate);
            params.put("package", packageType);
            params.put("car", carService);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to create JSON data", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            if (success) {
                                // Optionally refresh the RecyclerView or update the item
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Failed to update booking. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Failed to update booking. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public byte[] getBody() {
                return params.toString().getBytes(); // Send the JSON as a byte array
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8"); // Set the content type to JSON
                return headers;
            }
        };

        // Add the request to the Volley request queue
        requestQueue.add(stringRequest);
    }

    private void fetchPackageType(Spinner packageTypeSpinner) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/getPackages.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context); // Use context instead of 'this'

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parsePackageResponse(response, packageTypeSpinner);  // Pass the spinner
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();  // Use context
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void parsePackageResponse(String response, Spinner packageTypeSpinner) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            List<String> packageTypes = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                packageTypes.add(jsonArray.getString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, packageTypes); // Use context
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            packageTypeSpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to parse packages", Toast.LENGTH_SHORT).show(); // Use context
        }
    }
    // Fetch car options for the car service spinner
    private void fetchCarOptions(Spinner carServiceSpinner) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/getCars.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> parseCarResponse(response, carServiceSpinner),
                error -> Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(stringRequest);
    }

    private void parseCarResponse(String response, Spinner carServiceSpinner) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            List<String> carOptions = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                carOptions.add(jsonArray.getString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, carOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            carServiceSpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to parse cars", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount(){
        return bookingList.size();
    }
    // Function to edit booking (using Volley)

    // Function to edit booking (using Volley)
    private void editBooking(int bookingId, int position) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/edit_booking.php"; // Your API URL

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // You can handle any success logic here (e.g., updating the UI)
                        // No Toast needed since you're not fetching but setting data
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add POST parameters (e.g., booking_id)
                Map<String, String> params = new HashMap<>();
                params.put("booking_id", String.valueOf(bookingId));
                return params;
            }
        };

        // Add the request to the Volley request queue
        requestQueue.add(stringRequest);
    }


    // Function to show a confirmation dialog before canceling the booking
    private void showCancelConfirmationDialog(int bookingId, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Proceed to cancel the booking
                    cancelBooking(bookingId, position);
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void showDatePicker(EditText editBookingDepartureDateInput) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.CustomDatePickerDialog,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, month1, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    editBookingDepartureDateInput.setText(dateFormat.format(selectedDate.getTime()));
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private String formatDate(String dateStr) {
        try {
            // Assuming the incoming date is in the format "MMMM dd, yyyy" (e.g., "October 12, 2024")
            SimpleDateFormat incomingFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return desiredFormat.format(incomingFormat.parse(dateStr));
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr; // Return the original string if parsing fails
        }
    }

    // Function to send the cancellation request to the server using booking_id
    private void cancelBooking(int bookingId, int position) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/cancel_booking.php";  // Replace with your API URL

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server (success or failure)
                        Toast.makeText(context, "Booking canceled successfully!", Toast.LENGTH_SHORT).show();

                        // Remove the booking from the list and notify adapter
                        bookingList.remove(position);
                        notifyItemRemoved(position);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(context, "Failed to cancel booking. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add POST parameters (e.g., booking_id)
                Map<String, String> params = new HashMap<>();
                params.put("booking_id", String.valueOf(bookingId)); // Convert int to String
                return params;
            }
        };

        // Add the request to the Volley request queue
        requestQueue.add(stringRequest);
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        // Declare your TextViews and Button here
        TextView packageNameTextView;
        TextView departureDateTextView;
        TextView bookedBy;
        TextView bookEmail;
        TextView bookContactNumber;
        TextView bookAddress;
        Button package_edit_button;
        Button packageCancelButton;
        TextView carTextView;
        ConstraintLayout editLayout;
        private Spinner EditpackageTypeSpinner, EditcarServiceSpinner;
        private EditText EditBookingPhoneNumberInput, EditBookingAddressInput, editBookingDepartureDateInput, EditetName;
        private TextView EditbookingEmailInput;
        private Button saveChanges;
        private ImageButton btnBack;
        private String userEmail;
        private LinearLayout display_card;



        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your views
            packageNameTextView = itemView.findViewById(R.id.packageNameTextView);
            departureDateTextView = itemView.findViewById(R.id.departureDateTextView);
            bookedBy = itemView.findViewById(R.id.bookedBy);
            bookEmail = itemView.findViewById(R.id.bookEmail);
            bookContactNumber = itemView.findViewById(R.id.bookContactNumber);
            bookAddress = itemView.findViewById(R.id.bookAddress);
            package_edit_button = itemView.findViewById(R.id.package_edit_button);
            packageCancelButton = itemView.findViewById(R.id.package_cancel_button2);
            carTextView = itemView.findViewById(R.id.carTextView);
            editLayout = itemView.findViewById(R.id.edit_layout);
            display_card = itemView.findViewById(R.id.display_card);

            //Initial Views for Edit
            EditetName = itemView.findViewById(R.id.EditetName);
            EditBookingPhoneNumberInput = itemView.findViewById(R.id.EditBookingPhoneNumberInput);
            EditBookingAddressInput = itemView.findViewById(R.id.EditBookingAddressInput);
            EditbookingEmailInput = itemView.findViewById(R.id.EditbookingEmailInput);
            EditpackageTypeSpinner = itemView.findViewById(R.id.EditpackageTypeSpinner);
            EditcarServiceSpinner = itemView.findViewById(R.id.EditcarServiceSpinner);
            editBookingDepartureDateInput = itemView.findViewById(R.id.editBookingDepartureDateInput);
            btnBack = itemView.findViewById(R.id.btnBack);
            saveChanges = itemView.findViewById(R.id.saveChanges);

        }
    }
}
