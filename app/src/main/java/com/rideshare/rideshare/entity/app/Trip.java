package com.rideshare.rideshare.entity.app;

import com.rideshare.rideshare.utils.DateHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Trip {

    private String source;
    private String destination;
    private RideStop geoSource;
    private RideStop geoDestination;
    private Integer price;
    private Integer smoker;
    private Integer bag;
    private Integer passengers;
    private String date;
    private String type;
    private int status;
    private String timeFrom;
    private String timeUntil;
    private ArrayList<RideStop> stops;
    private String user;

    public Trip(String userId){
        this.user = userId;
        this.stops = new ArrayList<>();
    }

    public Trip(){
        this.stops = new ArrayList<>();
    }

    public void setBag(String bag) {
        if(bag.equals("None/Small Bag"))
            this.bag = 1;
        else if(bag.equals("Big Bag"))
            this.bag = 2;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSmoker(Integer smoker) {
        this.smoker = smoker;
    }

    public void setBag(Integer bag) {
        this.bag = bag;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setGeoDestination(RideStop geoDestination) {
        this.geoDestination = geoDestination;
    }

    public void setGeoSource(RideStop geoSource) {
        this.geoSource = geoSource;
    }

    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setSmoker(String smoker) {
        if(smoker.equals("Smoker"))
            this.smoker = 3;
        else if(smoker.equals("Not Smoker"))
            this.smoker = 1;
        else if(smoker.equals("Don't Care"))
            this.smoker = 2;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public int getPassengers() {
        return passengers.intValue();
    }

    public int getPrice() {
        return price.intValue();
    }

    public String getDate() {
        return date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeUntil() {
        return timeUntil;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public void addStop(RideStop stop) {
        this.stops.add(stop);
    }

    public void deleteStop(int position) {
        this.stops.remove(position);
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public void setTimeUntil(String timeUntil) {
        this.timeUntil = timeUntil;
    }

    public String validateRide(){
        if(source == null){
            return "Enter a Source Address";
        }
        if(destination == null){
            return "Enter a Destination Address";
        }
        if(geoSource == null){
            return "Source Address could not be found";
        }
        if(geoDestination == null){
            return "Destination Address could not be found";
        }
        if(price == null || price < 0){
            return "Enter a Valid Price";
        }
        if(passengers == null){
            return "Enter a Number of passengers";
        }
        if(smoker == null){
            return "Enter a Smoker Information";
        }
        if(date == null){
            return "Enter the Date of the Ride";
        }
        if(timeFrom == null && timeUntil == null){
            return "Enter the Time of the Ride";
        }
        if(DateHandler.timeDiff(timeUntil, timeFrom) < 0){
            return "Invalid Time difference";
        }
        return null;
    }

    public String validateRequest(){
        if(source == null){
            return "Enter a Source Address";
        }
        if(destination == null){
            return "Enter a Destination Address";
        }
        if(geoSource == null){
            return "Source Address could not be found";
        }
        if(geoDestination == null){
            return "Destination Address could not be found";
        }
        if(bag == null){
            return "Enter Your Bags Option";
        }
        if(smoker == null){
            return "Enter a Smoker Information";
        }
        if(date == null){
            return "Enter the Date of the Request";
        }
        if(timeFrom == null && timeUntil == null){
            return "Enter the Time of the Request";
        }
        if(DateHandler.timeDiff(timeUntil, timeFrom) < 0){
            return "Invalid Time difference";
        }
        return null;
    }

    public JSONObject toJsonRide() throws JSONException, UnsupportedEncodingException {
        JSONObject ride = new JSONObject();
        ride.put("source", source);
        ride.put("destination", destination);
        ride.put("date", date);
        ride.put("timeFrom", timeFrom);
        ride.put("timeUntil", timeUntil);
        JSONObject features = new JSONObject();
        features.put("price", price.intValue());
        features.put("passengers", passengers.intValue());
        features.put("smoker", smoker.intValue());
        ride.put("features", features);
        JSONArray stopsArrayJSON = new JSONArray();
        for(int i = 0; i < stops.size(); i++){
            RideStop stop = stops.get(i);
            JSONObject stopJSON = new JSONObject();
            stopJSON.put("address", stop.getAddress());
            stopJSON.put("price", stop.getPrice());
            stopJSON.put("time", stop.getTime());
            stopJSON.put("latitude", stop.getLatitude());
            stopJSON.put("longitude", stop.getLongitude());
            stopsArrayJSON.put(i, stopJSON);
        }
        ride.put("stops", stopsArrayJSON);
        JSONObject sourceGeo = new JSONObject();
        sourceGeo.put("latitude", geoSource.getLatitude());
        sourceGeo.put("longitude", geoSource.getLongitude());
        JSONObject destinationGeo = new JSONObject();
        destinationGeo.put("latitude", geoDestination.getLatitude());
        destinationGeo.put("longitude", geoDestination.getLongitude());
        ride.put("sourceGeo", sourceGeo);
        ride.put("destinationGeo", destinationGeo);
        ride.put("driver", user);
        return ride;
    }

    public JSONObject toJsonRequest() throws JSONException {
        JSONObject request = new JSONObject();
        request.put("source", source);
        request.put("destination", destination);
        request.put("date", date);
        request.put("timeExitFrom", timeFrom);
        request.put("timeExitUntil", timeUntil);
        JSONObject features = new JSONObject();
        features.put("bags", bag.intValue());
        features.put("smoker", smoker.intValue());
        request.put("features", features);
        JSONObject sourceGeo = new JSONObject();
        sourceGeo.put("latitude", geoSource.getLatitude());
        sourceGeo.put("longitude", geoSource.getLongitude());
        JSONObject destinationGeo = new JSONObject();
        destinationGeo.put("latitude", geoDestination.getLatitude());
        destinationGeo.put("longitude", geoDestination.getLongitude());
        request.put("sourceGeo", sourceGeo);
        request.put("destinationGeo", destinationGeo);
        request.put("user", user);
        return request;
    }

    public static Trip fromJSON(JSONObject trip) throws JSONException {
        Trip t = new Trip();
        t.setType(trip.getString("type"));
        t.setSource(trip.getString("source"));
        t.setDestination(trip.getString("destination"));
        t.setDate(trip.getString("date"));
        t.setTimeFrom(trip.getString("timeExitFrom"));
        t.setTimeUntil(trip.getString("timeExitUntil"));
        t.setStatus(trip.getInt("status"));
        JSONObject features = trip.getJSONObject("features");
        if (t.getType().equals("request")) {
            t.setSmoker(features.getInt("smoker"));
        } else {
            t.setPassengers(features.getInt("passengers"));
            t.setPrice(features.getInt("price"));
        }
        return t;
    }

    public String getSmoker(){
        switch (smoker) {
            case 1:
                return "Not Smoker";
            case 2:
                return "Don't Care";
            case 3:
                return "Smoker";
        }
        return "";
    }

    public String getBag(){
        switch (bag) {
            case 1:
                return "None/Small Bag";
            case 2:
                return "Big Bag";
        }
        return "";
    }
}