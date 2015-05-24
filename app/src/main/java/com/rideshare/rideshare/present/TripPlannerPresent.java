package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import android.widget.Toast;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.manager.TripManager;
import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.entity.app.Geo;
import com.rideshare.rideshare.entity.app.RideStop;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.service.GeoCoderParser;
import com.rideshare.rideshare.view.fragment.TripPlannerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class TripPlannerPresent {

    private TripPlannerFragment parent;
    private GeoCoderParser geoParser;
    private Trip trip;
    private TripManager tripManager;
    private ArrayList<AsyncTask> tasks;

    public TripPlannerPresent(TripPlannerFragment parent, String userId) {
        this.parent = parent;
        this.geoParser = new GeoCoderParser();
        this.trip = new Trip(userId);
        this.tripManager = new TripManager();
        this.tasks = new ArrayList<>();
    }

    public void addStop(RideStop rideStop){
        new GeoFinderStop().execute(rideStop);
    }

    public void deleteStop(int position){
        trip.deleteStop(position);
    }

    private class GeoFinderStop extends AsyncTask<RideStop, Void, RideStop> {

        public GeoFinderStop(){
            tasks.add(this);
        }

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
            tasks.remove(this);
        }
    }

    private class GeoFinder extends AsyncTask<String, Void, String> {

        public GeoFinder(){
            tasks.add(this);
        }

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
                parent.clean("source");
                trip.setGeoSource(null);
            } else if(result != null) {
                parent.showError("Destination Address can't be found");
                parent.clean("destination");
                trip.setGeoDestination(null);
            }
            tasks.remove(this);
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
        new PostRide().execute();
    }

    public void postRequest() {
        new PostRequest().execute();
    }

    private class PostRide extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            AppResponse appResponse = new AppResponse(false);
            while(tasks.size() > 0){
                try { this.wait(500);}
                catch (InterruptedException ignored){}
            }
            String error = trip.validateRide();
            if(error != null){
                return error;
            }
            try {
                JSONObject rideJSON = trip.toJsonRide();
                tripManager.postRide(rideJSON, appResponse);
            } catch (JSONException e) {
                return "Unexpected Error";
            } catch (UnsupportedEncodingException e) {
                return "Unexpected Error";
            }
            if(!appResponse.isValid()){
                return "Unexpected Error";
            }
            if(appResponse.getStatus() != 200){
                try {
                    return appResponse.getJSON().getString("msg");
                } catch (JSONException e) {
                    return "Unexpected Error";
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null){
                Toast.makeText(parent.getActivity(), "Ride has been Added", Toast.LENGTH_SHORT)
                        .show();
                parent.toMyRides();
            } else {
                parent.showError(result);
            }
        }
    }

    private class PostRequest extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            AppResponse appResponse = new AppResponse(false);
            String error = trip.validateRequest();
            if(error != null){
                return error;
            }
            try {
                JSONObject requestJSON = trip.toJsonRequest();
                tripManager.postRequest(requestJSON, appResponse);
            } catch (JSONException e) {
                return "Unexpected Error";
            }
            if(!appResponse.isValid()){
                return "Unexpected Error";
            }
            if(appResponse.getStatus() != 200){
                try {
                    return appResponse.getJSON().getString("msg");
                } catch (JSONException e) {
                    return "Unexpected Error";
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null){
                Toast.makeText(parent.getActivity(), "Request has been Added", Toast.LENGTH_SHORT)
                        .show();
                parent.toMyRides();
            } else {
                parent.showError(result);
            }
        }
    }
}
