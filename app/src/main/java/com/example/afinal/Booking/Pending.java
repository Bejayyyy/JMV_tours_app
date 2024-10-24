package com.example.afinal.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.Constructor.BookingAdapter;
import com.example.afinal.Constructor.BookingResponse;
import com.example.afinal.Home;
import com.example.afinal.R;
import com.example.afinal.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Pending extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<BookingResponse> bookingList;
    private String userEmail;
    ImageView PendingbackButton;
    ImageView carthomebtn;
    ImageView cartuserbtn;
    Spinner sortOrderSpinner; // Declare the spinner for sorting
    TextView upcoming2, completted2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        // Edge-to-edge handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get email from Intent
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");

        // Initialize buttons
        carthomebtn = findViewById(R.id.carthomebtn);
        cartuserbtn = findViewById(R.id.cartuserbtn);
        upcoming2 = findViewById(R.id.upcoming2);
        completted2 = findViewById(R.id.completted2);

        completted2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upcomingIntent = new Intent(Pending.this, Canceled.class);
                upcomingIntent.putExtra("userEmail", userEmail);
                startActivity(upcomingIntent);
            }
        });
        upcoming2.setOnClickListener(view -> {
            Intent upcomingIntent = new Intent(Pending.this, Upcoming.class);
            upcomingIntent.putExtra("userEmail", userEmail);
            startActivity(upcomingIntent);
        });

        // Set button click listeners
        cartuserbtn.setOnClickListener(view -> {
            Intent userIntent = new Intent(Pending.this, User.class);
            userIntent.putExtra("userEmail", userEmail);
            startActivity(userIntent);
        });

        carthomebtn.setOnClickListener(view -> {
            Intent homeIntent = new Intent(Pending.this, Home.class);
            homeIntent.putExtra("userEmail", userEmail);
            startActivity(homeIntent);
        });

        // Initialize RecyclerView and BookingAdapter
        recyclerView = findViewById(R.id.recyclerViewPending); // Ensure this ID exists in your XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList, Pending.this, false,userEmail); // Pass the third boolean parameter
        // Pass the context
        recyclerView.setAdapter(bookingAdapter);

        // Set up Spinner for sorting by departure_date
        sortOrderSpinner = findViewById(R.id.sortOrderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOrderSpinner.setAdapter(adapter);

        // Set listener for sorting option selection
        sortOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sortBookingsByDepartureDate(true); // Sort latest to oldest
                } else {
                    sortBookingsByDepartureDate(false); // Sort oldest to latest
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up click listener for tabHistory2 to navigate to History activity
        TextView tabHistory2 = findViewById(R.id.tabHistory2);
        tabHistory2.setOnClickListener(v -> {
            Intent historyIntent = new Intent(Pending.this, History.class);
            historyIntent.putExtra("userEmail", userEmail);
            startActivity(historyIntent);
        });

        // Fetch the bookings using Volley
        refreshPendingBookings();


        // Set up back button listener
        PendingbackButton = findViewById(R.id.PendingbackButton);
        PendingbackButton.setOnClickListener(view -> {
            Intent backIntent = new Intent(Pending.this, Home.class);
            backIntent.putExtra("userEmail", userEmail);
            startActivity(backIntent);
        });
    }

    private void refreshPendingBookings() {
        fetchPendingBookings(); // Call the existing method to fetch bookings
    }

    private void fetchPendingBookings() {
        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // URL for the API request to fetch pending bookings
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/fetch_pending_bookings.php?user_email=" + userEmail;

        // Create a JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log the API response
                        Log.d("API Response", response.toString());

                        try {
                            // Parse the response
                            JSONArray bookingsArray = response.getJSONArray("pendingBookings");
                            bookingList.clear(); // Clear existing bookings before adding new ones

                            for (int i = 0; i < bookingsArray.length(); i++) {
                                JSONObject bookingObject = bookingsArray.getJSONObject(i);

                                // Extract main details from JSON
                                int bookingId = bookingObject.getInt("booking_id");
                                String packageName = bookingObject.getString("package_name");
                                String departureDate = bookingObject.getString("departure_date");
                                String bookedBy = bookingObject.getString("book_by");
                                String mainEmail = bookingObject.getString("user_email");
                                String mainContactNumber = bookingObject.getString("user_contactNumber");
                                String mainAddress = bookingObject.getString("user_address");
                                String carName = bookingObject.getString("car_name");

                                // Extract alternative contact details
                                String altContactNumber = bookingObject.optString("alt_contact_number", ""); // Corrected key
                                String altAddress = bookingObject.optString("alt_address", "");

                                // Determine which contact number and address to use
                                String displayContactNumber = altContactNumber.isEmpty() ? mainContactNumber : altContactNumber;
                                String displayAddress = altAddress.isEmpty() ? mainAddress : altAddress;

                                // Create BookingResponse object and add to list
                                BookingResponse booking = new BookingResponse(
                                        bookingId, packageName, departureDate, bookedBy, mainEmail,
                                        displayContactNumber, displayAddress, altContactNumber, altAddress, carName
                                );
                                bookingList.add(booking);
                            }

                            // Notify the adapter that the data has changed
                            bookingAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace(); // Log error if JSON parsing fails
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); // Log error if request fails
                    }
                });

        // Add the request to the RequestQueue
        queue.add(request);
    }





    // Method to sort bookings by departure_date
    private void sortBookingsByDepartureDate(boolean latestToOldest) {
        Collections.sort(bookingList, new Comparator<BookingResponse>() {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy"); // Adjust format to match your new date format

            @Override
            public int compare(BookingResponse b1, BookingResponse b2) {
                try {
                    Date date1 = sdf.parse(b1.getDepartureDate());
                    Date date2 = sdf.parse(b2.getDepartureDate());
                    // Sorting logic based on the departure_date
                    return latestToOldest ? date2.compareTo(date1) : date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0; // In case of a parsing error, keep the original order
                }
            }
        });
        bookingAdapter.notifyDataSetChanged(); // Notify adapter of the change
    }
}
