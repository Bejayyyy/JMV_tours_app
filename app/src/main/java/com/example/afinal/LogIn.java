package com.example.afinal;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.afinal.APIService.APIService;
import com.example.afinal.Constructor.LoginRequest;
import com.example.afinal.Response.LoginResponse;
import com.example.afinal.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {
    Button signInBtn;
    EditText mainInputEmail;
    EditText mainInputPassword;
    TextView logIndontHaveAccountText;
    TextView forgotPassword;
    ProgressBar progressBar;  // ProgressBar declaration
    ValueAnimator animator; // Animator declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        // Handling system window insets (edges)
        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        signInBtn = findViewById(R.id.logsignInButton);
        mainInputEmail = findViewById(R.id.loginEmailInput);
        mainInputPassword = findViewById(R.id.loginPasswordInput);
        logIndontHaveAccountText = findViewById(R.id.logIndontHaveAccountText);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressBar);  // Initialize ProgressBar

        // Set click listener for forgot password
        forgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LogIn.this, PasswordResetActivity.class);
            startActivity(intent);
        });

        // Set click listener for 'Don't have an account' text
        logIndontHaveAccountText.setOnClickListener(view -> {
            Intent intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
        });

        // Handle sign-in button click
        signInBtn.setOnClickListener(view -> {
            String email = mainInputEmail.getText().toString().trim();
            String password = mainInputPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LogIn.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show ProgressBar and disable button when login starts
            progressBar.setVisibility(View.VISIBLE); // Show ProgressBar
            signInBtn.setEnabled(false);
            startAirplaneAnimation(); // Start the airplane animation

            // Create a LoginRequest object
            LoginRequest loginRequest = new LoginRequest(email, password);

            // Call the API for user login
            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<LoginResponse> call = apiService.loginUser(loginRequest);

            // Make the API call
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    // Hide ProgressBar and enable button once the response is received
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar
                    signInBtn.setEnabled(true);
                    stopAirplaneAnimation(); // Stop the animation

                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null && "success".equals(loginResponse.getStatus())) {
                            // Navigate to Home on successful login
                            Intent intent = new Intent(LogIn.this, Home.class);
                            intent.putExtra("userEmail", email);
                            startActivity(intent);
                            finish();
                        } else {
                            // Show error message from API
                            Toast.makeText(LogIn.this, "Login Failed: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle API response failure (non-successful response)
                        Toast.makeText(LogIn.this, "Login Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    // Hide ProgressBar and enable button on failure
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar
                    signInBtn.setEnabled(true);
                    stopAirplaneAnimation(); // Stop the animation

                    // Log the error and display a Toast message
                    Log.e("Login", "Login Failed", throwable);
                    Toast.makeText(LogIn.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void startAirplaneAnimation() {
        // Define the animator for the airplane icon moving to the right and up-and-down
        animator = ValueAnimator.ofFloat(0f, 100f); // Change 100f to desired max X position (horizontal movement)
        animator.setDuration(800); // Duration of the animation
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART); // Restart mode for continuous running
        animator.setInterpolator(new LinearInterpolator()); // Smooth movement
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            float verticalValue = (float) (Math.sin(value / 20) * 10); // Vertical sine wave movement

            progressBar.setTranslationX(value); // Update the X position of the ProgressBar
            progressBar.setTranslationY(verticalValue); // Update the Y position of the ProgressBar
        });
        animator.start(); // Start the animation
    }

    private void stopAirplaneAnimation() {
        if (animator != null) {
            animator.cancel(); // Stop the animation
            progressBar.setTranslationX(0); // Reset position
            progressBar.setTranslationY(0); // Reset vertical position
        }
    }
}
