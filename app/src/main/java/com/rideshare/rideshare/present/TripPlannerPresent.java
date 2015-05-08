package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.entity.app.Geo;
import com.rideshare.rideshare.entity.app.RideStop;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.service.GeoCoderParser;
import com.rideshare.rideshare.view.fragment.TripPlannerFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class TripPlannerPresent {

    private TripPlannerFragment parent;
    private GeoCoderParser geoParser;
    private int mode;
    private Trip trip;

    public TripPlannerPresent(TripPlannerFragment parent, String userId) {
        this.parent = parent;
        this.geoParser = new GeoCoderParser();
        this.trip = new Trip(userId);
    }

    public void addStop(RideStop rideStop){
        new GeoFinderStop().execute(rideStop);
    }

    public void deleteStop(int position){
        trip.deleteStop(position);
    }

    public void setRideMode(){
        this.mode = 1;
    }

    public void setRequestMode(){
        this.mode = 0;
    }

    private class GeoFinderStop extends AsyncTask<RideStop, Void, RideStop> {

        @Override
        protected RideStop doInBackground(RideStop... params) {
            Geo geoInfo = geoParser.sendAddress(params[0].getAddress());
            trip.addStop(params[0]);
            if(geoInfo == null){
                return params[0];
            }
            params[0].setLatitude(geoInfo.getLatitude());
            params[0].setLongitude(geoInfo.getLongitude());
            return null;
        }

        @Override
        protected void onPostExecute(RideStop result) {
            int position = parent.getStopPosition(result);
            if(result != null){
                parent.onDeleteStop(position);
                parent.showError("Could not found address " + result.getAddress());
            }
        }
    }

    private class GeoFinder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Geo geoInfo = geoParser.sendAddress(params[0]);
            if(params[1].equals("source")){
                trip.setSource(params[0]);
            } else {
                trip.setDestination(params[0]);
            }
            if(geoInfo == null){
                return params[1];
            }
            if(params[1].equals("source")){
                trip.setGeoSource(new RideStop(null, null, null, geoInfo.getLatitude(),
                        geoInfo.getLongitude()));
            } else {
                trip.setGeoDestination(new RideStop(null, null, null, geoInfo.getLatitude(),
                        geoInfo.getLongitude()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null && result.equals("source")){
                parent.showError("Source Address can't be found");
            } else if(result != null) {
                parent.showError("Destination Address can't be found");
            }
        }
    }

    public void changedValue(String text, int id) {
        parent.showError("");
        switch (id){
            case R.id.source:
                new GeoFinder().execute(text, "source");
                break;
            case R.id.destination:
                new GeoFinder().execute(text, "destination");
                break;
            case R.id.price:
                trip.setPrice(Integer.parseInt(text));
                break;
            case R.id.time_from:
                trip.setTimeFrom(text);
                break;
            case R.id.time_until:
                trip.setTimeUntil(text);
                break;
            case R.id.date:
                trip.setDate(text);
                break;
            case R.id.smoker:
                trip.setSmoker(text);
                break;
            case R.id.bag:
                trip.setBag(text);
                break;
            case R.id.passengers:
                trip.setPassengers(Integer.parseInt(text.substring(0, 1)));
                break;
        }
    }

    public void postRide() {
        String error = trip.validateRide();
        if(error != null){
            parent.showError(error);
            return;
        }
        try {
            String rideStrJSON = trip.toJsonRide();
        } catch (JSONException e) {
            parent.showError("Unexpected Error");
            return;
        }
    }

}
