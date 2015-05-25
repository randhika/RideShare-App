package com.rideshare.rideshare.manager;

import com.rideshare.rideshare.entity.AppResponse;
import org.json.JSONObject;

public class TripManager extends Manager {

    private final static String RIDE_URI = "ride";
    private final static String REQUEST_URI = "request";
    private final static String MY_RIDES_URI = "trips";

    public TripManager(){
        super();
    }

    public void postRide(JSONObject json, AppResponse appResponse){
        String url = buildUrl(RIDE_URI);
        httpHandler.postJSON(json, url, appResponse);
    }

    public void postRequest(JSONObject json, AppResponse appResponse){
        String url = buildUrl(REQUEST_URI);
        httpHandler.postJSON(json, url, appResponse);
    }

    public void updateRequest(JSONObject json, AppResponse appResponse){
        String url = buildUrl(REQUEST_URI);
        httpHandler.putJSON(json, url, appResponse);
    }

    public void getMyRides(String userId, AppResponse appResponse){
        String url = buildUrl(MY_RIDES_URI) + "?user=" + userId;
        httpHandler.getJSON(url, appResponse);
    }

    private String buildUrl(String path){
        return super.buildUrl(APPLICATION_URL, path);
    }
}
