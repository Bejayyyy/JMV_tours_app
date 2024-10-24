package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.afinal.Booking.Booking;

public class SafariTour extends AppCompatActivity {
    ImageView safariBackButton;
    Button btnBookNowCebuSafaritour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_safari_tour);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBookNowCebuSafaritour = findViewById(R.id.btnBookNowCebuSafaritour);
        safariBackButton = findViewById(R.id.safariBackButton);



        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");

        safariBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SafariTour.this, Package.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });
        btnBookNowCebuSafaritour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SafariTour.this, Booking.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });
    }
}