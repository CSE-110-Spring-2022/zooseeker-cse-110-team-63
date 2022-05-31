package com.team63.zooseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public Switch detailedSwitch;
    public Switch gpsSwitch;
    public boolean detailedDir;
    public boolean gpsActive;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("filenames", MODE_PRIVATE);
        editor = preferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        detailedSwitch = findViewById(R.id.detailed_switch);
        gpsSwitch = findViewById(R.id.gps_active_switch);

        loadProfile();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadProfile() {

        SharedPreferences preferences = getSharedPreferences("filenames", MODE_PRIVATE);

        detailedDir = preferences.getBoolean("detailedDir",false);
        detailedSwitch.setChecked(detailedDir);

        gpsActive = preferences.getBoolean("gpsActive",false);
        gpsSwitch.setChecked(gpsActive);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onGpsSwitchClicked(View view) {
        gpsActive = ((Switch) view).isChecked();
        editor.putBoolean("gpsActive", gpsActive);
        editor.commit();
        Log.d("Test", String.format("gpsActive field set to %b", gpsActive));
    }

    public void onDetailedSwitchClicked(View view) {
        detailedDir = ((Switch) view).isChecked();
        editor.putBoolean("detailedDir", detailedDir);
        editor.commit();
        Log.d("Test", String.format("detailedDir field set to %b", detailedDir));
    }
}