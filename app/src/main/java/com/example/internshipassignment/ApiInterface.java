package com.example.internshipassignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.JsonObject;

import java.util.prefs.Preferences;
import android.content.SharedPreferences;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {



    @POST("api/auth/register")
    Call<JsonObject> registerUser(@Body JsonObject userDetails);

    @POST("/api/verification/phone")
    Call<JsonObject> getOTP(@Body JsonObject phoneNum);

    @POST("/api/auth/login")
    Call<JsonObject> confirmOTP(@Body JsonObject otp);

    @POST("/api/bookings")
    Call<JsonObject> createMeet(@Header ("Authorization") String Auth,@Body JsonObject meet_details);

    @GET("/api/bookings")
    Call<JsonObject> getMeetings(@Header ("Authorization") String Auth);

    @PATCH()
    Call<JsonObject> update(@Header ("Authorization") String Auth, @Url String url,@Body JsonObject newDescrip);

    @DELETE()
    Call<JsonObject> delete(@Header ("Authorization") String Auth, @Url String url);
}

