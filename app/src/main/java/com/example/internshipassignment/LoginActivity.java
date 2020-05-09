package com.example.internshipassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText lPhone;
    Button reqOtp;
    TextView register;
    ApiInterface apiInterface;
    ProgressBar login_progress;
    ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register=findViewById(R.id.register);
        lPhone=findViewById(R.id.lPhone);
        reqOtp=findViewById(R.id.reqOtp);
        login_progress=findViewById(R.id.login_progress);
        layout=findViewById(R.id.login_layout);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://calendlio.sarayulabs.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
        reqOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_progress.setVisibility(View.VISIBLE);
                layout.setVisibility(View.INVISIBLE);
                JsonObject phone=new JsonObject();
                phone.addProperty("phone_number","+91"+lPhone.getText().toString());

                Call<JsonObject> call=apiInterface.getOTP(phone);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        login_progress.setVisibility(View.INVISIBLE);
                        layout.setVisibility(View.VISIBLE);
                        System.out.println(response);
                        Intent i=new Intent(LoginActivity.this,confirmOtp.class);
                        i.putExtra("phoneNum","+91"+lPhone.getText().toString());
                        i.putExtra("con",getApplication().toString());
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        login_progress.setVisibility(View.INVISIBLE);
                        layout.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,registerActivity.class));
            }
        });
    }
}
