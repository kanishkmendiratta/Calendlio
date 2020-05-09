package com.example.internshipassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences user_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);
        user_details=getSharedPreferences("user",MODE_PRIVATE);
        ConnectivityManager cm=(ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo an=cm.getActiveNetworkInfo();
        boolean isConnected=an !=null && an.isConnectedOrConnecting();
        if(isConnected) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (user_details.getBoolean("isLogged", false)) {
                        Intent home = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(home);
                        finish();
                    } else {
                        Intent login = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                }
            }, 3500);
        }
        else{
            Toast.makeText(this,"Check your Internet Conection",Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3500);
        }
    }
}

