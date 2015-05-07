package com.rideshare.rideshare.service;

import com.rideshare.rideshare.entity.app.Geo;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

public class GeoCoderParser {

    private final static String URL_STRING = "http://geocoder.api.here.com/6.2/geocode.json";
    private final static String APP_ID = "8fNGZ0upEuk2LVVivpib";
    private final static String APP_CODE = "p0bM_QKAKYBR6vGohIB1_g";

    public GeoCoderParser(){}

    public Geo sendAddress(String address) {

        address = address.replaceAll(" ","+");
        String output;
        String full = "";
        try {
            String addressEncode = URLEncoder.encode(address, "UTF-8");
            URL url = new URL(URL_STRING + "?app_id=" + APP_ID + "&app_code=" + APP_CODE + "&searchtext=" + addressEncode);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                //throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                full += output;
            }
        } catch (IOException e) {
            //e.printStackTrace();
            //throw new Error("json parse");
            return null;
        }
        return findLatitudeAndLongitude(full);
    }

    private Geo findLatitudeAndLongitude(String fullResponse){
        double longitude, latitude;
        try {
            JSONObject json = new JSONObject(fullResponse);
            JSONArray jsonArr = json.getJSONObject("Response").getJSONArray("View");
            if(jsonArr.length() == 0){
                //throw new Error("empty first array");
                return null;
            }
            jsonArr = jsonArr.getJSONObject(0).getJSONArray("Result");
            if(jsonArr.length() == 0){
                //throw new Error("empty second array");
                return null;
            }
            json = jsonArr.getJSONObject(0).getJSONObject("Location").getJSONObject("DisplayPosition");
            latitude = json.getDouble("Latitude");
            longitude = json.getDouble("Longitude");
        } catch (JSONException e){
            //e.printStackTrace();
            return null;
        }
        return new Geo(longitude, latitude);
    }

}
