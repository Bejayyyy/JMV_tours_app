package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePass extends AppCompatActivity {
    ImageButton userhomebtn, useruserbtn, backbtn;
    EditText etCurrentPassword, etNewPassword, etReTypePassword;
    Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializing buttons and fields
        userhomebtn = findViewById(R.id.userhomebtn);
        useruserbtn = findViewById(R.id.useruserbtn);
        backbtn = findViewById(R.id.backbtn);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        etCurrentPassword = findViewById(R.id.setCurrentPassword);
        etNewPassword = findViewById(R.id.setNewPassword);
        etReTypePassword = findViewById(R.id.setReTypePassword);

        // Getting email from intent
        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");

        // Set up button click listeners
        backbtn.setOnClickListener(view -> {
            Intent backIntent = new Intent(ChangePass.this, User.class);
            backIntent.putExtra("userEmail", email);
            startActivity(backIntent);
        });

        useruserbtn.setOnClickListener(view -> {
            Intent userIntent = new Intent(ChangePass.this, User.class);
            userIntent.putExtra("userEmail", email);
            startActivity(userIntent);
        });

        userhomebtn.setOnClickListener(view -> {
            Intent homeIntent = new Intent(ChangePass.this, Home.class);
            homeIntent.putExtra("userEmail", email);
            startActivity(homeIntent);
        });

        // Handling password change on button click
        btnSaveChanges.setOnClickListener(v -> {
            String currentPassword = etCurrentPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            String retypePassword = etReTypePassword.getText().toString();

            // Validate inputs
            if (validateInputs(currentPassword, newPassword, retypePassword)) {
                changePassword(email, currentPassword, newPassword);
            }
        });
    }


    private void changePassword(String email, String currentPassword, String newPassword) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/changePassword.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(ChangePass.this, message, Toast.LENGTH_SHORT).show();

                            if (status.equals("success")) {
                                // Password changed successfully, redirect to User screen
                                Intent successIntent = new Intent(ChangePass.this, User.class);
                                successIntent.putExtra("userEmail", email);
                                startActivity(successIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangePass.this, "Failed to parse response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ChangePass", "Error: " + error.getMessage());
                        Toast.makeText(ChangePass.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", email);
                params.put("old_password", currentPassword);
                params.put("new_password", newPassword);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    // New validateInputs method with additional password requirements
    private boolean validateInputs(String currentPassword, String newPassword, String retypePassword) {
        TextView passwordWarning = findViewById(R.id.passwordWarning);

        // Regex to check if password contains at least one uppercase letter, one lowercase letter, and one special character
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";

        if (currentPassword.isEmpty() || newPassword.isEmpty() || retypePassword.isEmpty()) {
            passwordWarning.setText("All fields are required.");
            passwordWarning.setVisibility(View.VISIBLE);
            return false;
        }

        if (newPassword.length() < 8) {
            passwordWarning.setText("New password must be at least 8 characters long.");
            passwordWarning.setVisibility(View.VISIBLE);
            return false;
        }

        if (!newPassword.matches(passwordPattern)) {
            passwordWarning.setText("Password must contain at least one uppercase letter, one lowercase letter, and one special character.");
            passwordWarning.setVisibility(View.VISIBLE);
            return false;
        }

        if (!newPassword.equals(retypePassword)) {
            passwordWarning.setText("New password and retype password do not match.");
            passwordWarning.setVisibility(View.VISIBLE);
            return false;
        }

        passwordWarning.setVisibility(View.GONE);  // Hide the warning when the validation passes
        return true;
    }

}
