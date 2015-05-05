package com.rideshare.rideshare.service;

import com.rideshare.rideshare.entity.app.Geo;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

public class geoCoderParser {

    private String urlString = "http://geocoder.api.here.com/6.2/geocode.json";
    private String appID = "8fNGZ0upEuk2LVVivpib";
    private String appCode = "p0bM_QKAKYBR6vGohIB1_g";

    public geoCoderParser(){}

    public Geo sendAddress(String address) {

        address = address.replaceAll(" ","+");
        String output = "";
        String full = "";
        try {
            URL url = new URL( urlString + "?app_id=" + appID + "&app_code=" + appCode + "&searchtext=" + address );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                full += output;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return findLatitudeAndLongitude(full);
    }

    private Geo findLatitudeAndLongitude(String fullResponse){
        try {
            JSONObject json = new JSONObject(fullResponse);
            JSONArray jsonArr = json.getJSONObject("Response").getJSONArray("View");
            if(jsonArr.length() == 0){
                throw new Error("empty first array");
            }
            jsonArr = jsonArr.getJSONObject(0).getJSONArray("Result");
            if(jsonArr.length() == 0){
                throw new Error("empty second array");
            }
            json = jsonArr.getJSONObject(0).getJSONObject("Location").getJSONObject("DisplayPosition");
            Geo geo = new Geo();
            geo.setLatitude(json.getDouble("Latitude"));
            geo.setLongitude(json.getDouble("Longitude"));
            return geo;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}
