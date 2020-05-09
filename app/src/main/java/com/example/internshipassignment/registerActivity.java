package com.example.internshipassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
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

public class registerActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    ProgressBar register_progress;
    EditText rFirst,rLast,rEmail,rPhone,rAddress;
    Button registerUser;
    ConstraintLayout register_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rFirst=findViewById(R.id.rFirstName);
        rLast=findViewById(R.id.rLastName);
        rEmail=findViewById(R.id.remail);
        rPhone=findViewById(R.id.rphone);
        register_layout=findViewById(R.id.register_layout);
        register_progress=findViewById(R.id.registr_progress);
        rAddress=findViewById(R.id.rAddress);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://calendlio.sarayulabs.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);

        registerUser=findViewById(R.id.registerUser);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_progress.setVisibility(View.VISIBLE);
                register_layout.setVisibility(View.INVISIBLE);

                JsonObject details = new JsonObject();
                details.addProperty("first_name",rFirst.getText().toString());
                details.addProperty("last_name",rLast.getText().toString());
                details.addProperty("email",rEmail.getText().toString());
                details.addProperty("address",rAddress.getText().toString());
                details.addProperty("phone_number","+91"+rPhone.getText().toString());

                Call<JsonObject> call=apiInterface.registerUser(details);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, final Response<JsonObject> response) {
                        JsonObject phone=new JsonObject();
                        System.out.println(response.body());
                        phone.addProperty("phone_number",response.body().getAsJsonObject().get("phone_number").getAsString());

                        Call<JsonObject> call2=apiInterface.getOTP(phone);

                        call2.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call2, Response<JsonObject> response2) {
                                System.out.println(response2);
                                register_progress.setVisibility(View.INVISIBLE);
                                register_layout.setVisibility(View.VISIBLE);
                                Intent i=new Intent(registerActivity.this,confirmOtp.class);
                                i.putExtra("phoneNum","+91"+rPhone.getText().toString());
                                startActivity(i);
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call2, Throwable t) {
                                Toast.makeText(registerActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                                register_progress.setVisibility(View.INVISIBLE);
                                register_layout.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(registerActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                        register_progress.setVisibility(View.INVISIBLE);
                        register_layout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }
}
