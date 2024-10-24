package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NewPasswordActivity extends AppCompatActivity {
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button submitButton;
    private TextView passwordRequirementsTextView;
    private TextView newPasswordErrorTextView;       // TextView for new password errors
    private TextView confirmPasswordErrorTextView;   // TextView for confirm password errors
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        submitButton = findViewById(R.id.submitButton);
        passwordRequirementsTextView = findViewById(R.id.passwordRequirementsTextView);
        newPasswordErrorTextView = findViewById(R.id.newPasswordErrorTextView);     // Initialize error TextView for new password
        confirmPasswordErrorTextView = findViewById(R.id.confirmPasswordErrorTextView); // Initialize error TextView for confirm password

        // Set password guidelines in TextView
        passwordRequirementsTextView.setText("Password must contain:\n" +
                "• At least 8 characters\n" +
                "• One uppercase letter\n" +
                "• One lowercase letter\n" +
                "• One digit\n" +
                "• One special character (@, #, $, etc.)");

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (!validateInputs(newPassword, confirmPassword)) {
                    // Validation failed, errors displayed in TextViews
                    return;
                }

                // Proceed with password update logic
                updatePassword(newPassword);
            }
        });
    }

    private boolean validateInputs(String newPassword, String confirmPassword) {
        // Reset error messages
        newPasswordErrorTextView.setText("");
        confirmPasswordErrorTextView.setText("");

        // Regex to check if password contains at least one uppercase letter, one lowercase letter, one digit, one special character
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

        boolean isValid = true;

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            if (newPassword.isEmpty()) {
                newPasswordErrorTextView.setText("Please fill in the new password.");
            }
            if (confirmPassword.isEmpty()) {
                confirmPasswordErrorTextView.setText("Please fill in the confirm password.");
            }
            isValid = false;
        }

        if (newPassword.length() < 8) {
            newPasswordErrorTextView.setText("Password must be at least 8 characters long.");
            isValid = false;
        }

        if (!newPassword.matches(passwordPattern)) {
            newPasswordErrorTextView.setText("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
            isValid = false;
        }

        if (!newPassword.equals(confirmPassword)) {
            confirmPasswordErrorTextView.setText("Passwords do not match.");
            isValid = false;
        }

        return isValid;
    }

    private void updatePassword(String newPassword) {
        // Prepare the JSON object to send to the server
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("new_password", newPassword);
            jsonObject.put("email", getIntent().getStringExtra("email")); // Pass the user's email
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // URL of your PHP script that handles the password reset
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/reset_password.php";

        // Create a JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            // Handle the response
                            if ("success".equals(status)) {
                                Intent intent = new Intent(NewPasswordActivity.this, LogIn.class);
                                startActivity(intent);
                                finish();
                            } else {
                                newPasswordErrorTextView.setText(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            newPasswordErrorTextView.setText("Error in response.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        newPasswordErrorTextView.setText("Error: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
