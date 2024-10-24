package com.example.afinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.APIService.APIService;
import com.example.afinal.Booking.Pending;
import com.example.afinal.Constructor.UserProfileUpdateRequest;
import com.example.afinal.Response.UserProfileResponse;
import com.example.afinal.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;

public class EditProfile extends AppCompatActivity {
    ImageButton editProfileBackBtn;
    TextView doneBtn;
    EditText  editProfileAddress, editProfileContactNumber,editLastName,editFirstName;
    private String email;
    ImageButton userhomebtn, usercartbtn;
    ImageView profileImageView;
    Bitmap selectedImage; // To hold the selected image bitmap
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        doneBtn = findViewById(R.id.doneButton);
        editLastName = findViewById(R.id.editLastName);
        editFirstName = findViewById(R.id.editFirstName);
        editProfileAddress = findViewById(R.id.editProfileAddress);
        editProfileContactNumber = findViewById(R.id.editProfileContactNumber);
        editProfileBackBtn = findViewById(R.id.editProfileBackBtn);
        userhomebtn = findViewById(R.id.userhomebtn);
        usercartbtn = findViewById(R.id.usercartbtn);
        profileImageView = findViewById(R.id.editImgProfile); // ImageView for displaying the profile image

        // Retrieve the email passed from the previous activity
        Intent intent = getIntent();
        email = intent.getStringExtra("userEmail");

        // Handle image selection
        profileImageView.setOnClickListener(view -> chooseImage());

        usercartbtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(EditProfile.this, Pending.class);
            intent1.putExtra("userEmail", email);
            startActivity(intent1);
        });

        editProfileBackBtn.setOnClickListener(view -> {
            Intent intent12 = new Intent(EditProfile.this, userProfile.class);
            intent12.putExtra("userEmail", email);
            startActivity(intent12);
        });

        // Fetch the user profile if email is available
        if (email != null) {
            fetchUserProfile(email);
        } else {
            Toast.makeText(this, "Error: Email not found", Toast.LENGTH_SHORT).show();
        }

        // Set up listener for the done button
        doneBtn.setOnClickListener(view -> {
            updateUserProfile(email);  // Pass the email to updateUserProfile method
            if (selectedImage != null) {
                uploadImage();
            }
            Intent intent12 = new Intent(EditProfile.this, userProfile.class);
            intent12.putExtra("userEmail", email);
            startActivity(intent12);

        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                profileImageView.setImageBitmap(selectedImage);  // Display selected image in ImageView
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchUserProfile(String email) {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/user_profile.php?email=" + email; // Replace with your server URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Assuming the response is in JSON format
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                // Extract user profile data from JSON
                                String userFirstName = jsonResponse.getString("first_name") ;
                                String userLastName = jsonResponse.getString("last_name");
                                String userProfileAddress = jsonResponse.getString("user_address");
                                String userProfileContactNumber = jsonResponse.getString("user_contactNumber");

                                // Populate EditTexts with fetched user profile data
                                editFirstName.setText(userFirstName);
                                editLastName.setText(userLastName);
                                editProfileAddress.setText(userProfileAddress);
                                editProfileContactNumber.setText(userProfileContactNumber);
                            } else {
                                Toast.makeText(EditProfile.this, "Profile not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProfile.this, "Error parsing profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfile.this, "Error fetching profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email); // Pass the email to the server
                return params;
            }
        };

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void updateUserProfile(String email) {
        String Firstname = editFirstName.getText().toString().trim();
        String LastName = editLastName.getText().toString().trim();
        String address = editProfileAddress.getText().toString();
        String contactNumber = editProfileContactNumber.getText().toString();

        String url = "https://honeydew-albatross-910973.hostingersite.com/api/update_user_profile.php"; // Ensure this URL is correct

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response here
                        Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error here
                        Toast.makeText(EditProfile.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("first_name", Firstname); // Ensure this matches your PHP expectation
                params.put("last_name", LastName); // Assuming last name is same as name for example
                params.put("user_address", address);
                params.put("user_contactNumber", contactNumber);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void uploadImage() {
        String url = "https://honeydew-albatross-910973.hostingersite.com/api/updateProfile.php"; // Replace with your server URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditProfile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfile.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                if (selectedImage != null) {
                    String imageData = imageToString(selectedImage);
                    params.put("profile_image", imageData);
                }
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
