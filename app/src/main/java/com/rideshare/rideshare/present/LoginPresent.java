package com.rideshare.rideshare.present;

import android.os.AsyncTask;

import com.rideshare.rideshare.view.activity.LoginActivity;
import com.rideshare.rideshare.app.IdManager;

public class LoginPresent {

    private LoginActivity parent;
    private IdManager idManager;

    public LoginPresent(LoginActivity parent){
        this.parent = parent;
        idManager = new IdManager();
    }

    public void login(String email, String password){
        if(email.equals("") || password.equals("")){
            //parent.showError("Please Enter Email and Password");
        } else {
            new Login().execute(email, password);
        }
        new Login().execute("a", "b");
    }

    private class Login extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            idManager.login(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            parent.dismissWaitingDialog();
        }

        @Override
        protected void onPreExecute() {
            parent.showWaitingDialog();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
