package com.rideshare.rideshare.entity.app;

import com.rideshare.rideshare.utils.DateHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String timeFrom;
    private String timeUntil;
    private ArrayList<RideStop> stops;
    private String user;

    public Trip(String userId){
        this.user = userId;
        this.stops = new ArrayList<>();
    }

    public void setBag(String bag) {
        if(bag.equals("None/Small Bag"))
            this.smoker = 1;
        else if(bag.equals("Big Bag"))
            this.smoker = 2;
    }

    public void setDate(String date) {
        this.date = date;
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
        if(price == null && price < 0){
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
            return "Enter a Number of passengers";
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

    public JSONObject toJsonRide() throws JSONException {
        JSONObject ride = new JSONObject();
        ride.put("source", source);
        ride.put("destination", destination);
        ride.put("time", DateHandler.timeDiff(timeUntil, timeFrom) / 60);
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
}
