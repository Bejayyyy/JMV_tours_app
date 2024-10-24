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
import com.example.afinal.Fragment.BookingBottomSheetFragment; // Import your BottomSheetFragment

public class CebuTour extends AppCompatActivity {
    ImageView cebuTourBackButton;
    Button btnBookNowCebutour;
    String packageName = "Cebu Tour ₱6000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cebu_tour);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String email = intent.getStringExtra("userEmail");

        btnBookNowCebutour = findViewById(R.id.btnBookNowCebutour);
        cebuTourBackButton = findViewById(R.id.cebuTourBackButton);

        cebuTourBackButton.setOnClickListener(view -> {
            Intent intentBack = new Intent(CebuTour.this, Package.class);
            intentBack.putExtra("userEmail", email);
            startActivity(intentBack);
        });

        btnBookNowCebutour.setOnClickListener(view -> {
            // Create an instance of the BookingBottomSheetFragment
            BookingBottomSheetFragment bookingBottomSheetFragment = new BookingBottomSheetFragment();

            // Show the BottomSheetFragment
            bookingBottomSheetFragment.show(getSupportFragmentManager(), bookingBottomSheetFragment.getTag());
        });
    }
}
