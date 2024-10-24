package com.example.afinal.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.afinal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
// Inside BookingBottomSheetFragment.java

public class BookingBottomSheetFragment extends BottomSheetDialogFragment {

    private Spinner packageTypeSpinner, carServiceSpinner;
    private EditText BookingPhoneNumberInput, BookingAddressInput, BookingDepartureDateInput, etName;
    private TextView BookingEmailInput;
    private Button bookNowButton;
    private ImageButton btnBack;
    private String userEmail; // Variable to hold the user's email
    private LinearLayout mainDetails;

    // Create a new instance of the fragment with userEmail
    public static BookingBottomSheetFragment newInstance(String email) {
        BookingBottomSheetFragment fragment = new BookingBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("USER_EMAIL", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_booking, container, false);

        // Retrieve the email from the arguments
        if (getArguments() != null) {
            userEmail = getArguments().getString("USER_EMAIL");
        }

        // Initialize views
        etName = view.findViewById(R.id.etName);
        BookingPhoneNumberInput = view.findViewById(R.id.BookingPhoneNumberInput);
        BookingAddressInput = view.findViewById(R.id.BookingAddressInput);
        BookingEmailInput = view.findViewById(R.id.BookingEmailInput);
        packageTypeSpinner = view.findViewById(R.id.packageTypeSpinner);
        BookingDepartureDateInput = view.findViewById(R.id.BookingDepartureDateInput);
        bookNowButton = view.findViewById(R.id.bookNowButton);
        btnBack = view.findViewById(R.id.btnBack); // Ensure btnBack is initialized

        // Set up button click listeners
        bookNowButton.setOnClickListener(v -> {
            // Handle confirm booking action
            dismiss(); // Close the bottom sheet
        });

        btnBack.setOnClickListener(v -> {
            // Handle cancel action
            dismiss(); // Close the bottom sheet
        });

        return view;
    }
}
