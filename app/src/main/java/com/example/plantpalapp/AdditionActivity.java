package com.example.plantpalapp;

import static android.view.View.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
import android.widget.Toast;

public class AdditionActivity extends AppCompatActivity {

    public static final String LOG_TAG = AdditionActivity.class.getSimpleName();

    private PlantDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the activity
        setContentView(R.layout.activity_addition);

        // Create an instance of PlantDataSource for database handling
        dataSource = new PlantDataSource(this);

        // Activate the add button, which handles adding plant data
        activateAddButton();
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

    private void activateAddButton() {
        // Get references to the UI elements
        Button buttonAddPlant = (Button) findViewById(R.id.button_addPlant);
        final EditText editTextName = (EditText) findViewById(R.id.editText_plantName);
        final EditText editTextPurchased = (EditText) findViewById(R.id.editText_purchaseDate);
        final EditText editTextPlacement = (EditText) findViewById(R.id.editText_placement);
        final EditText editTextWatering = (EditText) findViewById(R.id.editText_watering);
        final EditText editTextSize = (EditText) findViewById(R.id.editText_size);

        // Set the click listener for the "Add Plant" button
        buttonAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the text entered in the EditText fields
                String nameString = editTextName.getText().toString();
                String purchasedString = editTextPurchased.getText().toString();
                String placementString = editTextPlacement.getText().toString();
                String wateringString = editTextWatering.getText().toString();
                String sizeString = editTextSize.getText().toString();

                // Validate that the required fields are not empty
                if (TextUtils.isEmpty(nameString)) {
                    editTextName.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if (TextUtils.isEmpty(purchasedString)) {
                    editTextPurchased.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if (TextUtils.isEmpty(placementString)) {
                    editTextPlacement.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if (TextUtils.isEmpty(wateringString)) {
                    editTextWatering.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if (TextUtils.isEmpty(sizeString)) {
                    editTextSize.setError(getString(R.string.editText_errorMessage));
                    return;
                }

                // Clear the EditText fields
                editTextName.setText("");
                editTextPurchased.setText("");
                editTextPlacement.setText("");
                editTextWatering.setText("");
                editTextSize.setText("");

                // Create a new PlantMaintenance object and add it to the database
                dataSource.createPlantMaintenance(nameString, purchasedString, placementString, wateringString, sizeString);

                // Display a success message to the user
                Toast.makeText(AdditionActivity.this, "Addition successful!", Toast.LENGTH_SHORT).show();

                // Hide the keyboard
                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
    }

    public void backToOverview(View v) {
        Intent i = new Intent(this, OverviewActivity.class);
        startActivity(i);
    }

    public void initLogout(View v) {
        Intent i = new Intent(this, MainActivity.class);
        // Clear the existing activity stack and start a fresh session
        // The user will start from the login screen as if it's a new login
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Start the MainActivity
        startActivity(i);
        Toast.makeText(AdditionActivity.this, "Logout Successful!", Toast.LENGTH_SHORT).show();
        // Finish the current activity (AdditionActivity) to remove it from the activity stack
        finish();
    }

}