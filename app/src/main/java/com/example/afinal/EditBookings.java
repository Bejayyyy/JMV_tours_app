package com.example.afinal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.afinal.Booking.Pending;
import com.example.afinal.Constructor.RequestQueueSingleton;

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

public class EditBookings extends AppCompatActivity {

    private EditText etName, BookingPhoneNumberInput, BookingEmailInput, BookingAddressInput, BookingDepartureDateInput;
    private Spinner packageTypeSpinner, carServiceSpinner;
    private Button saveButton;
    private String userEmail;
    private int bookingId;
    private boolean packagesFetched = false;
    private boolean carsFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_bookings);

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        etName = findViewById(R.id.etName);
        BookingPhoneNumberInput = findViewById(R.id.BookingPhoneNumberInput);
        BookingEmailInput = findViewById(R.id.BookingEmailInput);
        BookingAddressInput = findViewById(R.id.BookingAddressInput);
        BookingDepartureDateInput = findViewById(R.id.BookingDepartureDateInput);
        packageTypeSpinner = findViewById(R.id.packageTypeSpinner);
        carServiceSpinner = findViewById(R.id.carServiceSpinner);
        saveButton = findViewById(R.id.saveButton);
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("user_email"); // Retrieve user_email from Intent

        bookingId = intent.getIntExtra("booking_id", -1); // Default to -1 if not found

        // Fetch the data
        fetchCarOptions();
        fetchPackageType();
        fetchBookingData(bookingId);
        setupDatePicker();

        // Set up the save button listener
        saveButton.setOnClickListener(v -> showConfirmationDialog());
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_confirm_booking, null);
        builder.setView(dialogView);

        TextView confirmationText = dialogView.findViewById(R.id.tvConfirmationText);
        Button btnYes = dialogView.findViewById(R.id.btnYes);
        Button btnNo = dialogView.findViewById(R.id.btnNo);

        confirmationText.setText("Are you sure you want to save changes?");

        AlertDialog dialog = builder.create();

        btnYes.setOnClickListener(v -> {
            updateBooking();
            dialog.dismiss();
        });

        btnNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void fetchBookingData(int bookingId) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/getBooking.php"; // Replace with your API URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            JSONObject bookingData = jsonObject.getJSONObject("data");
                            etName.setText(bookingData.getString("booker_name"));
                            BookingPhoneNumberInput.setText(bookingData.getString("contact_number"));
                            BookingEmailInput.setText(bookingData.getString("email"));
                            BookingAddressInput.setText(bookingData.getString("address"));
                            BookingDepartureDateInput.setText(bookingData.getString("departure_date"));

                            // Set the selected item for spinners
                            if (carsFetched && packagesFetched) {
                                setSpinnerSelection(carServiceSpinner, bookingData.getString("car_name"));
                                setSpinnerSelection(packageTypeSpinner, bookingData.getString("package_name"));
                            }
                        } else {
                            Toast.makeText(EditBookings.this, "Booking not found.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(EditBookings.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(EditBookings.this, "Error fetching booking data.", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("booking_id", String.valueOf(bookingId)); // Send booking ID to server
                return params;
            }
        };

        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void updateBooking() {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/updateBooking.php"; // Replace with your API URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(EditBookings.this, "Booking updated successfully.", Toast.LENGTH_SHORT).show();
                    // Navigate to Pending activity
                    Intent intent = new Intent(EditBookings.this, Pending.class);
                    intent.putExtra("email" ,userEmail);
                    startActivity(intent);
                    finish(); // Optional: finish the current activity
                },
                error -> Toast.makeText(EditBookings.this, "Error updating booking.", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("booking_id", String.valueOf(bookingId));
                params.put("booker_name", etName.getText().toString());
                params.put("contact_number", BookingPhoneNumberInput.getText().toString());
                params.put("email", BookingEmailInput.getText().toString());
                params.put("address", BookingAddressInput.getText().toString());
                params.put("departure_date", BookingDepartureDateInput.getText().toString());
                params.put("car_name", carServiceSpinner.getSelectedItem().toString());
                params.put("package_name", packageTypeSpinner.getSelectedItem().toString());

                return params;
            }
        };

        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void fetchCarOptions() {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/getCars.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                this::parseCarResponse,
                error -> Toast.makeText(EditBookings.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void parseCarResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            List<String> carOptions = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                carOptions.add(jsonArray.getString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditBookings.this, android.R.layout.simple_spinner_item, carOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            carServiceSpinner.setAdapter(adapter);
            carsFetched = true; // Mark that car options have been fetched
            fetchBookingData(bookingId); // Fetch booking data only after cars are fetched
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(EditBookings.this, "Failed to parse cars", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupDatePicker() {
        BookingDepartureDateInput.setInputType(InputType.TYPE_NULL);
        BookingDepartureDateInput.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(calendar.getTime());
                    BookingDepartureDateInput.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void fetchPackageType() {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/getPackages.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                this::parsePackageResponse,
                error -> Toast.makeText(EditBookings.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void parsePackageResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            List<String> packageOptions = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                packageOptions.add(jsonArray.getString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditBookings.this, android.R.layout.simple_spinner_item, packageOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            packageTypeSpinner.setAdapter(adapter);
            packagesFetched = true; // Mark that package options have been fetched
            fetchBookingData(bookingId); // Fetch booking data only after packages are fetched
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(EditBookings.this, "Failed to parse packages", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int position = adapter.getPosition(value);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }
}
