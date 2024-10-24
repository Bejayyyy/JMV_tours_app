package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.afinal.Booking.Booking;
import com.example.afinal.Booking.Pending;

public class Home extends AppCompatActivity {
    ImageButton homeHomebtn;
    ImageButton homeProfileBtn;
    LinearLayout packagesbtn3;
    LinearLayout iterenarybtn3;
    LinearLayout destinationbtn3;
    ImageButton homecartbtn;
    ConstraintLayout packagesLayout;
    CardView package_card1;
    CardView package_card2;
    CardView package_card3;
    ConstraintLayout ItenaryLayout;
    Button btnBookNowCebutour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        homeHomebtn = findViewById(R.id.homeHomebtn);
        homeProfileBtn = findViewById(R.id.homeProfileBtn);
        packagesbtn3 = findViewById(R.id.packagesbtn3);
        iterenarybtn3 = findViewById(R.id.iterenarybtn3);
        destinationbtn3 = findViewById(R.id.destinationbtn3);
        homecartbtn = findViewById(R.id.homecartbtn);
        packagesLayout = findViewById(R.id.packagesLayout);
        package_card1 = findViewById(R.id.package_card1);
        package_card2 = findViewById(R.id.package_card2);
        package_card3 = findViewById(R.id.package_card3);
        ItenaryLayout = findViewById(R.id.ItenaryLayout);
        btnBookNowCebutour = findViewById(R.id.btnBookNowCebutour);


        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");

        btnBookNowCebutour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Booking.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });
        homeHomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Home.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        homeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, User.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        packagesbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Package.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        iterenarybtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Car.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        destinationbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Destinations.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        homecartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Pending.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });
        packagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Package.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        package_card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, PackageTour.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        package_card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, CebuTour.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        package_card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, OslobTour.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });

        ItenaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Car.class);
                intent.putExtra("userEmail",email);
                startActivity(intent);
            }
        });
    }
}