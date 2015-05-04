package com.rideshare.rideshare.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.present.SignUpPresent;


public class SignUpActivity extends Activity {

    private SignUpPresent present;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        present = new SignUpPresent(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void registration(View v) {
        setError("");

        String fullName = ((EditText) findViewById(R.id.fullName)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.confirmPassword)).getText().toString();

        present.registration(fullName, email, phone, password, confirmPassword);
    }

    public void setError(String error){
        TextView errorView = (TextView) findViewById(R.id.error);
        errorView.setText(error);
    }

    public void registrationSuccess() {
        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
