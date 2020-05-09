package com.example.internshipassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewBooking extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    CalendarView calendar;
    int c;
    String date="";
    EditText descripET;
    ApiInterface apiInterface;
    Button chooseStart,chooseEnd,createMeet;
    String startTime="",endTime="";
    SharedPreferences user;
    ProgressBar progressBar;
    ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);
        calendar=findViewById(R.id.calendarView);
        chooseStart=findViewById(R.id.chooseStart);
        chooseEnd=findViewById(R.id.chooseEnd);
        progressBar=findViewById(R.id.create_progress);
        layout=findViewById(R.id.create_layout);
        user=getSharedPreferences("user",MODE_PRIVATE);
        createMeet=findViewById(R.id.create);
        descripET=findViewById(R.id.descripET);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://calendlio.sarayulabs.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);

        createMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startTime.equals("") || endTime.equals("")){
                    Toast.makeText(NewBooking.this,"Start and End time cannot be empty",Toast.LENGTH_LONG).show();
                }
                else{
                    if(Integer.parseInt(startTime.substring(0,2))>Integer.parseInt(endTime.substring(0,2))){
                        Toast.makeText(NewBooking.this,"Start time Cannot be after End time",Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(Integer.parseInt(startTime.substring(0,2))==Integer.parseInt(endTime.substring(0,2)) && Integer.parseInt(startTime.substring(3,5))>Integer.parseInt(endTime.substring(3,5))){
                            Toast.makeText(NewBooking.this,"Start time Cannot be after End time",Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(!descripET.getText().toString().isEmpty()){
                                progressBar.setVisibility(View.VISIBLE);
                                layout.setVisibility(View.INVISIBLE);
                                if(date.equals("")){
                                    Calendar currentDate=Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                                    date=simpleDateFormat.format(currentDate.getTime());
                                }
                                JsonObject meet_details=new JsonObject();
                                meet_details.addProperty("description",descripET.getText().toString());
                                meet_details.addProperty("start_datetime",date+"T"+startTime+":00.000000Z");
                                meet_details.addProperty("end_datetime",date+"T"+endTime+":00.000000Z");

                                System.out.println();
                                Call<JsonObject> call=apiInterface.createMeet("Token "+user.getString("Auth",""),meet_details);

                                call.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        if(response.isSuccessful()){
                                            System.out.println(response.body());
                                            progressBar.setVisibility(View.INVISIBLE);
                                            layout.setVisibility(View.VISIBLE);
                                            Toast.makeText(NewBooking.this,"Meeting Created successfully",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(NewBooking.this,HomeActivity.class));
                                            finishAffinity();
                                        }
                                        else{
                                            progressBar.setVisibility(View.INVISIBLE);
                                            layout.setVisibility(View.VISIBLE);
                                            Toast.makeText(NewBooking.this,"Some Error Occured. Try Again later.",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        layout.setVisibility(View.VISIBLE);
                                        Toast.makeText(NewBooking.this,t.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(NewBooking.this,"Description cannot be empty",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });
         calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
             @Override
             public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                 month++;
                 date=year+"-"+month+"-"+dayOfMonth;
                 Toast.makeText(NewBooking.this,date,Toast.LENGTH_LONG).show();
             }
         });
         chooseStart.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 c=0;
                 DialogFragment timePicker = new TimePickerFragment();
                 timePicker.show(getSupportFragmentManager(), "Start Time");
             }
         });
         chooseEnd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 c=1;
                 DialogFragment timePicker = new TimePickerFragment();
                 timePicker.show(getSupportFragmentManager(), "End Time");
             }
         });

    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(c==0) {
            TextView textView = (TextView) findViewById(R.id.sTime);
            Button btn=findViewById(R.id.chooseStart);
            btn.setText("Update Start Time");
            if(hourOfDay>9 && minute>9) {
                startTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            }
            else if(hourOfDay>9 && minute<10){
                startTime = String.valueOf(hourOfDay) + ":" +"0"+ String.valueOf(minute);
            }
            else if(hourOfDay<10 && minute>9){
                startTime = "0"+String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            }
            else if(hourOfDay<10 && minute<10){
                startTime = "0"+String.valueOf(hourOfDay) + ":"+"0" + String.valueOf(minute);
            }
            textView.setText(startTime);
        }
        else{
            TextView textView = (TextView) findViewById(R.id.eTime);
            Button btn=findViewById(R.id.chooseEnd);
            btn.setText("Update End Time");
            if(hourOfDay>9 && minute>9) {
                endTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            }
            else if(hourOfDay>9 && minute<10){
                endTime = String.valueOf(hourOfDay) + ":" +"0"+ String.valueOf(minute);
            }
            else if(hourOfDay<10 && minute>9){
                endTime = "0"+String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            }
            else if(hourOfDay<10 && minute<10){
                endTime = "0"+String.valueOf(hourOfDay) + ":"+"0" + String.valueOf(minute);
            }
            textView.setText(endTime);
        }
    }
}
