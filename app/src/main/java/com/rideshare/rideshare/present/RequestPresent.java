package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.entity.app.Geo;
import com.rideshare.rideshare.entity.app.RideStop;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.manager.TripManager;
import com.rideshare.rideshare.service.GeoCoderParser;
import com.rideshare.rideshare.view.fragment.RequestFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestPresent {

    private RequestFragment parent;
    private GeoCoderParser geoParser;
    private Trip trip;
    private TripManager tripManager;
    private AsyncTask task;

    public RequestPresent(RequestFragment parent, Trip trip) {
        this.parent = parent;
        this.geoParser = new GeoCoderParser();
        this.trip = trip;
        this.tripManager = new TripManager();
    }


    public void updateRequest() {
        int bagOption = parent.getBags();
        int smokerOption = parent.getSmoker();

        switch (bagOption){
            case(1): trip.setBag(1);
            case(2): trip.setBag(2);
        }

        switch (smokerOption){
            case(1): trip.setSmoker(3);
            case(2): trip.setSmoker(1);
            case(3): trip.setSmoker(2);
        }

        trip.setDate(parent.getDate());
        trip.setTimeFrom(parent.getTimeFrom());
        trip.setTimeUntil(parent.getTimeUntil());

        new UpdateRequest().execute(parent.getSource(), parent.getDestination());
    }

    private class UpdateRequest extends AsyncTask<String, Void, List<Geo>> {

        @Override
        protected List<Geo> doInBackground(String... params) {
            Geo sourceInfo = geoParser.sendAddress(params[0]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e("GeoFinder", "SLEEP", e);
            }
            Geo destinationInfo = geoParser.sendAddress(params[1]);

            ArrayList<Geo> geos = new ArrayList<>();
            geos.add(sourceInfo);
            geos.add(destinationInfo);
            return geos;
        }

        @Override
        protected void onPostExecute(List<Geo> geos) {
            if(geos.get(0) == null){
                parent.clean("source");
                parent.error("Source Address can not be Found");
            } else {
                trip.setSource(parent.getSource());
                trip.setGeoSource(new RideStop(null, null, null, geos.get(0).getLatitude(),
                        geos.get(0).getLongitude()));
            }

            if(geos.get(0) == null){
                parent.clean("destination");
                parent.error("Destination Address can not be Found");
            } else {
                trip.setDestination(parent.getDestination());
                trip.setGeoDestination(new RideStop(null, null, null, geos.get(1).getLatitude(),
                        geos.get(1).getLongitude()));
            }

            new ServerUpdateRequest().execute();
        }
    }

    private class ServerUpdateRequest extends AsyncTask<Void, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(Void... params) {
            JSONObject json = new JSONObject();
            try {
                json = trip.toJsonRequest();
                String id = json.getString("_id");
                json.put("id", id);
                json.remove("_id");
                if(json.has("user")) json.remove("user");
                if(json.has("type")) json.remove("type");
                if(json.has("status")) json.remove("status");
            } catch (JSONException e) {
                Log.e("ServerUpdateRequest", "JSON", e);
            }
            AppResponse appResponse = new AppResponse();
            tripManager.updateRequest(json, appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse appResponse) {
            if(appResponse.isValid() && appResponse.getStatus() == 200){
                Toast.makeText(parent.getActivity(), "Request Updated", Toast.LENGTH_SHORT).show();
                parent.toMyRides();
            } else {
                parent.error("Update Failed");
            }
        }
    }
}
