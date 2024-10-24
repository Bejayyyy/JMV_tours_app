package com.example.afinal.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
import com.example.afinal.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Canceled extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<BookingResponse> bookingList;
    private String userEmail;
    private Spinner sortOrderSpinner;
    TextView tabPending2,upcoming2,tabHistory2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_canceled);

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get email from Intent
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");

        // Initialize RecyclerView and BookingAdapter
        recyclerView = findViewById(R.id.recyclerViewCanceled); // Ensure this ID exists in your XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList, Canceled.this, false, userEmail); // Pass true to indicate "canceled" status
        recyclerView.setAdapter(bookingAdapter);
        tabPending2 = findViewById(R.id.tabPending2);
        upcoming2 = findViewById(R.id.upcoming2);
        tabHistory2 = findViewById(R.id.tabHistory2);

        tabHistory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(Canceled.this, History.class);
                userIntent.putExtra("userEmail", userEmail);
                startActivity(userIntent);
            }
        });
        upcoming2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(Canceled.this, Upcoming.class);
                userIntent.putExtra("userEmail", userEmail);
                startActivity(userIntent);
            }
        });
        tabPending2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(Canceled.this, Pending.class);
                userIntent.putExtra("userEmail", userEmail);
                startActivity(userIntent);
            }
        });
        // Set up Spinner for sorting by departure_date
        sortOrderSpinner = findViewById(R.id.sortOrderSpinner); // Ensure this ID exists in your XML
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

        fetchCanceledBookings();
    }

    private void fetchCanceledBookings() {
        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // URL for the API request to fetch canceled bookings (replace with your actual URL)
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/fetch_canceled_bookings.php?user_email=" + userEmail;

        // Create a JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the response
                            JSONArray bookingsArray = response.getJSONArray("canceledBookings");
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

                                // Extract alternative contact details (allow null or empty strings)
                                String altContactNumber = bookingObject.optString("alt_contactNumber", "");
                                String altAddress = bookingObject.optString("alt_address", "");

                                // Create BookingResponse object and add to list
                                BookingResponse booking = new BookingResponse(
                                        bookingId, packageName, departureDate, bookedBy, mainEmail,
                                        mainContactNumber, mainAddress, altContactNumber, altAddress, carName
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


    // Sort bookings by departure date
    private void sortBookingsByDepartureDate(boolean latestToOldest) {
        Collections.sort(bookingList, new Comparator<BookingResponse>() {
            @Override
            public int compare(BookingResponse b1, BookingResponse b2) {
                // Assuming departure_date is in a format that can be compared as strings (e.g., "yyyy-MM-dd")
                if (latestToOldest) {
                    return b2.getDepartureDate().compareTo(b1.getDepartureDate());
                } else {
                    return b1.getDepartureDate().compareTo(b2.getDepartureDate());
                }
            }
        });
        bookingAdapter.notifyDataSetChanged(); // Notify the adapter about the change
    }
}
