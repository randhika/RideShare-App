package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.entity.app.Suggestion;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.manager.TripManager;
import com.rideshare.rideshare.view.fragment.ConnectedRidesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class ConnectedRidesPresent {

    private String userID;
    private String requestID;
    private int requestStatus;
    private ConnectedRidesFragment parent;
    private TripManager tripManager;

    public ConnectedRidesPresent(String userID, String requestID, int requestStatus,
                                 ConnectedRidesFragment parent){
        this.userID = userID;
        this.requestID = requestID;
        this.parent = parent;
        this.requestStatus = requestStatus;
        this.tripManager = new TripManager();
    }

    public void getConnectedRides(){
        new ConnectedRidesGrabber().execute(userID);
    }

    public void removeFromRide(Suggestion suggestion) {
        new RemovePassenger().execute(suggestion.getRideID(), requestID, userID);
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    private class RemovePassenger extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;
        private String rideID;

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            rideID = params[0];
            if(requestStatus == 0)
                tripManager.removeFromWaitingList(params[0], params[1], params[2], appResponse);
            else
                tripManager.removeFromPassengerList(params[0], params[1], params[2], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            if(!result.isValid()){
                Toast.makeText(parent.getActivity(), "Fail to Removed from Ride",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if(result.getStatus() != 200){
                Toast.makeText(parent.getActivity(), "Fail to Removed from Ride",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(parent.getActivity(), "Removed from Ride", Toast.LENGTH_SHORT).show();
            parent.reload(rideID);
        }

        public void cancel() {
            this.cancel = true;
        }
    }

    private class ConnectedRidesGrabber extends AsyncTask<String, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            parent.clear();
            tripManager.getConnectedRides(params[0], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            if(!result.isValid()){
                return;
            }
            if(result.getStatus() == 200) {
                try {
                    ArrayList<Suggestion> rides = new ArrayList<>();
                    JSONArray ridesJSON = result.getJSON().getJSONArray("rides");
                    for(int i = 0; i < ridesJSON.length(); i++){
                        JSONObject rideJSON = ridesJSON.getJSONObject(i);
                        Suggestion ride = Suggestion.fromJSON(rideJSON);
                        rides.add(ride);
                    }
                    parent.show(rides);
                } catch (JSONException | ParseException e) {
                    Log.e("onPostExecute", "JSONException", e);
                    Toast.makeText(parent.getActivity(), "Failed Get Connected Rides",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(parent.getActivity(), "Failed Get Connected Rides",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
