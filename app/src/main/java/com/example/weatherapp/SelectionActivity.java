package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {
    Button CurrentWeather,Forecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        CurrentWeather=findViewById(R.id.currentWeather);
        Forecast=findViewById(R.id.forecast);

        CurrentWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if((cm.getActiveNetworkInfo() != null)) {

                    Intent currentIntent = new Intent(SelectionActivity.this, MainActivity.class);
                    startActivity(currentIntent);
                }
                    else {
                        Toast.makeText(SelectionActivity.this,"Please Provide Internet Connection",Toast.LENGTH_LONG).show();
                    }
            }
        });
        Forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if ((cm.getActiveNetworkInfo() != null)) {

                    Intent forecastIntent = new Intent(SelectionActivity.this, FiveDayWeather.class);
                    startActivity(forecastIntent);
                }
                else {
                    Toast.makeText(SelectionActivity.this,"Please Provide Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
