package com.rideshare.rideshare.entity.app;

import org.json.JSONException;
import org.json.JSONObject;

public class Rider {

    public Rider(){}

    private String id;
    private String request;
    private String fullName;
    private String source;
    private int bags;
    private String destination;
    private String rideID;
    private int type;
    private int rating;
    private String phone;

    public static Rider fromJSON(JSONObject json, int type) throws JSONException {
        Rider rider = new Rider();
        JSONObject user = json.getJSONObject("user");
        JSONObject request = json.getJSONObject("request");
        JSONObject features = request.getJSONObject("features");
        rider.request = request.getString("_id");
        rider.bags = features.getInt("bags");
        rider.id = user.getString("_id");
        rider.fullName = user.getString("fullName");
        rider.type = type;
        JSONObject rating = user.getJSONObject("rating");
        rider.rating = rating.getInt("driver");
        rider.phone = user.getString("phone");
        return rider;
    }

    public int getRating() {
        return rating;
    }

    public int getType() {
        return type;
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getRequest() {
        return request;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    public String getRideID() {
        return rideID;
    }

    public int getBags() {
        return bags;
    }
}
