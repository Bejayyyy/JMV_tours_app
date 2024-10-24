package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.afinal.Response.UserProfileResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class userProfile extends AppCompatActivity {
    ImageView editIcon;
    TextView userProfileFirstName; // Add for first name
    TextView userProfileLastName;  // Add for last name
    TextView userProfileEmail;
    TextView userProfileAddress;
    TextView userProfileContactNumber;

    ImageButton btnBack;
    ImageButton userhomebtn;
    ImageButton usercartbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize new TextViews
        userProfileFirstName = findViewById(R.id.userFirstName); // Make sure this ID matches your XML
        userProfileLastName = findViewById(R.id.userSecondName); // Make sure this ID matches your XML
        editIcon = findViewById(R.id.editIcon);
        userProfileEmail = findViewById(R.id.userProfileEmail);
        userProfileAddress = findViewById(R.id.userProfileAddress);
        userProfileContactNumber = findViewById(R.id.userProfileContactNumber);

        btnBack = findViewById(R.id.btnBack);
        userhomebtn = findViewById(R.id.userhomebtn);
        usercartbtn = findViewById(R.id.usercartbtn);

        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");

        btnBack.setOnClickListener(view -> {
            Intent intentBack = new Intent(userProfile.this, User.class);
            intentBack.putExtra("userEmail", email);
            startActivity(intentBack);
        });

        if (email != null) {
            fetchUserProfile(email);
        } else {
            Toast.makeText(userProfile.this, "Error: Email not Found", Toast.LENGTH_SHORT).show();
        }

        editIcon.setOnClickListener(view -> {
            Intent intentEdit = new Intent(userProfile.this, EditProfile.class);
            intentEdit.putExtra("userEmail", email);
            startActivity(intentEdit);
        });

        userhomebtn.setOnClickListener(view -> {
            Intent intentHome = new Intent(userProfile.this, Home.class);
            intentHome.putExtra("userEmail", email);
            startActivity(intentHome);
        });

        usercartbtn.setOnClickListener(view -> {
            Intent intentCart = new Intent(userProfile.this, Pending.class);
            intentCart.putExtra("userEmail", email);
            startActivity(intentCart);
        });
    }

    private void fetchUserProfile(String email) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/user_profile.php?email=" + email; // Replace with your actual API endpoint

        // Create JSON object to send the email as parameter
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObject,
                response -> {
                    try {
                        // Check if the response is successful
                        if (response.getBoolean("success")) {
                            userProfileFirstName.setText(response.getString("first_name"));
                            userProfileLastName.setText(response.getString("last_name"));
                            userProfileEmail.setText(response.getString("user_email"));
                            userProfileAddress.setText(response.getString("user_address"));
                            userProfileContactNumber.setText(response.getString("user_contactNumber"));
                        } else {
                            Toast.makeText(userProfile.this, "Profile not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(userProfile.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(userProfile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
