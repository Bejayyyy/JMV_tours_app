package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button submitButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        emailEditText = findViewById(R.id.emailEditText);
        submitButton = findViewById(R.id.submitButton);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        submitButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (!email.isEmpty()) {
                sendResetRequest(email); // Function to send password reset request
            } else {
                Toast.makeText(PasswordResetActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendResetRequest(String email) {
        // Prepare the JSON object to send to the server
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // URL of your PHP script that handles password reset
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/send_verification_code.php";

        // Create a JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Toast.makeText(PasswordResetActivity.this, message, Toast.LENGTH_SHORT).show();

                        if ("success".equals(status)) {
                            // Navigate to NewPasswordActivity directly
                            Intent intent = new Intent(PasswordResetActivity.this, VerifyCodeActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PasswordResetActivity.this, "Error in response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(PasswordResetActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

}
