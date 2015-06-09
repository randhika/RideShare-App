package com.rideshare.rideshare.manager;

import com.rideshare.rideshare.entity.AppResponse;
import org.json.JSONObject;

public class TripManager extends Manager {

    private final static String RIDE_URI = "ride";
    private final static String REQUEST_URI = "request";
    private final static String MY_RIDES_URI = "trips";
    private final static String GET_SUGGESTIONS = "request/suggestions";
    private final static String WAITING_LIST = "ride/waiting";
    private final static String GET_RIDERS = "ride/riders";
    private final static String PASSENGER_LIST = "ride/approve";
    private final static String CONNECTED_RIDES = "request/rides";

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
        String url = buildUrl(WAITING_LIST);
        httpHandler.postJSON(json, url, appResponse);
    }

    public void addToPassengerList(JSONObject json, AppResponse appResponse) {
        String url = buildUrl(PASSENGER_LIST);
        httpHandler.postJSON(json, url, appResponse);
    }

    public void removeFromPassengerList(String rideID, String requestID, String userID,
                                        AppResponse appResponse) {
        String url = buildUrl(PASSENGER_LIST);
        url += "?user=" + userID;
        url += "&request=" + requestID;
        url += "&ride=" + rideID;
        httpHandler.deleteJSON(url, appResponse);
    }

    private String buildUrl(String path){
        return super.buildUrl(APPLICATION_URL, path);
    }

    public void removeFromWaitingList(String rideID, String requestID, String userID,
                                     AppResponse appResponse) {
        String url = buildUrl(WAITING_LIST);
        url += "?user=" + userID;
        url += "&request=" + requestID;
        url += "&ride=" + rideID;
        httpHandler.deleteJSON(url, appResponse);
    }

    public void getConnectedRides(String user, AppResponse appResponse) {
        String url = buildUrl(CONNECTED_RIDES) + "?user=" + user;
        httpHandler.getJSON(url, appResponse);
    }
}
