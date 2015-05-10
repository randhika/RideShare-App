package com.rideshare.rideshare.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArrivalTime {

    private final String URL_STRING = "https://maps.googleapis.com/maps/api/directions/json";
    private final String API_KEY = "AIzaSyBMCcedH6pb5elMBT-_jV2FYJg3rbfOcB8";

    public ArrivalTime(){}

    public int getExpectedTimeToArrive(String source, String dest) {

        source = source.replaceAll(" ", "+");
        dest = dest.replaceAll(" ","+");

        String output;
        String full = "";

        try {
            String sourceEncode = URLEncoder.encode(source, "UTF-8");
            String destEncode = URLEncoder.encode(dest, "UTF-8");
            URL url = new URL(URL_STRING + "?origin=" + sourceEncode + "&destination=" + destEncode + "&sensor=false" );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                return -1;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                full += output;
            }
        } catch (IOException e) {
            //e.printStackTrace();
            //throw new Error("json parse");
            return -1;
        }
        return executeTimeFromResponseMessage(full);
    }

    private int executeTimeFromResponseMessage(String fullResponse) {
        try {
            JSONObject json = new JSONObject(fullResponse);
            JSONArray jsonArr = json.getJSONArray("routes");
            if(jsonArr.length() == 0){
                throw new Error("empty first array");
            }
            jsonArr = jsonArr.getJSONObject(0).getJSONArray("legs");
            if(jsonArr.length() == 0){
                throw new Error("empty second array");
            }
            json = jsonArr.getJSONObject(0).getJSONObject("duration");
            int time = json.getInt("value")/60;
            return time;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return -1;
    }
}
