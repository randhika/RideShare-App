package com.rideshare.rideshare.view.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.present.LoginPresent;
import com.rideshare.rideshare.view.dialog.WaitDialog;


public class LoginActivity extends FragmentActivity {

    private LoginPresent loginPresent;
    private WaitDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginPresent = new LoginPresent(this);
        waitDialog = new WaitDialog(this, "Connecting...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    public void showWaitingDialog(){
        waitDialog.show();
    }

    public void dismissWaitingDialog(){
        waitDialog.dismiss();
    }
}
