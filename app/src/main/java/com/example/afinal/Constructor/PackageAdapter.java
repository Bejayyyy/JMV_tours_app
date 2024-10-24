package com.example.afinal.Constructor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.afinal.Fragment.PackageDetailsFragment;
import com.example.afinal.R;
import com.example.afinal.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {

    private Context context;
    private List<Package> packageList; // Change to List<Package>
    private String userEmail;

    // Updated constructor to accept a list of packages
    public PackageAdapter(Context context, List<Package> packageList,String email) {
        this.context = context;
        this.packageList = packageList;
        this.userEmail = email; // Assign the email
        fetchPackages(); // Fetch packages on initialization
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_package, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        Package pkg = packageList.get(position);
        holder.packageTitle.setText(pkg.getTitle());
        holder.packagePrice.setText(String.format("₱%.2f", pkg.getPrice()));
        holder.packageDescription.setText(pkg.getDescription());

        // Load the main image using Glide
        Glide.with(context).load(pkg.getMainImage()).into(holder.packageImage);

        // Set an OnClickListener on the itemView
        holder.itemView.setOnClickListener(v -> {
            // Create an instance of the PackageDetailsFragment
            PackageDetailsFragment detailsFragment = PackageDetailsFragment.newInstance(pkg, userEmail);

            // Use the FragmentManager to navigate to the details fragment
            ((AppCompatActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment) // Ensure this ID matches your layout's container ID
                    .addToBackStack(null) // Optional: allows back navigation
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    private void fetchPackages() {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/fetch_package.php"; // Your API endpoint

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray packagesArray = response.getJSONArray("packages");
                            for (int i = 0; i < packagesArray.length(); i++) {
                                JSONObject packageObject = packagesArray.getJSONObject(i);
                                int packageId = packageObject.getInt("package_id");
                                String title = packageObject.getString("title");
                                double price = packageObject.getDouble("price");
                                String description = packageObject.getString("description");
                                String mainImage = packageObject.getString("main_image");
                                String image1 = packageObject.optString("image1", null);
                                String image2 = packageObject.optString("image2", null);
                                String image3 = packageObject.optString("image3", null);

                                // Get itinerary and inclusions as comma-separated strings
                                String itineraryString = packageObject.optString("itinerary", "");
                                String inclusionsString = packageObject.optString("inclusions", "");

                                // Convert the strings into ArrayLists
                                ArrayList<String> itineraryList = new ArrayList<>(Arrays.asList(itineraryString.split(",")));
                                ArrayList<String> inclusionsList = new ArrayList<>(Arrays.asList(inclusionsString.split(",")));

                                // Create a new Package object and add it to the list
                                Package pkg = new Package(packageId, title, price, description, mainImage,  image1, image2, image3,itineraryList, inclusionsList);
                                packageList.add(pkg);
                            }
                            notifyDataSetChanged(); // Notify the adapter that data has changed
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue (using VolleySingleton)
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder {
        CardView packageCard;
        ImageView packageImage;
        TextView packageTitle, packagePrice, packageDescription;
        ConstraintLayout package_card;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            packageCard = itemView.findViewById(R.id.package_card5);
            packageImage = itemView.findViewById(R.id.package_image5);
            packageTitle = itemView.findViewById(R.id.package_title5);
            packagePrice = itemView.findViewById(R.id.package_price5);
            packageDescription = itemView.findViewById(R.id.package_description5);
            package_card = itemView.findViewById(R.id.package_card);
        }
    }


}
