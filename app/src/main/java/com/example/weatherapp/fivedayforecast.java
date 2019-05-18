package com.example.weatherapp;

import android.content.Intent;
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
        dataBaseDataAccess = new DataBaseDataAccess(this);
        cityNameFD = (EditText) findViewById(R.id.EditTextFDCityName);
        weatherDataFD=(TextView) findViewById(R.id.TextViewFDWeatherData);
        btnGPSFD = (Button) findViewById(R.id.BtnFDGps);
        btnSearchFD = (Button) findViewById(R.id.BtnFDSearch);
        btnSearchFD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDataFD.setVisibility(View.VISIBLE);
                String name = cityNameFD.getText().toString();
                updateWeatherData(name.trim(),weatherDataFD);
                //btnGPS.setText(ApiDataAccess.Error);
              //  Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();
                if(ApiDataAccess.Exception!=null)
                Toast.makeText(getApplicationContext(),ApiDataAccess.Exception,Toast.LENGTH_LONG).show();


            }
        });

    }
    private void updateWeatherData(final String city, final TextView textView) {
        new Thread() {
            public void run() {
                final JSONObject jsonfd = ApiDataAccess.getJSON(city,true);
                dataBaseDataAccess.addData(ApiDataAccess.BuildAPIQuery(true,city));
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
                            + main.getString("temp_min")+" degrees \n maxTemp: " + main.getString("temp_max") + "degrees\n WindSpeed"+ wind.getString("speed")+" \n";
            }
            textView.setText(text);
        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }

    }

}