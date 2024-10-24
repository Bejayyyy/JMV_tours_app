package com.example.afinal.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html; // Import Html for formatting
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.afinal.Booking.Booking;
import com.example.afinal.Constructor.Package;
import com.example.afinal.R;

import java.util.List;

public class PackageDetailsFragment extends Fragment {

    private static final String ARG_PACKAGE = "selected_package";
    private Package selectedPackage;

    public static PackageDetailsFragment newInstance(Package pkg,String email) {
        PackageDetailsFragment fragment = new PackageDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PACKAGE, pkg);  // Assuming Package implements Serializable
        args.putString("userEmail", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.package_details, container, false);

        // Retrieve the selected package from the arguments
        if (getArguments() != null) {
            selectedPackage = (Package) getArguments().getSerializable(ARG_PACKAGE);
        }

        // Find views and populate them with package details
        TextView titleTextView = view.findViewById(R.id.PackageTitle);
        TextView priceTextView = view.findViewById(R.id.PackagePrice);
        TextView descriptionTextView = view.findViewById(R.id.CTdescription);
        TextView itineraryTextView = view.findViewById(R.id.bullet_itinerary);
        TextView inclusionsTextView = view.findViewById(R.id.bullet_inclusions);
        ImageView mainImageView = view.findViewById(R.id.PackageMainImage);
        ImageView backButton = view.findViewById(R.id.cebuTourBackButton);
        Button bookNowButton = view.findViewById(R.id.PackageBookNowButton);
        ImageView sub_image1 = view.findViewById(R.id.sub_image1);
        ImageView sub_image2 = view.findViewById(R.id.sub_image2);
        ImageView sub_image3 = view.findViewById(R.id.sub_image3);

        // Set package details
        if (selectedPackage != null) {
            titleTextView.setText(selectedPackage.getTitle());
            priceTextView.setText(String.format("₱%.2f", selectedPackage.getPrice()));
            descriptionTextView.setText(selectedPackage.getDescription());

            // Set itineraries and inclusions with bullet points
            itineraryTextView.setText(Html.fromHtml(convertToBulletPoints(selectedPackage.getItineraries()), Html.FROM_HTML_MODE_LEGACY));
            inclusionsTextView.setText(Html.fromHtml(convertToBulletPoints(selectedPackage.getInclusions()), Html.FROM_HTML_MODE_LEGACY));

            // Load the main image using Glide
            Glide.with(this).load(selectedPackage.getMainImage()).into(mainImageView);

            // Load sub-images using Glide
            Glide.with(this).load(selectedPackage.getImage1()).into(sub_image1);
            Glide.with(this).load(selectedPackage.getImage2()).into(sub_image2);
            Glide.with(this).load(selectedPackage.getImage3()).into(sub_image3);
            Glide.with(this)
                    .load(selectedPackage.getImage2())
                    .error(R.drawable.pack2) // Set a placeholder in case of an error
                    .into(new CustomViewTarget<ImageView, Drawable>(sub_image2) {
                        @Override
                        protected void onResourceCleared(@Nullable Drawable placeholder) {
                            // Handle cleanup if necessary
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            sub_image2.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            Log.e("PackageDetailsFragment", "Failed to load image for sub_image2");
                            sub_image2.setImageDrawable(errorDrawable);
                        }
                    });
        }

        // Set up click listener for back button
        // Set up click listener for book now button
        bookNowButton.setOnClickListener(v -> {
            // Create an Intent to start the Booking activity
            Intent bookingIntent = new Intent(getActivity(), Booking.class);

            // Retrieve the email from the arguments of PackageDetailsFragment
            String userEmail = getArguments() != null ? getArguments().getString("userEmail") : null;

            // Pass userEmail and selectedPackage to the Booking activity
            bookingIntent.putExtra("userEmail", userEmail);
            bookingIntent.putExtra("selectedPackage", selectedPackage); // Pass package if needed
            bookingIntent.putExtra("packageTitle", selectedPackage.getTitle()); // Pass package title

            // Start the Booking activity
            startActivity(bookingIntent);
        });



        return view;
    }

    // Helper method to convert list to bullet points in HTML
    private String convertToBulletPoints(List<String> items) {
        StringBuilder bulletString = new StringBuilder("<ul>"); // Start unordered list
        for (String item : items) {
            bulletString.append("<li> ").append(item).append("</li> "); // Append bullet and item
        }
        bulletString.append("</ul>"); // End unordered list
        return bulletString.toString(); // Return as HTML string
    }


}
