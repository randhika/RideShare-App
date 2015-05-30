package com.rideshare.rideshare.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.entity.app.Notification;
import com.rideshare.rideshare.present.LoginPresent;
import com.rideshare.rideshare.utils.DateHandler;
import com.rideshare.rideshare.utils.Security;
import com.rideshare.rideshare.view.dialog.WaitDialog;

import java.text.ParseException;
import java.util.ArrayList;

public class LoginActivity extends FragmentActivity {

    private LoginPresent loginPresent;
    private WaitDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginPresent = new LoginPresent(this);
        waitDialog = new WaitDialog(this, "Connecting...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        retrieveLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void showError(String text){
        TextView error = (TextView) findViewById(R.id.error);
        error.setText(text);
    }

    public void singUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        loginPresent.login(email, password);
    }

    public void enterApp(String userId, String notificationStr){
        if(userId != null)
            saveLoginInfo(userId);
        Intent intent = new Intent(this, NavigationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USER", userId);
        bundle.putString("NOTIFICATION_STR", notificationStr);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void saveLoginInfo(String userId) {
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", Security.encrypt(userId));
        editor.putString("email", Security.encrypt(email));
        editor.putString("password", Security.encrypt(password));
        editor.apply();
    }

    private void retrieveLogin(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String email = prefs.getString("email", null);
        String password = prefs.getString("password", null);

        if(email == null || password == null) return;

        email = Security.decrypt(email);
        password = Security.decrypt(password);

        if(email == null || password == null) return;

        ((EditText) findViewById(R.id.email)).setText(email);
        ((EditText) findViewById(R.id.password)).setText(password);
    }

    public void showWaitingDialog(){
        waitDialog.show();
    }

    public void dismissWaitingDialog(){
        waitDialog.dismiss();
    }
}
