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

public class Upcoming extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<BookingResponse> bookingList;
    private String userEmail;
    ImageView upcomingBackButton;
    ImageView carHomeBtn;
    ImageView cartUserBtn;
    Spinner sortOrderSpinner; // Declare the spinner for sorting
    TextView tabPending2,tabHistory2,completted2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

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
        carHomeBtn = findViewById(R.id.carthomebtn);
        cartUserBtn = findViewById(R.id.cartuserbtn);
        tabPending2 = findViewById(R.id.tabPending2);
        tabHistory2 = findViewById(R.id.tabHistory2);
        completted2 = findViewById(R.id.completted2);

        tabPending2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(Upcoming.this, Pending.class);
                userIntent.putExtra("userEmail", userEmail);
                startActivity(userIntent);
            }
        });
        tabHistory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(Upcoming.this, History.class);
                userIntent.putExtra("userEmail", userEmail);
                startActivity(userIntent);
            }
        });
        completted2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(Upcoming.this, Canceled.class);
                userIntent.putExtra("userEmail", userEmail);
                startActivity(userIntent);
            }
        });
        // Set button click listeners
        cartUserBtn.setOnClickListener(view -> {
            Intent userIntent = new Intent(Upcoming.this, User.class);
            userIntent.putExtra("userEmail", userEmail);
            startActivity(userIntent);
        });

        carHomeBtn.setOnClickListener(view -> {
            Intent homeIntent = new Intent(Upcoming.this, Home.class);
            homeIntent.putExtra("userEmail", userEmail);
            startActivity(homeIntent);
        });

        // Initialize RecyclerView and BookingAdapter
        recyclerView = findViewById(R.id.recyclerViewUpcoming); // Ensure this ID exists in your XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList, Upcoming.this,true,userEmail); // Pass the context
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

        // Set up back button listener
        upcomingBackButton = findViewById(R.id.upcomingbackButton);
        upcomingBackButton.setOnClickListener(view -> {
            Intent backIntent = new Intent(Upcoming.this, Home.class);
            backIntent.putExtra("userEmail", userEmail);
            startActivity(backIntent);
        });

        // Fetch the upcoming bookings using Volley
        fetchUpcomingBookings();
    }

    private void fetchUpcomingBookings() {
        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // URL for the API request to fetch upcoming bookings (replace with your actual URL)
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/fetch_upcoming_bookings.php?user_email=" + userEmail;

        // Create a JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log the API response
                        Log.d("API Response", response.toString());

                        try {
                            // Parse the response
                            JSONArray bookingsArray = response.getJSONArray("upcomingBookings");
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
