package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.entity.app.Rank;
import com.rideshare.rideshare.manager.TripManager;
import com.rideshare.rideshare.view.fragment.RankFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RankPresent {

    private RankFragment parent;
    private String userID;
    private ArrayList<Rank> ranks;
    private TripManager tripManager;
    private int mode;
    private String tripID;

    public RankPresent(RankFragment parent, String userID, ArrayList<Rank> ranks){
        this.parent = parent;
        this.userID = userID;
        this.ranks = ranks;
        this.tripManager = new TripManager();
    }

    public void getDriver(String rideID) {
        mode = 0;
        tripID = rideID;
        new DriverGrabber().execute(rideID);
    }

    public void getPassengers(String requestID) {
        mode = 1;
        tripID = requestID;
        new PassengersGrabber().execute(requestID);
    }

    public void rankUsers() {
        for(Rank rank : ranks){
            new RankTask().execute(rank);
        }
    }

    private class RankTask extends AsyncTask<Rank, Void, AppResponse> {

        private boolean cancel = false;

        @Override
        protected AppResponse doInBackground(Rank... params) {
            Rank rank = params[0];
            AppResponse appResponse = new AppResponse();
            if(mode == 1) {
                JSONObject json = new JSONObject();
                try {
                    json.put("request", tripID);
                    json.put("rankingUser", userID);
                    json.put("rider", rank.getId());
                    json.put("rank", rank.getRank());
                } catch (JSONException e) {
                    Log.e("RankTask", "JSONException", e);
                    return appResponse;
                }
                tripManager.rankRide(json, appResponse);
            }
            else {
                JSONObject json = new JSONObject();
                try {
                    json.put("request", tripID);
                    json.put("rankingUser", userID);
                    json.put("driver", rank.getId());
                    json.put("rank", rank.getRank());
                } catch (JSONException e) {
                    Log.e("RankTask", "JSONException", e);
                    return appResponse;
                }
                tripManager.rankRequest(json, appResponse);
            }
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            if(!result.isValid() || result.getStatus() != 200){
                Toast.makeText(parent.getActivity(), "Failed to Rank", Toast.LENGTH_SHORT).show();
            }
            parent.getFragmentManager().popBackStack();
        }

        public void cancel() {
            this.cancel = true;
        }
    }

    private class DriverGrabber extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            tripManager.getRideByRequest(params[0], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            if(!result.isValid()){
                return;
            }
            ArrayList<Rank> rankUsers = new ArrayList<>();
            try {
                JSONObject driver = result.getJSON().getJSONObject("driver");
                Rank user = new Rank();
                user.setId(driver.getString("_id"));
                user.setName(driver.getString("fullName"));
                rankUsers.add(user);
            } catch (JSONException e) {
                Log.e("RidersGrabber", "JSONException", e);
                return;
            }
            if(!cancel){
                parent.initList(rankUsers);
            }
        }

        public void cancel() {
            this.cancel = true;
        }
    }

    private class PassengersGrabber extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;
        private String rideID;

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            rideID = params[0];
            tripManager.getRiders(rideID, appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            if(!result.isValid()){
                return;
            }
            ArrayList<Rank> rankUsers = new ArrayList<>();
            try {
                JSONArray rides = result.getJSON().getJSONArray("rides");
                for(int ind = 0; ind < rides.length(); ind++) {
                    JSONObject ride = rides.getJSONObject(ind);
                    JSONArray passengersJSON = ride.getJSONArray("passengers");
                    for (int i = 0; i < passengersJSON.length(); i++) {
                        JSONObject userJSON = passengersJSON.getJSONObject(i);
                        Rank user = new Rank();
                        user.setId(userJSON.getString("_id"));
                        user.setName(userJSON.getString("fullName"));
                        rankUsers.add(user);
                    }
                }
            } catch (JSONException e) {
                Log.e("RidersGrabber", "JSONException", e);
                return;
            }
            if(!cancel){
                parent.initList(rankUsers);
            }
        }

        public void cancel() {
            this.cancel = true;
        }
    }
}
