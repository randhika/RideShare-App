package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import android.util.Log;

import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.entity.app.Rider;
import com.rideshare.rideshare.manager.TripManager;
import com.rideshare.rideshare.view.fragment.PassengersDetailsFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

public class PassengersDetailsPresent {

    private PassengersDetailsFragment parent;
    private TripManager tripManager;
    private RidersGrabber task;

    public PassengersDetailsPresent(PassengersDetailsFragment parent){
        this.parent = parent;
        tripManager = new TripManager();
    }

    public void getRiders(String rideID){
        task = new RidersGrabber();
        task.execute(rideID);
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
                finish();
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
                finish();
                return;
            }
            if(!cancel){
                parent.setRiders(passengers, waitingList);
            }
        }

        public void finish(){
            task = null;
        }

        public void cancel() {
            this.cancel = true;
        }
    }
}
