package com.rideshare.rideshare.app;

import com.rideshare.rideshare.entity.AppResponse;
import org.json.JSONObject;

public class TripManager extends Manager {

    private final static String RIDE_URI = "ride";

    public TripManager(){
        super();
    }

    public void postRide(JSONObject json, AppResponse appResponse){
        String url = buildUrl(RIDE_URI);
        httpHandler.postJSON(json, url, appResponse);
    }

    private String buildUrl(String path){
        return super.buildUrl(APPLICATION_URL, path);
    }
}