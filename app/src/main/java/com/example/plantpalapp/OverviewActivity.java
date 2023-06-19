package com.example.plantpalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;
import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Toast;


public class OverviewActivity extends AppCompatActivity {

    public static final String LOG_TAG = OverviewActivity.class.getSimpleName();

    private PlantDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Log.d(LOG_TAG, "Data Source object is created.");
        dataSource = new PlantDataSource(this);
        initializeContextualActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Data source is opened");
        dataSource.open();

        Log.d(LOG_TAG, "Following entries exist:");
        showAllListEntries();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Data Source is closed.");
        dataSource.close();
    }

    public void showAllListEntries() {
        // Retrieve all plant entries from the data source
        List<PlantMaintenance> plantList = dataSource.getAllPlants();

        // Create an ArrayAdapter to bind the plantList to the ListView
        ArrayAdapter<PlantMaintenance> plantMaintenanceArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_multiple_choice,
                plantList
        );

        // Get a reference to the ListView and set the ArrayAdapter as its adapter
        ListView plantListView = (ListView) findViewById(R.id.listView_plants);
        plantListView.setAdapter(plantMaintenanceArrayAdapter);
    }

    public void initializeContextualActionBar() {
        // Get a reference to the ListView with the ID `listView_plants`
        final ListView plantListView = (ListView) findViewById(R.id.listView_plants);
        // Set the choice mode of the ListView to CHOICE_MODE_MULTIPLE_MODAL
        plantListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        // Set a MultiChoiceModeListener on the ListView
        plantListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Called when the checked state of an item in the ListView changes
                // No action is performed in this method
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Called when the action mode is created
                // Inflate the menu layout for the contextual action bar
                getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Called when the action mode is about to be shown
                // No action is performed in this method
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Called when a contextual action bar item is clicked
                switch (item.getItemId()) {
                    case R.id.cab_delete:
                        // Retrieve the positions of the checked items in the ListView
                        SparseBooleanArray touchedPlantPositions = plantListView.getCheckedItemPositions();
                        for (int i = 0; i < touchedPlantPositions.size(); i++) {
                            boolean isChecked = touchedPlantPositions.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedPlantPositions.keyAt(i);
                                // Retrieve the PlantMaintenance object at the specified position
                                PlantMaintenance plantMaintenance = (PlantMaintenance) plantListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "Position in ListView: " + positionInListView + " Inhalt: " + plantMaintenance.toString());
                                // Delete the plant from the data source
                                dataSource.deletePlant(plantMaintenance);
                            }
                        }
                        // Refresh the ListView to reflect the changes
                        showAllListEntries();
                        // Finish the action mode
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Called when the action mode is destroyed
                // No action is performed in this method
            }
        });
    }

    public void launchAdd(View v) {
        Intent i = new Intent(this, AdditionActivity.class);
        startActivity(i);
    }

    public void initLogout(View v) {
        Intent i = new Intent(this, MainActivity.class);
        // existing activity stack is cleared / user starts from login screen as fresh session
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        Toast.makeText(OverviewActivity.this, "Logout Successful!", Toast.LENGTH_SHORT).show();
        finish();
    }
}