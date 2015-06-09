package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.entity.app.Suggestion;
import com.rideshare.rideshare.manager.TripManager;
import com.rideshare.rideshare.view.fragment.SuggestionFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class SuggestionPresent {

    private SuggestionFragment parent;
    private String requestID;
    private TripManager tripManager;
    private SuggestionGrabber task;

    public SuggestionPresent(SuggestionFragment parent, String requestID){
        this.tripManager = new TripManager();
        this.requestID = requestID;
        this.parent = parent;
    }

    public void getSuggestion(){
        task = new SuggestionGrabber();
        task.execute(requestID);
    }

    public void requestRide(Suggestion suggestion){
        new AddToRide().execute(suggestion);
    }

    private class SuggestionGrabber extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            tripManager.getSuggestions(params[0], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            if(!result.isValid()){
                finish();
                return;
            }
            ArrayList<Suggestion> suggestions = new ArrayList<>();
            try {
                JSONArray suggestionsJSON = result.getJSON().getJSONArray("suggestions");
                for(int i = 0; i < suggestionsJSON.length(); i++){
                    suggestions.add(Suggestion.fromJSON(suggestionsJSON.getJSONObject(i)));
                }
            } catch (JSONException e) {
                Log.e("SuggestionGrabber", "JSONException", e);
                finish();
            } catch (ParseException e) {
                Log.e("SuggestionGrabber", "ParseException", e);
                finish();
            }
            if(!cancel){
                parent.addSuggestions(suggestions);
            }
        }
        public void finish(){
            task = null;
        }

        public void cancel() {
            this.cancel = true;
        }
    }

    private class AddToRide extends AsyncTask<Suggestion, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(Suggestion... params) {
            AppResponse appResponse = new AppResponse();
            JSONObject json = new JSONObject();
            try {
                json.put("user", params[0].getUserID());
                json.put("request", params[0].getRequestID());
                json.put("ride", params[0].getRideID());
                tripManager.addToWaitingList(json, appResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            if(!result.isValid()){
                return;
            }
            if(result.getStatus() == 200) {
                Toast.makeText(parent.getActivity(), "Joined to Ride", Toast.LENGTH_SHORT).show();
            } else if(result.getStatus() == 404){
                Toast.makeText(parent.getActivity(), "Already Added to Ride", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(parent.getActivity(), "Failed Joined to Ride", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void stopAsync(){
        if(task != null)
            task.cancel();
    }
}
