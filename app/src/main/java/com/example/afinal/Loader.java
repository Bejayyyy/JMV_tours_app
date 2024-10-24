package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class Loader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        // Find the logo and apply fade-in animation
        ImageView loaderLogo = findViewById(R.id.loader_logo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        loaderLogo.startAnimation(fadeIn);

        // Find the ProgressBar and apply spin animation
        ProgressBar loadingIcon = findViewById(R.id.loader_icon);
        Animation spinAnimation = AnimationUtils.loadAnimation(this, R.anim.spin);
        loadingIcon.startAnimation(spinAnimation);

        // Simulate loading process and navigate to MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Loader.this, MainActivity.class);
                startActivity(intent);
                finish();  // Close the Loader activity
            }
        }, 3000);  // 3 seconds delay for the splash screen
    }
}
