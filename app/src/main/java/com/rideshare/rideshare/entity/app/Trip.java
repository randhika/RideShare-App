package com.rideshare.rideshare.entity.app;

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
}
