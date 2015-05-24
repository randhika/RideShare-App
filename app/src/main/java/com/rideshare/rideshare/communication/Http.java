package com.rideshare.rideshare.communication;

import android.util.Log;
import com.rideshare.rideshare.entity.AppResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    /*public void getJSON(String url, AppResponse result){
        try {
            URL u = new URL(url);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.connect();
            InputStream in = c.getInputStream();

            byte[] buffer = new byte[1048576];
            StringBuilder builder = new StringBuilder();
            while ((in.read(buffer)) > 0) {
                builder.append(new String(buffer));
            }
            in.close();
            String s = builder.toString();
            JSONObject jsonObject = new JSONObject(s);
            result.init(200, jsonObject);
        } catch (IOException | JSONException e) {
            Log.e("getJSON", "IOException | JSONException", e);
        }
    }*/

    public void getJSON(String url, AppResponse result){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response;

        try{
            response = httpClient.execute(request);
        } catch (IOException e) {
            Log.e("postJSON", "IOException", e);
            response = null;
        }

        if(response == null){
            httpClient.getConnectionManager().shutdown();
            return;
        }

        try{
            int status = response.getStatusLine().getStatusCode();
            String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            JSONObject jsonObject = new JSONObject(responseString);
            result.init(status, jsonObject);
        } catch (JSONException e) {
            Log.e("postJSON", "JSONException", e);
        } catch (IOException e) {
            Log.e("postJSON", "IOException2", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public void postJSON(JSONObject json, String url, AppResponse result){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        HttpResponse response;

        try{
            request.addHeader("content-type", "application/json");
            StringEntity bodyEntity = new StringEntity(json.toString(), HTTP.UTF_8);
            request.setEntity(bodyEntity);
            response = httpClient.execute(request);
        } catch (IOException e) {
            Log.e("postJSON", "IOException", e);
            response = null;
        }

        if(response == null){
            httpClient.getConnectionManager().shutdown();
            return;
        }

        try{
            int status = response.getStatusLine().getStatusCode();
            String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            JSONObject jsonObject = new JSONObject(responseString);
            result.init(status, jsonObject);
        } catch (JSONException e) {
            Log.e("postJSON", "JSONException", e);
        } catch (IOException e) {
            Log.e("postJSON", "IOException2", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}
