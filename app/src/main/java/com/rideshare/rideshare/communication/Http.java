package com.rideshare.rideshare.communication;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

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

    public JsonResponse postJSON(String body, String url){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        HttpResponse response = null;
        try{
            StringEntity bodyEntity =new StringEntity(body);
            request.addHeader("content-type", "application/json");
            request.setEntity(bodyEntity);
            response = httpClient.execute(request);
        } catch (Exception e) {
            response = null;
        } finally {
            httpClient.getConnectionManager().shutdown();

        }
        if(response == null)
            return null;
        try{
            return new JsonResponse(response);
        } catch (Exception e){
            return null;
        }
    }
}
