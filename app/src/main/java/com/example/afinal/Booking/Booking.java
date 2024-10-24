    package com.example.afinal.Booking;

    import android.app.DatePickerDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.text.InputType;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.LinearLayout;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;

    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;
    import com.example.afinal.Home;
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

    public class Booking extends AppCompatActivity {
        private Spinner packageTypeSpinner, carServiceSpinner;
        private EditText bookingPhoneNumberInput, bookingAddressInput, bookingDepartureDateInput, etName;
        private TextView bookingEmailInput;
        private Button bookNowButton;
        private ImageButton btnBack;
        private String userEmail;
        private LinearLayout mainDetails;
        EditText etAlternativeName, etAlternativeContact, etAlternativeAddress;
        private String packageTitle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_booking);

            packageTypeSpinner = findViewById(R.id.packageTypeSpinner);
            carServiceSpinner = findViewById(R.id.carServiceSpinner);
            bookingPhoneNumberInput = findViewById(R.id.BookingPhoneNumberInput);
            bookingAddressInput = findViewById(R.id.BookingAddressInput);
            bookingDepartureDateInput = findViewById(R.id.BookingDepartureDateInput);
            bookingEmailInput = findViewById(R.id.BookingEmailInput);
            bookNowButton = findViewById(R.id.bookNowButton);
            btnBack = findViewById(R.id.btnBack);
            etName = findViewById(R.id.etName);
            mainDetails = findViewById(R.id.mainDetails);
            etAlternativeName = findViewById(R.id.etAlternativeName);
            etAlternativeContact = findViewById(R.id.etAlternativeContact);
            etAlternativeAddress = findViewById(R.id.etAlternativeAddress);


            // Get email from Intent
            Intent intent = getIntent();
            userEmail = intent.getStringExtra("userEmail");
            bookingEmailInput.setText(userEmail);
            packageTitle = getIntent().getStringExtra("packageTitle");


            // Fetch user details and populate form fields
            fetchUserDetails(userEmail);

            // Setup DatePickerDialog for Departure Date input
            setupDatePicker();

            // Fetch data for PackageType and Car Service spinners
            fetchPackageType();
            fetchCarOptions();
            CheckBox alternativeDetailsCheckBox = findViewById(R.id.useAlternativeDetailsCheckBox);
            LinearLayout alternativeDetailsLayout = findViewById(R.id.alternativeDetailsLayout);

            alternativeDetailsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Show alternative details
                    alternativeDetailsLayout.setVisibility(View.VISIBLE);
                    mainDetails.setVisibility(View.GONE);
                } else {
                    // Hide alternative details
                    alternativeDetailsLayout.setVisibility(View.GONE);
                    mainDetails.setVisibility(View.VISIBLE);
                }
            });

            // Handle back button
            handleBackButton(userEmail);

            // Handle "Book Now" button click
            handleBookNowButtonClick();

        }

        private void setupDatePicker() {
            bookingDepartureDateInput.setInputType(InputType.TYPE_NULL);
            bookingDepartureDateInput.setOnClickListener(v -> showDatePicker());
        }

        private void handleBackButton(String userEmail) {
            btnBack.setOnClickListener(view -> {
                Intent homeIntent = new Intent(Booking.this, Home.class);
                homeIntent.putExtra("userEmail", userEmail);
                startActivity(homeIntent);
            });
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

            // Create DatePickerDialog with the custom style
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, (view, year1, month1, dayOfMonth) -> {
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
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> parsePackageResponse(response),
                    error -> Toast.makeText(Booking.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

            requestQueue.add(stringRequest);
        }

        private void parsePackageResponse(String response) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                List<String> packageTypes = new ArrayList<>();

                packageTypes.add(packageTitle);


                for (int i = 0; i < jsonArray.length(); i++) {
                    packageTypes.add(jsonArray.getString(i));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Booking.this, android.R.layout.simple_spinner_item, packageTypes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                packageTypeSpinner.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Booking.this, "Failed to parse packages", Toast.LENGTH_SHORT).show();
            }
        }

        private void fetchCarOptions() {
            String url = "https://honeydew-albatross-910973.hostingersite.com/api/getCars.php";
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> parseCarResponse(response),
                    error -> Toast.makeText(Booking.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

            requestQueue.add(stringRequest);
        }

        private void parseCarResponse(String response) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                List<String> carOptions = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    carOptions.add(jsonArray.getString(i));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Booking.this, android.R.layout.simple_spinner_item, carOptions);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                carServiceSpinner.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Booking.this, "Failed to parse cars", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean validateInputs() {
            String email = bookingEmailInput.getText().toString().trim();
            String phoneNumber = bookingPhoneNumberInput.getText().toString().trim();
            String address = bookingAddressInput.getText().toString().trim();
            String departureDate = bookingDepartureDateInput.getText().toString().trim();

            if (email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || departureDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (isDepartureDateInvalid(departureDate)) {
                Toast.makeText(this, "Departure date must be today or in the future", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }

        private boolean isDepartureDateInvalid(String departureDate) {
            String[] dateParts = departureDate.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int day = Integer.parseInt(dateParts[2]);

            Calendar departureCalendar = Calendar.getInstance();
            departureCalendar.set(year, month, day, 0, 0, 0);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            return departureCalendar.before(today);
        }

        private void showConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_confirm_booking, null);
            builder.setView(dialogView);

            TextView confirmationText = dialogView.findViewById(R.id.tvConfirmationText);
            Button btnYes = dialogView.findViewById(R.id.btnYes);
            Button btnNo = dialogView.findViewById(R.id.btnNo);

            confirmationText.setText("Are you sure you want to book?");

            AlertDialog dialog = builder.create();

            btnYes.setOnClickListener(v -> {
                // Perform booking logic here
                insertBookingDetails();
                dialog.dismiss();
            });

            btnNo.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }

        private void fetchUserDetails(String email) {
            String url = "https://honeydew-albatross-910973.hostingersite.com/api/getUser.php"; // Replace with actual URL

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Log.d("API Response", response); // Check the raw response

                        try {
                            // Check if the response is valid JSON
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
                                Toast.makeText(Booking.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Failed to parse the response
                            Log.e("JSON Parse Error", e.toString());
                            Toast.makeText(Booking.this, "Failed to parse user details: " + response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        // Handle request errors
                        Log.e("API Error", error.toString());
                        Toast.makeText(Booking.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email); // Send the email as a POST parameter
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }


        private void insertBookingDetails() {
            String email = bookingEmailInput.getText().toString();
            String packageType = packageTypeSpinner.getSelectedItem().toString(); // Selected package title
            String carType = carServiceSpinner.getSelectedItem().toString();
            String departureDate = bookingDepartureDateInput.getText().toString();

            // Main Booker Details
            String bookBy = etName.getText().toString(); // Get the name input

            // Alternative Booker Details
            String altName = etAlternativeName.getText().toString();
            String altContact = etAlternativeContact.getText().toString();
            String altAddress = etAlternativeAddress.getText().toString();

            // URL of your insertBooking.php
            String url = "https://honeydew-albatross-910973.hostingersite.com/api/insertBooking.php";

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        // Handle response
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");
                            Toast.makeText(Booking.this, message, Toast.LENGTH_SHORT).show();
                            if (success) {
                                Intent homeIntent = new Intent(Booking.this, Pending.class);
                                homeIntent.putExtra("userEmail", userEmail);
                                startActivity(homeIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Booking.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(Booking.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("package", packageType); // Using selected package title directly
                    params.put("car", carType);
                    params.put("departure_date", departureDate);

                    // Check if alternative details are provided, otherwise use main details
                    if (!altName.isEmpty()) {
                        params.put("book_by", altName);  // Alternative Booker Name
                    } else {
                        params.put("book_by", bookBy);  // Main Booker Name
                    }

                    if (!altContact.isEmpty()) {
                        params.put("alt_contact_number", altContact);  // Alternative Contact Number
                    }

                    if (!altAddress.isEmpty()) {
                        params.put("alt_address", altAddress);  // Alternative Address
                    }

                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }
    }