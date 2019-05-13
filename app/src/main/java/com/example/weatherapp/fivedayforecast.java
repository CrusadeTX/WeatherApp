package com.example.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class fivedayforecast extends AppCompatActivity {
    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fivedayforecast);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrentWeatherActivity();
            }
        });
    }
    public void openCurrentWeatherActivity(){
        Intent intent = new Intent(this,currentweather.class);
        startActivity(intent);
    }
}
