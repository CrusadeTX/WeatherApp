package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class currentweather extends AppCompatActivity {
    Button btnSearch;
    Button btnGPS;
    TextView weatherData;
    EditText cityName;
    DataBaseDataAccess dataBaseDataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentweather);
        dataBaseDataAccess = new DataBaseDataAccess(this);
       cityName = (EditText) findViewById(R.id.EditTextCWCityName);
       weatherData=(TextView) findViewById(R.id.TextViewCWWeatherData);
       btnGPS = (Button) findViewById(R.id.BtnCWGps);
       btnSearch = (Button) findViewById(R.id.BtnCWSearch);
       btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               weatherData.setVisibility(View.VISIBLE);
               String name = cityName.getText().toString();
                updateWeatherData(name.trim(),weatherData);

               //btnGPS.setText(ApiDataAccess.Error);
               if(ApiDataAccess.Exception!=null)
               Toast.makeText(getApplicationContext(),ApiDataAccess.Exception,Toast.LENGTH_LONG).show();


           }
       });
       btnGPS.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //requestCoarseLocationPermission();
               requestFineLocationPermission();
               LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
               @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               String longitude = Double.toString(location.getLongitude());
               String latitude = Double.toString(location.getLatitude());
               updateWeatherDataWithCoordinates(longitude,latitude,weatherData);

           }
       });

    }
    private void updateWeatherData(final String city, final TextView textView) {
        new Thread() {
            public void run() {
                final JSONObject json = ApiDataAccess.getJSON(city,false,null,null);
                dataBaseDataAccess.addData(ApiDataAccess.BuildAPIQuery(false,city,null,null));
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
    private void updateWeatherDataWithCoordinates(final String longtitude,final String latitude, final TextView textView) {
        new Thread() {
            public void run() {
                final JSONObject json = ApiDataAccess.getJSON(null,false,latitude,longtitude);
                dataBaseDataAccess.addData(ApiDataAccess.BuildAPIQuery(false,null,longtitude,latitude));
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
            JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            JSONObject sys = json.getJSONObject("sys");
            JSONObject wind = json.getJSONObject("wind");


            textView.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    sys.getString("country") + " Sunrise: "+ sys.getString("sunrise") + " Sunset: "+
                    sys.getString("sunset") + " weather: "+ weather.getString("description")+", Current Temp "+ main.getString("temp") +" degrees, minTemp: "
                    + main.getString("temp_min")+" degrees, maxTemp: " + main.getString("temp_max") + "degrees, WindSpeed"+ wind.getString("speed"));



        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }

    }
    public void requestFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            return;

        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                123

        );
    }
    public void requestCoarseLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            return;

        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                456

        );
    }

}
