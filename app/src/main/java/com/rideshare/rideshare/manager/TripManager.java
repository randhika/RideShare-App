package com.rideshare.rideshare.manager;

import com.rideshare.rideshare.entity.AppResponse;
import org.json.JSONObject;

public class TripManager extends Manager {

    private final static String RIDE_URI = "ride";
    private final static String REQUEST_URI = "request";
    private final static String MY_RIDES_URI = "trips";
    private final static String GET_SUGGESTIONS = "request/suggestions";
    private final static String ADD_TO_WAITING_LIST = "ride/waiting";
    private final static String GET_RIDERS = "ride/riders";

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

    public void getSuggestions(String requestID, AppResponse appResponse){
        String url = buildUrl(GET_SUGGESTIONS) + "?request=" + "556946a61fcc8703003960f4";
        httpHandler.getJSON(url, appResponse);
    }

    public void getRiders(String rideID, AppResponse appResponse){
        String url = buildUrl(GET_RIDERS) + "?ride=" + rideID;
        httpHandler.getJSON(url, appResponse);
    }

    public void addToWaitingList(JSONObject json, AppResponse appResponse) {
        String url = buildUrl(ADD_TO_WAITING_LIST);
        httpHandler.postJSON(json, url, appResponse);
    }

    private String buildUrl(String path){
        return super.buildUrl(APPLICATION_URL, path);
    }
}
