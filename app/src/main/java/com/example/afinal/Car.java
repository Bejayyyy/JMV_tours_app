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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.Booking.Pending;

import org.json.JSONException;
import org.json.JSONObject;

public class Car extends AppCompatActivity {
    LinearLayout carsPackagebtn;
    LinearLayout carsDestinationbtn;
    ImageButton destinationcartbtn;
    ImageButton destinationhomebtn;
    ImageButton destinationmuserbtn;
    TextView displayName; // Declare TextView for displayName
    TextView displayName2; // Declare TextView for displayName2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        carsPackagebtn = findViewById(R.id.carsPackagebtn);
        carsDestinationbtn = findViewById(R.id.carsDestinationbtn);
        destinationcartbtn = findViewById(R.id.destinationcartbtn);
        destinationhomebtn = findViewById(R.id.destinationhomebtn);
        destinationmuserbtn = findViewById(R.id.destinationmuserbtn);
        displayName = findViewById(R.id.displayName); // Initialize displayName TextView
        displayName2 = findViewById(R.id.displayName2); // Initialize displayName2 TextView

        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");

        // Fetch user profile data
        fetchUserProfile(email);

        // Set click listeners for buttons
        destinationcartbtn.setOnClickListener(view -> {
            Intent pendingIntent = new Intent(Car.this, Pending.class);
            pendingIntent.putExtra("userEmail", email);
            startActivity(pendingIntent);
        });

        destinationmuserbtn.setOnClickListener(view -> {
            Intent userIntent = new Intent(Car.this, User.class);
            userIntent.putExtra("userEmail", email);
            startActivity(userIntent);
        });

        destinationhomebtn.setOnClickListener(view -> {
            Intent homeIntent = new Intent(Car.this, Home.class);
            homeIntent.putExtra("userEmail", email);
            startActivity(homeIntent);
        });

        carsPackagebtn.setOnClickListener(view -> {
            Intent packageIntent = new Intent(Car.this, Package.class);
            packageIntent.putExtra("userEmail", email);
            startActivity(packageIntent);
        });

        carsDestinationbtn.setOnClickListener(view -> {
            Intent destinationIntent = new Intent(Car.this, Destinations.class);
            destinationIntent.putExtra("userEmail", email);
            startActivity(destinationIntent);
        });
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
                                displayName.setText("Hello,"); // Set greeting text
                                displayName2.setText(userName); // Set user_name to displayName2 TextView
                            } else {
                                displayName.setText("Name not found");
                                displayName2.setText(""); // Clear second TextView
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            displayName.setText("Error parsing data");
                            displayName2.setText(""); // Clear second TextView
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        displayName.setText("Error fetching data");
                        displayName2.setText(""); // Clear second TextView
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
