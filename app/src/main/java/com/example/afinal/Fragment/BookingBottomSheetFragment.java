package com.example.afinal.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.Booking.Booking;
import com.example.afinal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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

public class BookingBottomSheetFragment extends BottomSheetDialogFragment {

    private Spinner packageTypeSpinner, carServiceSpinner;
    private EditText bookingPhoneNumberInput, bookingAddressInput, bookingDepartureDateInput, etName,
            etAlternativeName, etAlternativeContact, etAlternativeAddress;
    private TextView bookingEmailInput;
    private Button bookNowButton;
    private ImageButton btnBack;
    private String userEmail; // Variable to hold the user's email
    private LinearLayout mainDetails;
    private CheckBox useAlternativeDetailsCheckBox;

    // Create a new instance of the fragment with userEmail
    public static BookingBottomSheetFragment newInstance(String email) {
        BookingBottomSheetFragment fragment = new BookingBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("userEmail", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        Bundle args = getArguments();
        // Retrieve the email from the arguments
        if (args != null) {
            userEmail = args.getString("userEmail"); // Assign to instance variable
            Log.d("BookingBottomSheet", "User email: " + userEmail);
        }

        // Initialize views
        etName = view.findViewById(R.id.etName);
        bookingPhoneNumberInput = view.findViewById(R.id.BookingPhoneNumberInput);
        bookingAddressInput = view.findViewById(R.id.BookingAddressInput);
        bookingEmailInput = view.findViewById(R.id.BookingEmailInput);
        packageTypeSpinner = view.findViewById(R.id.packageTypeSpinner);
        carServiceSpinner = view.findViewById(R.id.carServiceSpinner);
        bookingDepartureDateInput = view.findViewById(R.id.BookingDepartureDateInput);
        bookNowButton = view.findViewById(R.id.bookNowButton);
        btnBack = view.findViewById(R.id.btnBack);
        mainDetails = view.findViewById(R.id.mainDetails);
        useAlternativeDetailsCheckBox = view.findViewById(R.id.useAlternativeDetailsCheckBox);

        //Alternative
        etAlternativeName = view.findViewById(R.id.etAlternativeName);
        etAlternativeContact = view.findViewById(R.id.etAlternativeContact);
        etAlternativeAddress = view.findViewById(R.id.etAlternativeAddress);

        // Set email input
        bookingEmailInput.setText(userEmail);

        // Fetch user details and populate form fields
        fetchUserDetails(userEmail); // Call with the instance variable

        // Setup DatePickerDialog for Departure Date input
        setupDatePicker();

        // Fetch data for PackageType and Car Service spinners
        fetchPackageType();
        fetchCarOptions();

        // Handle back button
        btnBack.setOnClickListener(v -> dismiss());

        // Handle "Book Now" button click
        handleBookNowButtonClick();

        useAlternativeDetailsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show alternative details
                useAlternativeDetailsCheckBox.setVisibility(View.VISIBLE);
                mainDetails.setVisibility(View.GONE);
            } else {
                // Hide alternative details
                useAlternativeDetailsCheckBox.setVisibility(View.GONE);
                mainDetails.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void setupDatePicker() {
        bookingDepartureDateInput.setInputType(InputType.TYPE_NULL);
        bookingDepartureDateInput.setOnClickListener(v -> showDatePicker());
    }

    private void handleBookNowButtonClick() {
        bookNowButton.setOnClickListener(v -> {
            if (validateInputs()) {
                showConfirmationDialog();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.CustomDatePickerDialog, (view, year1, month1, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year1, month1, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            bookingDepartureDateInput.setText(dateFormat.format(selectedDate.getTime()));
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void fetchPackageType() {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/getPackages.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                this::parsePackageResponse,
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(stringRequest);
    }

    private void parsePackageResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            List<String> packageTypes = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                packageTypes.add(jsonArray.getString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, packageTypes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            packageTypeSpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to parse packages", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCarOptions() {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/getCars.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                this::parseCarResponse,
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(stringRequest);
    }

    private void parseCarResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            List<String> carOptions = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                carOptions.add(jsonArray.getString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, carOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            carServiceSpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to parse cars", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        String email = bookingEmailInput.getText().toString().trim();
        String phoneNumber = bookingPhoneNumberInput.getText().toString().trim();
        String address = bookingAddressInput.getText().toString().trim();
        String departureDate = bookingDepartureDateInput.getText().toString().trim();

        if (email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || departureDate.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isDepartureDateInvalid(departureDate)) {
            Toast.makeText(getContext(), "Departure date must be today or in the future", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isDepartureDateInvalid(String departureDate) {
        String[] dateParts = departureDate.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based
        int day = Integer.parseInt(dateParts[2]);
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        return selectedDate.before(Calendar.getInstance());
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Booking")
                .setMessage("Do you want to proceed with the booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Handle booking confirmation
                    // You can call the API to save the booking here
                    Toast.makeText(getContext(), "Booking confirmed!", Toast.LENGTH_SHORT).show();
                    dismiss(); // Close the dialog
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void fetchUserDetails(String email) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/getUser.php?email=" + email;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                this::parseUserDetailsResponse,
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(stringRequest);
    }

    private void parseUserDetailsResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("full_name")) {

                String firstName = jsonObject.getString("first_name");
                String lastName = jsonObject.getString("last_name");
                String fullName = jsonObject.getString("full_name");
                String phone = jsonObject.getString("user_contactNumber");
                String address = jsonObject.getString("user_address");

                Log.d("User Profile", "Full Name: " + firstName);
                // Populate the fields with fetched data
                etName.setText(firstName);
                bookingPhoneNumberInput.setText(phone);
                bookingAddressInput.setText(address);
                Log.d("JSON Response", jsonObject.toString());
                Log.d("Fetched User Details", "Name: " + firstName + ", Phone: " + phone + ", Address: " + address);
            } else if (jsonObject.has("status") && jsonObject.getString("status").equals("error")) {
                String errorMessage = jsonObject.getString("message");

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
        }
    }
}
