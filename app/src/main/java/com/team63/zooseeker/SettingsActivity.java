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

    public PlanViewModel viewModel;
    public Switch detailedSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        viewModel = new ViewModelProvider(this)
                .get(PlanViewModel.class);

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

        SharedPreferences preferences = getSharedPreferences("filenames", MODE_PRIVATE);

        boolean detailedDir = preferences.getBoolean("detailedDir",false);
        detailedSwitch.setChecked(detailedDir);

        Log.d("TEST", String.valueOf(detailedDir));
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
        viewModel.setDetailedDir(detailedSwitch.isChecked());
    }
}