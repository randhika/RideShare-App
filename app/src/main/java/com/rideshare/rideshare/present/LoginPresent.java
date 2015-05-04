package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.view.activity.LoginActivity;
import com.rideshare.rideshare.app.IdManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginPresent {

    private LoginActivity parent;
    private IdManager idManager;

    public LoginPresent(LoginActivity parent){
        this.parent = parent;
        idManager = new IdManager();
    }

    public void login(String email, String password){
        if(email.equals("") || password.equals("")){
            parent.showError("Please Enter Email and Password");
        } else {
            new LoginGrabber().execute(email, password);
        }
    }

    private void postLogin(AppResponse loginResult){
        if(!loginResult.isValid()){
            parent.showError("Unexpected Error Accorded");
            return;
        }
        JSONObject responseJSON = loginResult.getJSON();
        if(loginResult.getStatus() != 200){
            int errorCode;
            String msg;
            try {
                errorCode = responseJSON.getInt("code");
                msg = responseJSON.getString("msg");
            } catch (JSONException e) {
                errorCode = -1;
                msg = null;
                parent.showError("Unexpected Error Accorded");
                e.printStackTrace();
            }
            if(errorCode == 10111001 || errorCode == 10111002){
                parent.showError(msg);
            } else {
                parent.showError("Unexpected Error Accorded");
            }
        }
        else {
            try {
                getNotifications(loginResult.getJSON().getString("_id"));
            } catch (JSONException e) {
                parent.showError("Unexpected Error Accorded");
            }
        }
    }

    public void getNotifications(String userId) {
        new NotificationGrabber().execute(userId);
    }

    private void postGetNotifications(AppResponse notificationResult){
        JSONArray notificationsJSON;
        String responseStr, userId;

        if(!notificationResult.isValid()){
            parent.showError("Unexpected Error Accorded");
            return;
        }
        JSONObject responseJSON = notificationResult.getJSON();
        if(notificationResult.getStatus() != 200){
            parent.showError("Unexpected Error Accorded");
            return;
        }
        try {
            notificationsJSON = responseJSON.getJSONArray("notifications");
            userId = notificationsJSON.getJSONObject(0).getString("user");
            responseStr = responseJSON.toString();
        } catch (JSONException e) {
            parent.showError("Unexpected Error Accorded");
            return;
        }
        parent.enterApp(userId, responseStr);
    }

    private class NotificationGrabber extends AsyncTask<String, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            idManager.getNotifications(params[0], null, null, appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            parent.dismissWaitingDialog();
            postGetNotifications(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class LoginGrabber extends AsyncTask<String, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            idManager.login(params[0], params[1], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            postLogin(result);
        }

        @Override
        protected void onPreExecute() {
            parent.showWaitingDialog();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
