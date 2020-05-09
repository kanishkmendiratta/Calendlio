package com.example.internshipassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements DialogBox.ExampleDialogListener{

    SharedPreferences user;
    ProgressBar list_progress;
    ArrayList<bookingListModel> bookList=new ArrayList<>();
    Button newBook;
    ApiInterface apiInterface;
    ListView booking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user=getSharedPreferences("user",MODE_PRIVATE);
        newBook=findViewById(R.id.newBooking);
        booking=findViewById(R.id.bookingList);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://calendlio.sarayulabs.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
        list_progress=findViewById(R.id.list_progress);
        newBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,NewBooking.class));
            }
        });

        Call<JsonObject> call=apiInterface.getMeetings("Token "+user.getString("Auth",""));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray listOfBookings=response.body().get("results").getAsJsonArray();
                for(int i=0;i<Integer.parseInt(response.body().get("count").getAsString());i++){
                    JsonObject newBooking=listOfBookings.get(i).getAsJsonObject();
                    bookingListModel b2=new bookingListModel(newBooking.get("id").getAsString(),newBooking.get("start_datetime").getAsString(),newBooking.get("end_datetime").getAsString(),newBooking.get("created_at").getAsString(),newBooking.get("modified_at").getAsString(),newBooking.get("description").getAsString());
                    bookList.add(b2);
                }
                booking.setVisibility(View.VISIBLE);
                list_progress.setVisibility(View.INVISIBLE);
                BookingListAdapter bookingListAdapter=new BookingListAdapter(HomeActivity.this,R.layout.booking_list_item_view,bookList);
                booking.setAdapter(bookingListAdapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                list_progress.setVisibility(View.INVISIBLE);
                Toast.makeText(HomeActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void applyTexts(String newDescrip,String id) {
        user=getSharedPreferences("user",MODE_PRIVATE);
        if(!newDescrip.isEmpty()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://calendlio.sarayulabs.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiInterface = retrofit.create(ApiInterface.class);

            JsonObject descrip=new JsonObject();
            descrip.addProperty("description",newDescrip);

            Call<JsonObject> call=apiInterface.update("Token "+user.getString("Auth",""),"/api/bookings/"+id,descrip);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
    }
}
