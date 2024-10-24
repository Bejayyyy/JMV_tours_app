package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError; // Import for VolleyError
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley; // Ensure Volley is imported for RequestQueue
import com.example.afinal.Booking.Pending;

import org.json.JSONException;
import org.json.JSONObject;

public class Destinations extends AppCompatActivity {
    LinearLayout destinationPackagebtn;
    LinearLayout destinationCarsbtn;
    ImageButton destinationcartbtn;
    ImageButton destinationhomebtn;
    ImageButton destinationuserbtn;
    TextView displayName2; // Changed from displayName to displayName2 for consistency
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destinations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        destinationPackagebtn = findViewById(R.id.packagesbtn3);
        destinationCarsbtn = findViewById(R.id.destinationCarsbtn);
        destinationcartbtn = findViewById(R.id.destinationcartbtn);
        destinationhomebtn = findViewById(R.id.destinationhomebtn);
        destinationuserbtn = findViewById(R.id.destinationuserbtn);
        displayName2 = findViewById(R.id.displayName2); // Ensure you're using the correct TextView

        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");

        // Set click listeners
        destinationhomebtn.setOnClickListener(view -> {
            Intent homeIntent = new Intent(Destinations.this, Home.class);
            homeIntent.putExtra("userEmail", email);
            startActivity(homeIntent);
        });

        destinationcartbtn.setOnClickListener(view -> {
            Intent pendingIntent = new Intent(Destinations.this, Pending.class);
            pendingIntent.putExtra("userEmail", email);
            startActivity(pendingIntent);
        });

        destinationuserbtn.setOnClickListener(view -> {
            Intent userIntent = new Intent(Destinations.this, User.class);
            userIntent.putExtra("userEmail", email);
            startActivity(userIntent);
        });

        destinationPackagebtn.setOnClickListener(view -> {
            Intent packageIntent = new Intent(Destinations.this, Package.class);
            packageIntent.putExtra("userEmail", email);
            startActivity(packageIntent);
        });

        destinationCarsbtn.setOnClickListener(view -> {
            Intent carIntent = new Intent(Destinations.this, Car.class);
            carIntent.putExtra("userEmail", email);
            startActivity(carIntent);
        });

        // Fetch user profile
        if (email != null) {
            fetchUserProfile(email);
        }
    }

    private void fetchUserProfile(String email) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/getUserDetails.php?email=" + email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("user_name")) {
                                String userName = response.getString("user_name"); // Access user_name from response
                                displayName2.setText(userName); // Set user_name to displayName2 TextView
                            } else {
                                displayName2.setText("Name not found");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            displayName2.setText("Error parsing data");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        displayName2.setText("Error fetching data");
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
