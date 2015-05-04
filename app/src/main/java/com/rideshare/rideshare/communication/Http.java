package com.rideshare.rideshare.communication;

import android.util.Log;

import com.rideshare.rideshare.entity.AppResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Http {

    private static boolean initialize = false;
    private static Http http;

    private Http(){
        initialize = true;
    }

    public static Http init(){
        if(!initialize) {
            initialize = true;
            http = new Http();
        }
        return http;
    }

    public void getJSON(String url, AppResponse result){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;

        try{
            request.addHeader("content-type", "application/json");
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            response = null;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        if(response == null)
            return;
        try{
            String responseString = EntityUtils.toString(response.getEntity());
            int status = response.getStatusLine().getStatusCode();
            JSONObject jsonObject = new JSONObject(responseString);
            result.init(status, jsonObject);
        } catch (Exception e){
            Log.e("AppResponse", "JSONException", e);
        }
    }

    public void postJSON(JSONObject json, String url, AppResponse result){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        HttpResponse response = null;

        try{
            request.addHeader("content-type", "application/json");
            StringEntity bodyEntity = new StringEntity(json.toString());
            request.setEntity(bodyEntity);
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            response = null;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        if(response == null)
            return;
        try{
            String responseString = EntityUtils.toString(response.getEntity());
            int status = response.getStatusLine().getStatusCode();
            JSONObject jsonObject = new JSONObject(responseString);
            result.init(status, jsonObject);
        } catch (JSONException e) {
            Log.e("AppResponse", "JSONException", e);
        } catch (IOException e) {
            Log.e("AppResponse", "IOException", e);
        }
    }
}
