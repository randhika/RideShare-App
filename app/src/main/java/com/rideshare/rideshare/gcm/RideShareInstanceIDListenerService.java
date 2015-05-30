package com.rideshare.rideshare.gcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;
import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.manager.IdManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RideShareInstanceIDListenerService extends InstanceIDListenerService {

    private IdManager manager;
    private String userID;

    public RideShareInstanceIDListenerService(){
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        manager = new IdManager();
        userID = intent.getStringExtra("USER");
        new TokenGrabber().execute();
        return result;
    }

    @Override
    public void onTokenRefresh() {
        new TokenGrabber().execute();
    }

    private String getToken() throws IOException {
        String PROJECT_ID = "768224652247";
        String SCOPE = "GCM";
        return InstanceID.getInstance(this).getToken(PROJECT_ID, SCOPE);
    }

    private class TokenGrabber extends AsyncTask<Void, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(Void... params) {
            String token;
            JSONObject json = new JSONObject();
            AppResponse result = new AppResponse();
            try {
                token = getToken();
            } catch (IOException e) {
                Log.e("TokenGrabber", "IOException", e);
                return result;
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                    getApplicationContext());
            String storedToken = prefs.getString("GCM_TOKEN", null);
            if(storedToken != null && storedToken.equals(token)){
                return null;
            }
            try {
                json.put("id", userID);
                json.put("gcmToken", token);
                result.setArgs(token);
                manager.updateUser(json, result);
            } catch (JSONException e) {
                Log.e("TokenGrabber", "JSONException", e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(AppResponse appResponse) {
            super.onPostExecute(appResponse);
            if(appResponse != null && (!appResponse.isValid() || appResponse.getStatus() != 200)){
                Toast.makeText(getApplicationContext(), "Notification Registration Failed",
                        Toast.LENGTH_SHORT).show();
            } else if(appResponse != null){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("GCM_TOKEN", appResponse.getArgs());
                editor.apply();
            }
        }
    }
}