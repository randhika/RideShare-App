package com.rideshare.rideshare.entity.app;

public class Geo {

    private double latitude;
    private double longitude;

    public Geo(){
        latitude = -1;
        longitude = -1;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
