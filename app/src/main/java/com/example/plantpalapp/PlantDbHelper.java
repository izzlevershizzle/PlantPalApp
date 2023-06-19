package com.example.plantpalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlantDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = PlantDbHelper.class.getSimpleName();
    public static final String DB_NAME = "plant_database.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_PLANTS = "table_plants";
    public static final String TABLE_USER = "table_user";

    // PLANTS
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PURCHASED = "purchased";
    public static final String COLUMN_PLACEMENT = "placement";
    public static final String COLUMN_WATERING = "watering";
    public static final String COLUMN_SIZE = "size";

    // USER
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";


    // PLANTS TABLE
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_PLANTS +
             "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             COLUMN_NAME + " TEXT NOT NULL, " +
             COLUMN_PURCHASED + " TEXT NOT NULL, " +
             COLUMN_PLACEMENT + " TEXT NOT NULL, " +
             COLUMN_WATERING + " TEXT NOT NULL, "  +
             COLUMN_SIZE + " TEXT NOT NULL);";

    // USER TABLE
    public static final String SQL_CREATE_USER =
            "CREATE TABLE " + TABLE_USER +
             "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             COLUMN_USERNAME + " TEXT NOT NULL, " +
             COLUMN_PASSWORD + " TEXT NOT NULL);";


    public PlantDbHelper(Context context) {
        // Call the super constructor of SQLiteOpenHelper to initialize the database
        super(context, DB_NAME, null, DB_VERSION);
        // Log a message indicating that the database was created
        Log.d(LOG_TAG, "DbHelper created the database" + getDatabaseName() );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Table is created with SQL Command: " + SQL_CREATE + " .");
            // SQL command to create the table for plants
            db.execSQL(SQL_CREATE);
            // SQL command to create the table for user
            Log.d(LOG_TAG, "Table is created with SQL Command: " + SQL_CREATE_USER + " .");
            db.execSQL(SQL_CREATE_USER);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Error creating table: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
