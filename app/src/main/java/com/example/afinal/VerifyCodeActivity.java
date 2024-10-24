package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
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

public class VerifyCodeActivity extends AppCompatActivity {

    private EditText verificationCodeEditText;
    private Button verifyButton;
    private RequestQueue requestQueue;
    private String email; // Email passed from PasswordResetActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        verificationCodeEditText = findViewById(R.id.verificationCodeEditText);
        verifyButton = findViewById(R.id.verifyButton);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Get the email passed from PasswordResetActivity
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        verifyButton.setOnClickListener(v -> {
            String verificationCode = verificationCodeEditText.getText().toString().trim();
            if (!verificationCode.isEmpty()) {
                verifyCode(verificationCode); // Function to verify the entered code
            } else {
                Toast.makeText(VerifyCodeActivity.this, "Please enter the verification code.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyCode(String verificationCode) {
        // Prepare the JSON object to send to the server
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email); // Pass the email
            jsonObject.put("verification_code", verificationCode); // Pass the verification code
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // URL of your PHP script that verifies the code
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/verify_code.php";

        // Create a JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Toast.makeText(VerifyCodeActivity.this, message, Toast.LENGTH_SHORT).show();

                        if ("success".equals(status)) {
                            // Navigate to NewPasswordActivity
                            Intent intent = new Intent(VerifyCodeActivity.this, NewPasswordActivity.class);
                            intent.putExtra("email", email); // Pass the email to NewPasswordActivity
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(VerifyCodeActivity.this, "Error in response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(VerifyCodeActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
