package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.afinal.Booking.Pending;

public class User extends AppCompatActivity {
    TextView tvPersonalInfo;
    TextView tvMyBookings;
    TextView tvChangePassword;
    ImageButton userbtnBack;
    ImageButton userhomebtn;
    ImageButton usercartbtn;
    TextView tvLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvPersonalInfo = findViewById(R.id.tvPersonalInfo);
        tvMyBookings = findViewById(R.id.tvMyBookings);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        userbtnBack = findViewById(R.id.btnBack);
        userhomebtn = findViewById(R.id.userhomebtn);
        tvLogout = findViewById(R.id.tvLogout);
        usercartbtn = findViewById(R.id.usercartbtn);

        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(User.this, MainActivity.class);
                intent1.putExtra("userEmail", email);
                startActivity(intent1);
            }
        });
        tvPersonalInfo.setOnClickListener(v -> {
            Intent intent1 = new Intent(User.this, userProfile.class);
            intent1.putExtra("userEmail", email);
            startActivity(intent1);
        });
        usercartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(User.this, Pending.class);
                intent1.putExtra("userEmail", email);
                startActivity(intent1);
            }
        });
        userhomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(User.this, Home.class);
                intent1.putExtra("userEmail", email);
                startActivity(intent1);
            }
        });

        userbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(User.this, Home.class);
                intent1.putExtra("userEmail", email);
                startActivity(intent1);
            }
        });
        tvMyBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(User.this, Pending.class);
                intent1.putExtra("userEmail", email);
                startActivity(intent1);
            }
        });
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(User.this, ChangePass.class);
                intent1.putExtra("userEmail", email);
                startActivity(intent1);
            }
        });
    }
}
