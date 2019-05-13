package com.example.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Locale;

public class currentweather extends AppCompatActivity {
    Button btnSearch;
    Button btnGPS;
    TextView weatherData;
    EditText cityName;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentweather);
       cityName = (EditText) findViewById(R.id.EditTextCWCityName);
       weatherData=(TextView) findViewById(R.id.TextViewCWWeatherData);
       btnGPS = (Button) findViewById(R.id.BtnCWGps);
       btnSearch = (Button) findViewById(R.id.BtnCWSearch);
       btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               weatherData.setVisibility(View.VISIBLE);
               String name = (String)cityName.getText().toString();
                updateWeatherData(name.trim(),weatherData);
                if (ApiDataAccess.Error == null) {
                }
                else{
                    weatherData.setText(ApiDataAccess.Error);

                }

           }
       });

    }
    private void updateWeatherData(final String city, final TextView textView) {
        new Thread() {
            public void run() {
                final JSONObject json = ApiDataAccess.getJSON(city,false);
                if (json == null) {



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"JSON IS NULL",Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setVisibility(View.VISIBLE);
                            SetText(json,textView);
                        }
                    });


                }
            }
        }.start();
    }


        private void SetText(JSONObject json, TextView textView){
        try {

            textView.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));



        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }

    }

}
