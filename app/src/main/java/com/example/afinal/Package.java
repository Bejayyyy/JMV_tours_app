package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.Booking.Booking;
import com.example.afinal.Booking.Pending;
import com.example.afinal.Constructor.PackageAdapter; // Import your PackageAdapter
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Package extends AppCompatActivity {
    ImageView package_profile_icon;
    LinearLayout packageCarsbtn;
    LinearLayout packageDestinationBtn;
    LinearLayout packageTourLayout;
    LinearLayout cebuTourLayout;
    LinearLayout oslobpackageTourLayout;
    LinearLayout safariTourLayout;
    LinearLayout simalaTourLayout;
    ImageButton packagecartbtn;
    ImageButton packagehomebtn;
    Button btnBookNowCebutour;
    TextView displayName;
    RecyclerView for_item_package;
    private List<com.example.afinal.Constructor.Package> packageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_package);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");


        // Initialize UI components
        package_profile_icon = findViewById(R.id.pckageuserbtn);
        packageCarsbtn = findViewById(R.id.packageCarsbtn);
        packageDestinationBtn = findViewById(R.id.packageDestinationBtn);
        packageTourLayout = findViewById(R.id.packageTourLayout);
        cebuTourLayout = findViewById(R.id.cebuTourLayout);
        oslobpackageTourLayout = findViewById(R.id.oslobpackageTourLayout);
        safariTourLayout = findViewById(R.id.safariTourLayout);
        simalaTourLayout = findViewById(R.id.simalaTourLayout);
        packagecartbtn = findViewById(R.id.packagecartbtn);
        packagehomebtn = findViewById(R.id.packagehomebtn);
        btnBookNowCebutour = findViewById(R.id.btnBookNowCebutour);
        displayName = findViewById(R.id.displayName);
        for_item_package = findViewById(R.id.for_item_package);

        List<com.example.afinal.Constructor.Package> packageList = new ArrayList<>(); // Populate this list with actual packages

        // Set the RecyclerView layout manager
        for_item_package.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter for the RecyclerView
        PackageAdapter packageAdapter = new PackageAdapter(this, packageList,email);
        for_item_package.setAdapter(packageAdapter);


        // Fetch user profile data
        fetchUserProfile(email);

        // Set up button click listeners
        btnBookNowCebutour.setOnClickListener(view -> {
            Intent bookingIntent = new Intent(Package.this, Booking.class);
            bookingIntent.putExtra("userEmail", email);
            startActivity(bookingIntent);
        });

        packagehomebtn.setOnClickListener(view -> {
            Intent homeIntent = new Intent(Package.this, Home.class);
            homeIntent.putExtra("userEmail", email);
            startActivity(homeIntent);
        });

        packagecartbtn.setOnClickListener(view -> {
            Intent pendingIntent = new Intent(Package.this, Pending.class);
            pendingIntent.putExtra("userEmail", email);
            startActivity(pendingIntent);
        });

        package_profile_icon.setOnClickListener(view -> {
            Intent userIntent = new Intent(Package.this, User.class);
            userIntent.putExtra("userEmail", email);
            startActivity(userIntent);
        });

        packageCarsbtn.setOnClickListener(view -> {
            Intent carIntent = new Intent(Package.this, Car.class);
            carIntent.putExtra("userEmail", email);
            startActivity(carIntent);
        });

        packageDestinationBtn.setOnClickListener(view -> {
            Intent destinationIntent = new Intent(Package.this, Destinations.class);
            destinationIntent.putExtra("userEmail", email);
            startActivity(destinationIntent);
        });

        packageTourLayout.setOnClickListener(view -> {
            Intent packageTourIntent = new Intent(Package.this, PackageTour.class);
            packageTourIntent.putExtra("userEmail", email);
            startActivity(packageTourIntent);
        });

        cebuTourLayout.setOnClickListener(view -> {
            Intent cebuTourIntent = new Intent(Package.this, CebuTour.class);
            cebuTourIntent.putExtra("userEmail", email);
            startActivity(cebuTourIntent);
        });

        oslobpackageTourLayout.setOnClickListener(view -> {
            Intent oslobTourIntent = new Intent(Package.this, OslobTour.class);
            oslobTourIntent.putExtra("userEmail", email);
            startActivity(oslobTourIntent);
        });

        safariTourLayout.setOnClickListener(view -> {
            Intent safariTourIntent = new Intent(Package.this, SafariTour.class);
            safariTourIntent.putExtra("userEmail", email);
            startActivity(safariTourIntent);
        });

        simalaTourLayout.setOnClickListener(view -> {
            Intent simalaTourIntent = new Intent(Package.this, SimalaTour.class);
            simalaTourIntent.putExtra("userEmail", email);
            startActivity(simalaTourIntent);
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
                                displayName.setText(userName); // Set user_name to displayName TextView
                            } else {
                                displayName.setText("Name not found");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            displayName.setText("Error parsing data");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        displayName.setText("Error fetching data");
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
