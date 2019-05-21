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
        requestFineLocationPermission();
       cityName = (EditText) findViewById(R.id.EditTextCWCityName);
       weatherData=(TextView) findViewById(R.id.TextViewCWWeatherData);
       btnGPS = (Button) findViewById(R.id.BtnCWGps);
       btnSearch = (Button) findViewById(R.id.BtnCWSearch);
       btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String name = cityName.getText().toString().trim();
               if (name.length()>0) {
                   weatherData.setVisibility(View.VISIBLE);

                   updateWeatherData(name, weatherData);

                   //btnGPS.setText(ApiDataAccess.Error);
                   if (ApiDataAccess.Exception != null)
                       Toast.makeText(getApplicationContext(), ApiDataAccess.Exception, Toast.LENGTH_LONG).show();
               }
               else{
                   Toast.makeText(getApplicationContext(),"Please enter a city name!",Toast.LENGTH_LONG).show();
               }


           }
       });
       btnGPS.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //requestCoarseLocationPermission();
               if (ContextCompat.checkSelfPermission(getApplicationContext(),
                       Manifest.permission.ACCESS_FINE_LOCATION) ==
                       PackageManager.PERMISSION_GRANTED) {
                   LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                   Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                   String longitude = Double.toString(location.getLongitude());
                   String latitude = Double.toString(location.getLatitude());
                   updateWeatherDataWithCoordinates(longitude, latitude, weatherData);
               }
               else
                   {
                       requestFineLocationPermission();
                   }

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
               boolean result = dataBaseDataAccess.addData(ApiDataAccess.BuildAPIQuery(false,null,longtitude,latitude));
               if(!result){
                   Toast.makeText(getApplicationContext(),"Error adding data to the database",Toast.LENGTH_LONG);
               }
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
                    sys.getString("country") + "\n Sunrise: "+ sys.getString("sunrise") + "\n Sunset: "+
                    sys.getString("sunset") + "\n weather: "+ weather.getString("description")+"\n Current Temp "+ main.getString("temp") +" degrees\n minTemp: "
                    + main.getString("temp_min")+" degrees\n maxTemp: " + main.getString("temp_max") + "degrees\n Wind speed"+ wind.getString("speed"));



        }catch(Exception e){
            Log.e("currentweather", "No such name exists");
            Toast.makeText(getApplicationContext(),"City name doesnt exist",Toast.LENGTH_LONG).show();
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
