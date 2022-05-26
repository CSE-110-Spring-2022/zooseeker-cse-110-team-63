package com.team63.zooseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public Switch detailedSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        detailedSwitch = findViewById(R.id.detailed_switch);
        loadProfile();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveProfile();
    }

    public void loadProfile() {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        boolean detailedDir = preferences.getBoolean("detailedDir",false);
        detailedSwitch.setChecked(detailedDir);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveProfile() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("detailedDir",detailedSwitch.isChecked());

        editor.apply();
    }

    public void onDetailedDirBtnClicked(View view) {
        {   // alert for testing functionality, will be deleted later.
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder
                    .setTitle("Alert!")
                    .setMessage(String.format("Detailed directions is %b", detailedSwitch.isChecked()))
                    .setPositiveButton("OK", (dialog, id) -> {
                    })
                    .setCancelable(true);

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }
}