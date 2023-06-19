package com.example.plantpalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    public static final String LOG_TAG = AdditionActivity.class.getSimpleName();
    private PlantDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dataSource = new PlantDataSource(this);
        activateSignUpButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Data source is opened");
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Data Source is closed.");
        dataSource.close();
    }

    private void activateSignUpButton() {
        Button buttonSignUp = (Button) findViewById(R.id.button_signup);
        final EditText editTextUsername = (EditText) findViewById(R.id.editText_username);
        final EditText editTextPassword = (EditText) findViewById(R.id.editText_password);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userNameString = editTextUsername.getText().toString();
                String passwordString = editTextPassword.getText().toString();

                boolean userNameAvail = dataSource.checkUserName(userNameString);

                if (TextUtils.isEmpty(userNameString)) {
                    editTextUsername.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if (TextUtils.isEmpty(passwordString)) {
                    editTextPassword.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if (userNameAvail == false) {
                    editTextUsername.setError("Username is already taken!");
                    return;
                }

                editTextUsername.setText("");
                editTextPassword.setText("");

                dataSource.createUserProfile(userNameString, passwordString);

                Toast.makeText(SignUpActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), OverviewActivity.class);
                startActivity(i);

                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

    }

    public void backToLogin(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}