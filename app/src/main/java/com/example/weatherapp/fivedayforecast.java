package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class fivedayforecast extends AppCompatActivity {
    Button btnSearchFD;
    Button btnGPSFD;
    TextView weatherDataFD;
    EditText cityNameFD;
    DataBaseDataAccess dataBaseDataAccess;
   // String query = ApiDataAccess.BuildAPIQuery(true,"Plovdiv");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fivedayforecast);
        requestFineLocationPermission();
        dataBaseDataAccess = new DataBaseDataAccess(this);
        cityNameFD = (EditText) findViewById(R.id.EditTextFDCityName);
        weatherDataFD=(TextView) findViewById(R.id.TextViewFDWeatherData);
        btnGPSFD = (Button) findViewById(R.id.BtnFDGps);
        btnSearchFD = (Button) findViewById(R.id.BtnFDSearch);
        btnSearchFD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cityNameFD.getText().toString().trim();
                if(name.length()>0) {
                    weatherDataFD.setVisibility(View.VISIBLE);
                    updateWeatherData(name, weatherDataFD);
                    //btnGPS.setText(ApiDataAccess.Error);
                    //  Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();
                    if (ApiDataAccess.Exception != null)
                        Toast.makeText(getApplicationContext(), ApiDataAccess.Exception, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter a city name!",Toast.LENGTH_LONG).show();
                }


            }
        });
        btnGPSFD.setOnClickListener(new View.OnClickListener() {
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
                    updateWeatherDataWithCoordinates(longitude, latitude, weatherDataFD);
                }
                else{
                    requestFineLocationPermission();
                }

            }
        });

    }
    private void updateWeatherData(final String city, final TextView textView) {
        new Thread() {
            public void run() {
                final JSONObject jsonfd = ApiDataAccess.getJSON(city,true,null,null);
                dataBaseDataAccess.addData(ApiDataAccess.BuildAPIQuery(true,city,null,null));
                if (jsonfd == null) {



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(),"JSON IS NULL",Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setVisibility(View.VISIBLE);
                            SetText(jsonfd,textView);

                        }
                    });


                }
            }
        }.start();
    }
    private void updateWeatherDataWithCoordinates(final String longtitude,final String latitude, final TextView textView) {
        new Thread() {
            public void run() {
                final JSONObject json = ApiDataAccess.getJSON(null,true,latitude,longtitude);
                dataBaseDataAccess.addData(ApiDataAccess.BuildAPIQuery(true,null,longtitude,latitude));
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
           JSONObject city = json.getJSONObject("city");
           String text = city.getString("name") + ", "+ city.getString("country")+"\n";
           //String text = json.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp");

           JSONArray list = json.getJSONArray("list");



            for (int i=0; i<list.length();i++)
                  {
                      JSONObject row = list.getJSONObject(i);
                      JSONObject main = row.getJSONObject("main");
                      JSONObject weather = row.getJSONArray("weather").getJSONObject(0);
                      JSONObject sys = row.getJSONObject("sys");
                      JSONObject wind = row.getJSONObject("wind");
                      String date = row.getString("dt_txt");
                    text+= "\nDate: "+ date +"\nweather: "+ weather.getString("description")+"\n Current Temp "+ main.getString("temp") +" degrees\n minTemp: "
                            + main.getString("temp_min")+" degrees \n maxTemp: " + main.getString("temp_max") + "degrees\n Wind speed"+ wind.getString("speed")+" \n";
            }
            textView.setText(text);
        }catch(Exception e){
            Log.e("fivedayforecast", "No such name exists");
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

}