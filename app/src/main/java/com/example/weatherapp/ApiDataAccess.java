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

public class ApiDataAccess {
    public static String Error = null;
    public static JSONObject jsono;
    private static final String API_KEY = "6fa92478ca54c09396ee514c752cec16";
    public static String BuildAPIQuery(Boolean isForecast, String cityName)

    {
        String query = "api.openweathermap.org/data/2.5/";
        if (isForecast)
        {
            query+="forecast";
        }
        else
        {
            query+="weather";
        }

        query+="?q="+cityName+"&APPID="+API_KEY;
        return query;
    }

    public static JSONObject getJSON(String city, Boolean isForecast) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(BuildAPIQuery(false,city));
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
            jsono=json;
            return json;
        } catch (Exception e) {
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}