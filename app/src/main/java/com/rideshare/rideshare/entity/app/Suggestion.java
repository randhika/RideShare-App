package com.rideshare.rideshare.entity.app;

import com.rideshare.rideshare.utils.DateHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class Suggestion {

    private String driver;
    private String userID;
    private String requestID;
    private String rideID;
    private String driverRank;
    private String driverPhone;
    private String destination;
    private String source;
    private String date;
    private String timeFrom;
    private String timeUntil;
    private Integer price;
    private Integer seatsLeft;
    private String smoker;
    private boolean partOfRide;
    private String car;
    private String suggestionDate;

    public Suggestion(){}

    public static Suggestion fromJSON(JSONObject json) throws JSONException, ParseException {
        Suggestion suggestion = new Suggestion();

        JSONObject driver, ride;

        if(json.has("ride")){
            driver = json.getJSONObject("ride").getJSONObject("driver");
            ride = json.getJSONObject("ride");
        } else {
            ride = json;
            driver = ride.getJSONObject("driver");
        }


        suggestion.driver = driver.getString("fullName");
        suggestion.driverRank = driver.getJSONObject("rating").getString("driver");
        suggestion.driverPhone = driver.getString("phone");
        suggestion.destination = ride.getString("destination");
        suggestion.source = ride.getString("source");
        suggestion.date = ride.getString("date");
        suggestion.timeFrom = ride.getString("timeExitFrom");
        suggestion.timeUntil = ride.getString("timeExitUntil");
        suggestion.price = ride.getJSONObject("features").getInt("price");
        suggestion.seatsLeft = ride.getJSONObject("features").getInt("passengers");
        switch (ride.getJSONObject("features").getInt("smoker")){
            case(1): suggestion.smoker = "Not Smoker"; break;
            case(2): suggestion.smoker = "Don't Care"; break;
            case(3): suggestion.smoker = "Smoker"; break;
        }
        suggestion.partOfRide = ride.has("parentRide");
        String color = driver.getJSONObject("car").getString("color");
        String model = driver.getJSONObject("car").getString("model");
        String car;
        if (!model.equals("null") && !color.equals("null")) {
            car = color + " " + model;
        } else if(!model.equals("null")) {
            car = model;
        } else {
            car = "Not Specified";
        }
        suggestion.car = car;
        suggestion.suggestionDate = DateHandler.getDate(json.getString("created"));
        if(json.has("request"))
            suggestion.requestID = json.getString("request");
        if(json.has("user"))
            suggestion.userID = json.getString("user");
        suggestion.rideID = ride.getString("_id");
        return suggestion;
    }

    public String getSource() {
        return source;
    }

    public String getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public String getDestination() {
        return destination;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }

    public String getCar() {
        return car;
    }

    public String getDriver() {
        return driver;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public int getDriverRank() {
        return Integer.parseInt(driverRank);
    }

    public String getRequestID() {
        return requestID;
    }

    public String getRideID() {
        return rideID;
    }

    public String getSmoker() {
        return smoker;
    }

    public String getSuggestionDate() {
        return suggestionDate;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeUntil() {
        return timeUntil;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isPartOfRide(){
        return partOfRide;
    }
}