package com.example.plantpalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class PlantDataSource {

    private static final String LOG_TAG = PlantDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private PlantDbHelper dbHelper;

    private String [] columns = {
        PlantDbHelper.COLUMN_ID,
        PlantDbHelper.COLUMN_NAME,
        PlantDbHelper.COLUMN_PURCHASED,
        PlantDbHelper.COLUMN_PLACEMENT,
        PlantDbHelper.COLUMN_WATERING,
        PlantDbHelper.COLUMN_SIZE
    };

    private String[] columnsUser = {
            PlantDbHelper.COLUMN_USER_ID,
            PlantDbHelper.COLUMN_USERNAME,
            PlantDbHelper.COLUMN_PASSWORD
    };

    public PlantDataSource(Context context) {
        // Create an instance of the PlantDbHelper class
        Log.d(LOG_TAG, "Data Source creates the dbHelper");
        dbHelper = new PlantDbHelper(context);
    }

    public void open() {
        // Get a writable database from the PlantDbHelper
        Log.d(LOG_TAG, "A reference to the DB is requested.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "DB reference received. Path to DB: " + database.getPath());
    }

    public void close() {
        // Close the PlantDbHelper
        dbHelper.close();
        Log.d(LOG_TAG, "DB closed.");
    }

    public PlantMaintenance createPlantMaintenance(String name, String purchased, String placement, String watering, String size) {
        // Create a new ContentValues object and populate it with the plant maintenance data
        ContentValues values = new ContentValues();
        values.put(PlantDbHelper.COLUMN_NAME, name);
        values.put(PlantDbHelper.COLUMN_PURCHASED, purchased);
        values.put(PlantDbHelper.COLUMN_PLACEMENT, placement);
        values.put(PlantDbHelper.COLUMN_WATERING, watering);
        values.put(PlantDbHelper.COLUMN_SIZE, size);

        // Insert the values into the database and get the insert ID
        long insertId = database.insert(PlantDbHelper.TABLE_PLANTS, null, values);

        // Query the database to retrieve the newly inserted plant maintenance data
        Cursor cursor = database.query(PlantDbHelper.TABLE_PLANTS,
                        columns, PlantDbHelper.COLUMN_ID + "=" + insertId,
                        null, null, null, null);

        cursor.moveToFirst();
        // Convert the cursor data into a PlantMaintenance object
        PlantMaintenance plantMaintenance = cursorToPlantMaintenance(cursor);
        cursor.close();

        return plantMaintenance;
    }

    public PlantRegistration createUserProfile(String username, String password) {
        // Create a new ContentValues object and populate it with the user profile data
        ContentValues values = new ContentValues();
        values.put(PlantDbHelper.COLUMN_USERNAME, username);
        values.put(PlantDbHelper.COLUMN_PASSWORD, password);

        // Insert the values into the database and get the insert ID
        long insertId = database.insert(PlantDbHelper.TABLE_USER, null, values);

        // Query the database to retrieve the newly inserted user profile data
        Cursor cursor = database.query(PlantDbHelper.TABLE_USER,
                        columnsUser, PlantDbHelper.COLUMN_USER_ID + "=" + insertId,
                        null, null, null, null);

        cursor.moveToFirst();
        // Convert the cursor data into a PlantRegistration object
        PlantRegistration plantRegistration = cursorToPlantRegistration(cursor);
        cursor.close();

        return plantRegistration;

        }

    public Boolean checkUserName(String username) {
        // Query the database to check if the given username already exists
        database = dbHelper.getWritableDatabase();
        Cursor cursor  = database.rawQuery("SELECT * FROM " + PlantDbHelper.TABLE_USER + " WHERE " + PlantDbHelper.COLUMN_USERNAME + " = ?", new String[] {username});
        if (cursor.getCount() == 0)
            return true;
        else
            return false;
    }

    public Boolean checkUsernameAndPassword(String username, String password) {
        // Method checks if the provided username and password combination exists in the database.
        database = dbHelper.getWritableDatabase();
        // Query the database to check if the provided username and password combination exists
        Cursor cursor = database.rawQuery("SELECT * FROM table_user WHERE username = ? AND password = ?", new String[] {username, password});
        // Check if the login was successful by checking the count of the returned rows
        boolean loginSuccessful = cursor.getCount() > 0;
        // Close the cursor
        cursor.close();
        return loginSuccessful;
    }


    public void deletePlant(PlantMaintenance plantMaintenance) {
        // Deletes a plant maintenance entry from the database.
        // Get the ID of the plant maintenance entry
        long id = plantMaintenance.getId();

        // Delete the entry from the database using the ID
        database.delete(PlantDbHelper.TABLE_PLANTS,
                        PlantDbHelper.COLUMN_ID + "=" + id,
                        null);

        // Log the deletion information
        Log.d(LOG_TAG, "Entry deleted! ID: " + id + " Inhalt: " + plantMaintenance.toString());
    }

    private PlantMaintenance cursorToPlantMaintenance(Cursor cursor) {
        // Converts a cursor row into a PlantMaintenance object.

        // Retrieve the column indices for the desired columns
        int idIndex = cursor.getColumnIndex(PlantDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(PlantDbHelper.COLUMN_NAME);
        int idPurchased = cursor.getColumnIndex(PlantDbHelper.COLUMN_PURCHASED);
        int idPlacement = cursor.getColumnIndex(PlantDbHelper.COLUMN_PLACEMENT);
        int idWatering = cursor.getColumnIndex(PlantDbHelper.COLUMN_WATERING);
        int idSize = cursor.getColumnIndex(PlantDbHelper.COLUMN_SIZE);

        // Retrieve the values from the cursor based on the column indices
        String size = cursor.getString(idSize);
        String watering = cursor.getString(idWatering);
        String placement = cursor.getString(idPlacement);
        String purchased = cursor.getString(idPurchased);
        String name = cursor.getString(idName);
        long id = cursor.getLong(idIndex);

        // Create a new PlantMaintenance object with the retrieved values
        PlantMaintenance plantMaintenance = new PlantMaintenance(id, name, purchased, placement, watering, size);
        return plantMaintenance;
    }

    private PlantRegistration cursorToPlantRegistration(Cursor cursor) {
        // Converts a cursor row into a PlantRegistration object.

        // Retrieve the column indices for the desired columns
        int idIndex = cursor.getColumnIndex(PlantDbHelper.COLUMN_USER_ID);
        int idUsername = cursor.getColumnIndex(PlantDbHelper.COLUMN_USERNAME);
        int idPassword = cursor.getColumnIndex(PlantDbHelper.COLUMN_PASSWORD);

        // Retrieve the values from the cursor based on the column indices
        String password = cursor.getString(idPassword);
        String username = cursor.getString(idUsername);
        long id = cursor.getLong(idIndex);

        // Create a new PlantRegistration object with the retrieved values
        PlantRegistration plantRegistration = new PlantRegistration(id, username, password);
        return plantRegistration;
    }

    public List<PlantMaintenance> getAllPlants() {
        // Retrieves all plant maintenance entries from the database.
        List<PlantMaintenance> plantList = new ArrayList<>();

        // Execute a query to retrieve all rows from the plants table
        Cursor cursor = database.query(PlantDbHelper.TABLE_PLANTS,
                        columns, null, null, null, null, null);

        // Move the cursor to the first row
        cursor.moveToFirst();
        PlantMaintenance plantMaintenance;

        // Iterate over the cursor rows to read and convert the data into PlantMaintenance objects
        while(!cursor.isAfterLast()) {
            // Convert the current row into a PlantMaintenance object
            plantMaintenance = cursorToPlantMaintenance(cursor);
            // Add the PlantMaintenance object to the plantList
            plantList.add(plantMaintenance);
            // Print the ID and content of the current PlantMaintenance object
            Log.d(LOG_TAG, "ID: " + plantMaintenance.getId() + ", Inhalt: " + plantMaintenance.toString());
            // Move to the next row
            cursor.moveToNext();
        }

        // Close the cursor
        cursor.close();

        return plantList;
    }

}