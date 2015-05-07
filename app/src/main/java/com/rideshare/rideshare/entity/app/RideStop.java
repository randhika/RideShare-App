package com.rideshare.rideshare.entity.app;

public class RideStop {

    private String address;
    private String price;
    private String time;
    private String latitude;
    private String longitude;

    public RideStop(String address, String price, String time, double latitude, double longitude){
        this.address = address;
        this.price = price;
        this.time = time;
        this.latitude = "" + latitude;
        this.longitude = "" + longitude;
    }

    public RideStop(String address, String price, String time){
        this.address = address;
        this.price = price;
        this.time = time;
    }


    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = "" + latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = "" + longitude;
    }
}

