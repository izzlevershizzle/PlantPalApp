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

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = AdditionActivity.class.getSimpleName();
    private PlantDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("PlantPal");
        dataSource = new PlantDataSource(this);
        activateLoginButton();
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

    private void activateLoginButton() {
        // Get references to the login button and input fields
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        final EditText editTextUsername = (EditText) findViewById(R.id.editText_username_login);
        final EditText editTextPassword = (EditText) findViewById(R.id.editText_password_login);

        // Set a click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the entered username and password strings
                String userNameLoginString = editTextUsername.getText().toString();
                String passwordLoginString = editTextPassword.getText().toString();

                // Check if the fields are empty
                if (TextUtils.isEmpty(userNameLoginString)) {
                    editTextUsername.setError(getString(R.string.editText_errorMessage));
                    return;
                }

                if (TextUtils.isEmpty(passwordLoginString)) {
                    editTextPassword.setError(getString(R.string.editText_errorMessage));
                    return;
                }

                // Call the checkUsernameAndPassword() method
                // on the dataSource object to validate the entered username and password combination.
                boolean loginSuccessful = dataSource.checkUsernameAndPassword(userNameLoginString, passwordLoginString);

                // If login is successful, show a success message and start the OverviewActivity
                if (loginSuccessful) {
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
                    startActivity(intent);
                } else {
                    // If login is unsuccessful, show an error message
                    Toast.makeText(MainActivity.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                }

                // Clear the input fields
                editTextUsername.setText("");
                editTextPassword.setText("");

                // Hide the keyboard
                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

    }

    public void launchOverview(View v) {
        Intent i = new Intent(this, OverviewActivity.class);
        startActivity(i);
    }

    public void launchSignUp(View v) {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }
}