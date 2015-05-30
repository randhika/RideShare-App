package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import android.util.Log;
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
import java.util.List;

public class TripPlannerPresent {

    private TripPlannerFragment parent;
    private GeoCoderParser geoParser;
    private Trip trip;
    private TripManager tripManager;
    private ArrayList<GeoFinderStop> geoTasks;
    private AddRequest requestTask;
    private AddRide rideTask;
    private AddTrip tripTask;

    public TripPlannerPresent(TripPlannerFragment parent, String userId) {
        this.parent = parent;
        this.geoParser = new GeoCoderParser();
        this.trip = new Trip(userId);
        this.tripManager = new TripManager();
        this.geoTasks = new ArrayList<>();
    }

    public void addStop(RideStop rideStop){
        GeoFinderStop task = new GeoFinderStop();
        geoTasks.add(task);
        task.execute(rideStop);
    }

    public void deleteStop(int position){
        trip.deleteStop(position);
    }

    public void postRide() {
        if(parent.getSource().equals("") || parent.getDestination().equals("")){
            parent.error("Enter Source and Destination Addresses");
        } else if(rideTask == null && requestTask == null && tripTask == null){
            rideTask = new AddRide();
            if(geoTasks.size() == 0)
                rideTask.execute(parent.getSource(), parent.getDestination());
        }
    }

    public void postRequest() {
        if(parent.getSource().equals("") || parent.getDestination().equals("")){
            parent.error("Enter Source and Destination Addresses");
        } else if(rideTask == null && requestTask == null && tripTask == null){
            requestTask = new AddRequest();
            if(geoTasks.size() == 0)
                requestTask.execute(parent.getSource(), parent.getDestination());
        }
    }

    public void stopTasks() {
    }

    private class GeoFinderStop extends AsyncTask<RideStop, Void, RideStop> {

        private boolean cancel = false;

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
            if(cancel) {
                finish(true);
                return;
            }
            int position = parent.getStopPosition(result);
            if(result != null){
                parent.onDeleteStop(position);
                parent.error("Could not found address " + result.getAddress());
                finish(true);
            }
            geoTasks.remove(this);
            finish(false);
        }

        public void finish(boolean error){
            if(error){
                requestTask = null;
                rideTask = null;
            }
            else if(geoTasks.size() == 0 && requestTask != null)
                requestTask.execute(parent.getSource(), parent.getDestination());
            else if(geoTasks.size() == 0 && rideTask != null)
                rideTask.execute(parent.getSource(), parent.getDestination());
        }

        public void cancel(){
            this.cancel = true;
        }
    }

    private abstract class PreAddTrip extends AsyncTask<String, Void, List<Geo>> {

        private boolean cancel = false;

        @Override
        protected List<Geo> doInBackground(String... params) {
            Geo sourceInfo = geoParser.sendAddress(params[0]);
            Geo destinationInfo = geoParser.sendAddress(params[1]);

            ArrayList<Geo> geos = new ArrayList<>();
            geos.add(sourceInfo);
            geos.add(destinationInfo);
            return geos;
        }

        @Override
        protected void onPostExecute(List<Geo> geos) {
            if(cancel){
                finish();
                return;
            }
            if(geos.get(0) == null){
                parent.clean("source");
                parent.error("Source Address can not be Found");
                finish();
            } else {
                trip.setSource(parent.getSource());
                trip.setGeoSource(new RideStop(null, null, null, geos.get(0).getLatitude(),
                        geos.get(0).getLongitude()));
            }

            if(geos.get(0) == null){
                parent.clean("destination");
                parent.error("Destination Address can not be Found");
                finish();
            } else {
                trip.setDestination(parent.getDestination());
                trip.setGeoDestination(new RideStop(null, null, null, geos.get(1).getLatitude(),
                        geos.get(1).getLongitude()));
            }

            trip.setDate(parent.getDate());
            trip.setTimeUntil(parent.getTimeUntil());
            trip.setTimeFrom(parent.getTimeFrom());
            switch (parent.getSmoker()){
                case(1): trip.setSmoker(3);
                case(2): trip.setSmoker(1);
                case(3): trip.setSmoker(2);
            }

            if(!cancel) addTrip();
        }

        public abstract void addTrip();
        public abstract void finish();

        public void cancel(){
            this.cancel = true;
        }
    }

    private class AddRequest extends PreAddTrip {

        @Override
        public void addTrip() {
            switch (parent.getBags()){
                case(1): trip.setBag(1);
                case(2): trip.setBag(2);
            }

            String error = trip.validateRequest();
            if(error != null){
                parent.error(error);
            } else {
                tripTask = new AddTrip();
                tripTask.execute("request");
            }
            finish();
        }

        @Override
        public void finish() {
            requestTask = null;
        }
    }

    private class AddRide extends PreAddTrip {

        @Override
        public void addTrip() {
            trip.setPassengers(parent.getPassengers());
            trip.setPrice(parent.getPrice());

            String error = trip.validateRide();
            if(error != null){
                parent.error(error);
            } else {
                tripTask = new AddTrip();
                tripTask.execute("ride");
            }
            finish();
        }

        @Override
        public void finish() {
            rideTask = null;
        }
    }

    private class AddTrip extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;

        @Override
        protected AppResponse doInBackground(String... params) {
            JSONObject json = new JSONObject();
            try {
                if(params[0].equals("request")){
                    json = trip.toJsonRequest();
                } else {
                    json = trip.toJsonRide();
                }
                if(json.has("_id")) json.remove("_id");
                if(json.has("type")) json.remove("type");
                if(json.has("status")) json.remove("status");
            } catch (JSONException | UnsupportedEncodingException e) {
                Log.e("AddTrip", "JSON", e);
            }
            AppResponse appResponse = new AppResponse();
            appResponse.setArgs(params[0]);
            if(params[0].equals("request")){
                tripManager.postRequest(json, appResponse);
            } else {
                tripManager.postRide(json, appResponse);
            }
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse appResponse) {
            String key = appResponse.getArgs().substring(0, 1).toUpperCase()
                    + appResponse.getArgs().substring(1);
            if(cancel) {
                finish();
                return;
            }
            if(appResponse.isValid() && appResponse.getStatus() == 200){
                Toast.makeText(parent.getActivity(), key + " Added", Toast.LENGTH_SHORT).show();
                parent.toMyRides();
            } else {
                parent.error("Add " + key + " Failed");
            }
            finish();
        }

        public void cancel(){
            this.cancel = true;
        }

        public void finish(){
            tripTask = null;
        }
    }
}
