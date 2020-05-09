package com.example.internshipassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class confirmOtp extends AppCompatActivity {

    Bundle bundle=new Bundle();
    EditText otp;
    Button confirm;
    String userPhone;
    ApiInterface apiInterface;
    ProgressBar progressBar;
    ConstraintLayout layout;
    SharedPreferences user_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);
        otp=findViewById(R.id.otp);
        confirm=findViewById(R.id.confirmOtp);
        progressBar=findViewById(R.id.confirmOtp_progress);
        layout=findViewById(R.id.otpLayout);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://calendlio.sarayulabs.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);

        bundle=getIntent().getExtras();
        userPhone=bundle.getString("phoneNum");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                layout.setVisibility(View.INVISIBLE);
                JsonObject otpJson=new JsonObject();
                otpJson.addProperty("otp",otp.getText().toString());
                otpJson.addProperty("phone_number",userPhone);
                Call<JsonObject> call=apiInterface.confirmOTP(otpJson);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if(response.isSuccessful()){
                            user_detail=getSharedPreferences("user",MODE_PRIVATE);
                            SharedPreferences.Editor ed=user_detail.edit();
                            ed.putBoolean("isLogged",true);
                            ed.putString("firstName",response.body().get("first_name").getAsString());
                            ed.putString("lastName",response.body().get("last_name").getAsString());
                            ed.putString("email",response.body().get("email").getAsString());
                            ed.putString("id",response.body().get("id").getAsString());
                            ed.putString("Auth",response.body().get("auth_token").getAsString());
                            ed.putString("Phone",response.body().get("phone_number").getAsString());
                            ed.apply();
                            progressBar.setVisibility(View.INVISIBLE);
                            layout.setVisibility(View.VISIBLE);
                            Intent intent=new Intent(confirmOtp.this,HomeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                        else{
                            progressBar.setVisibility(View.INVISIBLE);
                            layout.setVisibility(View.VISIBLE);
                            Toast.makeText(confirmOtp.this,"Some Error Occured. Try Again later.",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        layout.setVisibility(View.VISIBLE);
                        Toast.makeText(confirmOtp.this,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
