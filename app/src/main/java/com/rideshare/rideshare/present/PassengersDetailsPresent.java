package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.entity.app.Rider;
import com.rideshare.rideshare.manager.TripManager;
import com.rideshare.rideshare.view.activity.NavigationActivity;
import com.rideshare.rideshare.view.fragment.PassengersDetailsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class PassengersDetailsPresent {

    private PassengersDetailsFragment parent;
    private TripManager tripManager;
    private String rideID;

    public PassengersDetailsPresent(PassengersDetailsFragment parent, String rideID){
        this.parent = parent;
        this.tripManager = new TripManager();
        this.rideID = rideID;
    }

    public void getRiders(){
        new RidersGrabber().execute(rideID);
    }

    public void removePassenger(Rider rider) {
        new RemovePassenger().execute(rideID, rider.getRequest(), rider.getId());
    }

    public void removeWaitingUser(Rider rider) {
        new RemoveWaitingPassenger().execute(rideID, rider.getRequest(), rider.getId());
    }

    public void addPassenger(Rider rider) {
        new AddPassenger().execute(rider.getId(), rideID, rider.getRequest());
    }

    private class RidersGrabber extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            tripManager.getRiders(params[0], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            if(!result.isValid()){
                return;
            }
            ArrayList<Rider> passengers = new ArrayList<>();
            ArrayList<Rider> waitingList = new ArrayList<>();
            try {
                JSONArray waitingListJSON = result.getJSON().getJSONArray("waitingList");
                JSONArray passengersJSON = result.getJSON().getJSONArray("passengers");
                for(int i = 0; i < passengersJSON.length(); i++){
                    passengers.add(Rider.fromJSON(passengersJSON.getJSONObject(i), 1));
                }
                for(int i = 0; i < waitingListJSON.length(); i++){
                    waitingList.add(Rider.fromJSON(waitingListJSON.getJSONObject(i), 0));
                }
            } catch (JSONException e) {
                Log.e("RidersGrabber", "JSONException", e);
                return;
            }
            if(!cancel){
                parent.setRiders(passengers, waitingList);
            }
        }

        public void cancel() {
            this.cancel = true;
        }
    }

    private class AddPassenger extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            JSONObject json = new JSONObject();
            try {
                json.put("request", params[2]);
                json.put("user", params[0]);
                json.put("ride", params[1]);
            } catch (JSONException e) {
                Log.e("AddPassenger", "JSONException", e);
            }
            tripManager.addToPassengerList(json, appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            if(!result.isValid()){
                Toast.makeText(parent.getActivity(), "Failed Added to Ride", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if(result.getStatus() != 200){
                Toast.makeText(parent.getActivity(), "Failed Added to Ride", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            Toast.makeText(parent.getActivity(), "Added to Ride", Toast.LENGTH_SHORT).show();
            reload();
        }

        public void cancel() {
            this.cancel = true;
        }
    }

    private class RemovePassenger extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
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
            reload();
        }

        public void cancel() {
            this.cancel = true;
        }
    }

    private class RemoveWaitingPassenger extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            tripManager.removeFromWaitingList(params[0], params[1], params[2], appResponse);
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
            reload();
        }

        public void cancel() {
            this.cancel = true;
        }
    }

    private void reload(){
        Bundle bundle = new Bundle();
        bundle.putString("RIDE_ID", rideID);
        ((NavigationActivity) parent.getActivity()).selectItem(12, bundle);
    }
}
