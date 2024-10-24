    package com.example.afinal.APIService;

    import com.example.afinal.Constructor.BookingResponse;
    import com.example.afinal.Constructor.LoginRequest;
    import com.example.afinal.Constructor.PasswordChangeRequest;
    import com.example.afinal.Constructor.PendingResponse;
    import com.example.afinal.Constructor.User;
    import com.example.afinal.Constructor.UserProfileUpdateRequest;
    import com.example.afinal.Response.ApiResponse;
    import com.example.afinal.Response.LoginResponse;
    import com.example.afinal.Response.RegisterResponse;
    import com.example.afinal.Response.UserProfileResponse;
    import com.google.gson.JsonObject;
    import com.example.afinal.Response.changePasswordApiResponse;

    import java.util.List;
    import java.util.Map;

    import retrofit2.Call;
    import retrofit2.http.Body;
    import retrofit2.http.FieldMap;
    import retrofit2.http.FormUrlEncoded;
    import retrofit2.http.GET;
    import retrofit2.http.POST;
    import retrofit2.http.Query;

    public interface APIService {
        @POST("register.php")
        Call<RegisterResponse> registerUser(@Body User user);

        @POST("login.php")
        Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

        @GET("user_profile.php")
        Call<UserProfileResponse> getUserProfile (@Query("email") String email);

        @POST("updateProfile.php")
        Call<Void> updateUserProfile(@Body UserProfileUpdateRequest request);

        @GET("getCars.php")
        Call<List<String>> getCars();

        @GET("getPackages.php")
        Call<List<String>> getPackages();

        @FormUrlEncoded
        @POST("insertBooking.php")
        Call<Map<String, Object>> submitBooking(@FieldMap Map<String, String> bookingDetails);

        @GET("getUserBookings.php")
        Call<PendingResponse> getUserBookings(@Query("email") String email);

        @POST("updateBookingStatus.php")  // Ensure this is the correct path to your API
        Call<JsonObject> updateBookingStatus(
                @Body JsonObject bookingStatus
        );

        @GET("fetchCompletedBookings.php")
        Call<ApiResponse> fetchCompletedBookings(@Query("email") String email);

        @POST ("changePassword.php")
        Call<changePasswordApiResponse> changePassword(@Body PasswordChangeRequest request);

        Call<Void> createBooking(Map<String, String> bookingDetails);
    }
