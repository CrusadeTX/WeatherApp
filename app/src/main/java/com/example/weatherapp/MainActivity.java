package com.example.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnCurrentWeather;
    Button btnFiveDayForecast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCurrentWeather = findViewById(R.id.BtnCurrentWeather);
        btnFiveDayForecast = findViewById(R.id.BtnFiveDayForecast);
        btnCurrentWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrentWeatherActivity();
            }
        });
        btnFiveDayForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiveDayForecastActivity();
            }
        });
    }
    public void openCurrentWeatherActivity(){
        Intent intent = new Intent(this,currentweather.class);
        startActivity(intent);
    }
    public void openFiveDayForecastActivity(){
        Intent intent = new Intent(this,fivedayforecast.class);
        startActivity(intent);
    }
}
