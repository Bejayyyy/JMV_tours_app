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

public class OslobTour extends AppCompatActivity {
    ImageView oslobBackButton;
    Button btnBookNowOslobtour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_oslob_tour);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");

        btnBookNowOslobtour = findViewById(R.id.btnBookNowOslobtour);
        oslobBackButton = findViewById(R.id.oslobBackButton);

        oslobBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OslobTour.this, Package.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });
        btnBookNowOslobtour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OslobTour.this, Booking.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });
    }
}