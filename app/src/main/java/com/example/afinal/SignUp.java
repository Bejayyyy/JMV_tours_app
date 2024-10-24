package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText regInputEmail, firstnameInput, lastnameInput, signUpUsernameInput;
    EditText regInputPassword;
    Button regRegBtn;
    TextView signUpAlreadyHaveAccountText, passwordWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        regInputEmail = findViewById(R.id.signUpEmailInput);
        regInputPassword = findViewById(R.id.signUpPasswordInput);
        regRegBtn = findViewById(R.id.signUpButton);
        signUpAlreadyHaveAccountText = findViewById(R.id.signUpAlreadyHaveAccountText);
        passwordWarning = findViewById(R.id.passwordWarning);
        lastnameInput = findViewById(R.id.lastnameInput);
        firstnameInput = findViewById(R.id.firstnameInput);
        signUpUsernameInput = findViewById(R.id.signUpUsernameInput);

        signUpAlreadyHaveAccountText.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, LogIn.class);
            startActivity(intent);
        });

        regRegBtn.setOnClickListener(view -> {
            String username = signUpUsernameInput.getText().toString().trim();
            String firstname = firstnameInput.getText().toString().trim();
            String lastname = lastnameInput.getText().toString().trim();
            String email = regInputEmail.getText().toString().trim();
            String password = regInputPassword.getText().toString();

            // Validate email format using regex
            if (!isValidEmail(email)) {
                passwordWarning.setText("Please enter a valid email address.");
                passwordWarning.setVisibility(View.VISIBLE);
                return;
            }

            // Check password length
            if (password.length() < 8) {
                passwordWarning.setText("Password must be at least 8 characters long.");
                passwordWarning.setVisibility(View.VISIBLE);
                return;
            }

            // Check if password meets the pattern
            if (!isValidPassword(password)) {
                passwordWarning.setText("Password must contain at least 1 Uppercase letter, 1 lowercase letter, and 1 special character.");
                passwordWarning.setVisibility(View.VISIBLE);
                return;
            }

            passwordWarning.setVisibility(View.GONE);  // Hide the warning when validation passes

            if (username.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                passwordWarning.setText("Please fill in all fields.");
                passwordWarning.setVisibility(View.VISIBLE);
                return;
            }

            // Send data to the server
            String registerUrl = "https://honeydew-albatross-910973.hostingersite.com/api/register.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, registerUrl,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if ("success".equals(status)) {
                                // Show success toast
                                Toast.makeText(SignUp.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, LogIn.class);
                                startActivity(intent);
                            } else {
                                passwordWarning.setText("Registration Failed: " + message);
                                passwordWarning.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            passwordWarning.setText("Registration Failed: " + e.getMessage());
                            passwordWarning.setVisibility(View.VISIBLE);
                        }
                    },
                    error -> {
                        Log.e("SignUp", "Registration Failed", error);
                        passwordWarning.setText("Error: " + error.getMessage());
                        passwordWarning.setVisibility(View.VISIBLE);
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_name", username);
                    params.put("first_name", firstnameInput.getText().toString().trim()); // Add this to capture first name
                    params.put("last_name", lastnameInput.getText().toString().trim()); // Add this to capture last name
                    params.put("user_email", email);
                    params.put("user_password", password);
                    return params;
                }
            };

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(stringRequest);
        });
    }

    // Function to validate password
    private boolean isValidPassword(String password) {
        // Regex to check 1 uppercase, 1 lowercase, 1 special character, and at least 8 characters
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }

    // Function to validate email format using regex
    private boolean isValidEmail(String email) {
        // General regex pattern to check if email is valid
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailPattern);
    }
}
