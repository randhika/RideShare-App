package com.rideshare.rideshare.present;

import android.os.AsyncTask;

import com.rideshare.rideshare.manager.IdManager;
import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.view.activity.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpPresent {

    private SignUpActivity parent;
    private IdManager idManager;

    public SignUpPresent(SignUpActivity parentActivity){
        this.parent = parentActivity;
        idManager = new IdManager();
    }

    public void registration(String fullName, String email, String phone, String password,
                             String confirmPassword){
        if(fullName.equals("") || email.equals("") || phone.equals("") || password.equals("") ||
                confirmPassword.equals("")){
            parent.setError("Please Fill All The Fields");
        }
        else if(!password.equals(confirmPassword)){
            parent.setError("Passwords and Confirmation not Match");
        }
        else {
            new Registration().execute(fullName, email, phone, password);
        }
    }

    private void postRegistration(AppResponse registrationResult){
        if(!registrationResult.isValid()){
            parent.setError("Unexpected Error Accorded");
            return;
        }
        JSONObject responseJSON = registrationResult.getJSON();
        if(registrationResult.getStatus() != 200){
            int errorCode;
            String msg;
            try {
                errorCode = responseJSON.getInt("code");
                msg = responseJSON.getString("msg");
            } catch (JSONException e) {
                errorCode = -1;
                msg = null;
                parent.setError("Unexpected Error Accorded");
                e.printStackTrace();
            }
            if(errorCode == 10101000 || errorCode == 10101004){
                parent.setError(msg);
            } else {
                parent.setError("Unexpected Error Accorded");
            }
        }
        else {
            parent.registrationSuccess();
        }
    }

    private class Registration extends AsyncTask<String, Void, AppResponse> {

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            idManager.registration(params[0], params[1], params[2], params[3], appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse registrationResult) {
            postRegistration(registrationResult);
        }
    }
}
