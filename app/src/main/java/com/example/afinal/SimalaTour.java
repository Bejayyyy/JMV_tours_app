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

public class SimalaTour extends AppCompatActivity {
    ImageView simalaBackButton;
    Button btnBookNowSimalatour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_simala_tour);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        simalaBackButton = findViewById(R.id.simalaBackButton);
        btnBookNowSimalatour = findViewById(R.id.btnBookNowSimalatour);
        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");

        simalaBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SimalaTour.this, Package.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        btnBookNowSimalatour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SimalaTour.this, Booking.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });
    }
}