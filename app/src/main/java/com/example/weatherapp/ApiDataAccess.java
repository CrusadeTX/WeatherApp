package com.example.weatherapp;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.microedition.khronos.egl.EGLContext;
//api.openweathermap.org/data/2.5/forecast?lat=35&lon=139

public class ApiDataAccess {
    public static String Error = null;
    public static String Exception = null;
    //public static JSONObject jsono;
    private static final String API_KEY = "6fa92478ca54c09396ee514c752cec16";
    public static String BuildAPIQuery(Boolean isForecast, String cityName, String longtitude, String latitude)

    {
        String query = "http://api.openweathermap.org/data/2.5/";
        if (isForecast)
        {
            query+="forecast";
        }
        else
        {
            query+="weather";
        }
        if(longtitude.equals(null) || latitude.equals(null)) {
            query += "?q=" + cityName + "&APPID=" + API_KEY + "&units=metric";
        }
        else{
            query += "?lat=" + latitude+ "&lon=" +longtitude +"&APPID="+ API_KEY + "&units=metric";
        }
        return query;
    }

    public static JSONObject getJSON(String city, Boolean isForecast, String lattitude, String longtitude) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            if(lattitude.equals(null) || longtitude.equals(null)) {
                url = new URL(BuildAPIQuery(isForecast, city, null, null));
            }
            else{
                url = new URL(BuildAPIQuery(isForecast, null, longtitude, lattitude));
            }
            connection = (HttpURLConnection)url.openConnection();
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
            {
                is = connection.getErrorStream();
                Error="HTTP_ERROR";
            }
            else
                {
                is = connection.getInputStream();
                    Error = "HTTP_OK";
                }
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            JSONObject json = new JSONObject(response.toString());
          //  jsono=json;
            return json;
        } catch (Exception e) {
             Exception = e.getLocalizedMessage().toString();
             return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

}
