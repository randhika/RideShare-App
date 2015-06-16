package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rideshare.rideshare.manager.TripManager;
import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.view.fragment.MyRidesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyRidesPresent {

    private MyRidesFragment parent;
    private TripManager manager;

    public MyRidesPresent(MyRidesFragment parent) {
        this.parent = parent;
        this.manager = new TripManager();
    }

    public void getTrips(String userId){
        new FetchTrips().execute(userId);
    }

    public void deleteRide(String id) {
        new DeleteRide().execute(id);
    }

    public void deleteRequest(String id, String userID) {
        new DeleteRequest().execute(id, userID);
    }

    private class DeleteRequest extends AsyncTask<String, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            manager.deleteRequest(params[0], params[1], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse tripResult) {
            if(!tripResult.isValid() || tripResult.getStatus() != 200){
                Toast.makeText(parent.getActivity(), "Delete Failed", Toast.LENGTH_SHORT).show();
            } else {
                parent.reload();
            }
        }
    }

    private class DeleteRide extends AsyncTask<String, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            manager.deleteRide(params[0], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse tripResult) {
            if(!tripResult.isValid() || tripResult.getStatus() != 200){
                Toast.makeText(parent.getActivity(), "Delete Failed", Toast.LENGTH_SHORT).show();
            } else {
                parent.reload();
            }
        }
    }

    private class FetchTrips extends AsyncTask<String, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            manager.getMyRides(params[0], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse tripResult) {
            String nextUrl = null;
            ArrayList<Trip> myTrips = new ArrayList<>();
            if(!tripResult.isValid()){
                return;
            }
            JSONObject tripsResult = tripResult.getJSON();
            try {
                nextUrl = tripsResult.getJSONObject("metadata").getString("nextUrl");
            } catch (JSONException ignored) { }
            try {
                JSONArray trips = tripsResult.getJSONArray("trips");
                for(int i = 0; i < trips.length(); i++){
                    JSONObject trip = trips.getJSONObject(i);
                    Trip t = Trip.fromJSON(trip);
                    if(t != null)
                        myTrips.add(t);
                }
            } catch (JSONException e) {
                Log.e("getTrips", "JSONException", e);
            }
            parent.updateTips(nextUrl, myTrips);
        }
    }
}
