package com.team63.zooseeker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class DirectionActivity extends AppCompatActivity implements LocationObserver{
    private List<Direction> directions;
    private List<Step> steps;
    private PlanViewModel planViewModel;
    private int directionInd;
    private LocationManager locationManager;

    private TextView exhibitView;
    private TextView directionsView;
    private Button nextBtn;
    private Button prevBtn;
    private Button skipBtn;
    private EditText latitudeInput;
    private EditText longitudeInput;
    private Button applyBtn;
    private String closestExhibit = "";
    private boolean needAlert = true; // whether or not we need to ask user to reroute.
    // This is reset whenever we click next, or our exhibit changes, or we come back from settings

    private LocationSubject manualLocationSubject;
    private LocationSubject gpsLocationSubject;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
                perms.forEach((perm, isGranted) -> {
                    Log.i("ZooSeeker", String.format("Permission %s granted: %s", perm, isGranted));
                });
            });

    private void updateFromPreferences() {
        needAlert = true;
        Log.d("Test", "calling updateFromPreferences now");
        SharedPreferences preferences = getSharedPreferences("filenames", MODE_PRIVATE);
        planViewModel.setDetailedDir(preferences.getBoolean("detailedDir", false));
        if (preferences.getBoolean("gpsActive", false)) {
            gpsLocationSubject.registerObserver(this);
            manualLocationSubject.removeObserver(this);
        }
        else {
            gpsLocationSubject.removeObserver(this);
            manualLocationSubject.registerObserver(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        Log.d("ZooSeeker", "onCreate1");

        {
            var requiredPermissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };

            var hasNoLocationParams = Arrays.stream(requiredPermissions)
                    .map(perm -> ContextCompat.checkSelfPermission(this, perm))
                    .allMatch(status -> status == PackageManager.PERMISSION_DENIED);

            if(hasNoLocationParams) {
                requestPermissionLauncher.launch(requiredPermissions);
//                return;
            }
        }

        locationManager = (LocationManager) getApplication().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        exhibitView = findViewById(R.id.exhibit_view);
        directionsView = findViewById(R.id.directions_view);
        nextBtn = findViewById(R.id.next_exhibit_btn);
        prevBtn = findViewById(R.id.previous_exhibit_btn);
        skipBtn = findViewById(R.id.skip_exhibit_btn);
        latitudeInput = findViewById(R.id.latitude_input);
        longitudeInput = findViewById(R.id.longitude_input);
        applyBtn = findViewById(R.id.apply_btn);


        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        manualLocationSubject = planViewModel.getManualLocationSubject();
        gpsLocationSubject = planViewModel.getGPSLocationSubject(locationManager);


        directionInd = 0;

//        Log.d("ZooSeeker", String.format("directionInd is: %d", directionInd));
        directionsView.setMovementMethod(new ScrollingMovementMethod());

        LiveData<List<Direction>> liveData = planViewModel.getDirections();
        liveData.observe(this, this::updateDirections);

        prevBtn.setVisibility(View.GONE);
        updateFromPreferences();
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateFromPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    public void updateDirections(List<Direction> directions){
        this.directions = directions;
        this.steps = directions.get(directionInd).getSteps();

        String dirStrings = "";
        int count = 1;
        double cumDist = 0;
        for (Step step : steps) {
            dirStrings += count + ": " + step.toString() + ".\n\n";
            cumDist += step.distance;
            count++;
        }

        String destination = directions.get(directionInd).directionInfo.name;
        directionsView.setText(dirStrings);
        exhibitView.setText(destination + "\n(" + Direction.roundDistance(cumDist) + " ft)");

        SetBtnVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_btn:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNextBtnClicked(View view) {
        directionInd++;
        Log.d("ZooSeeker", String.format("directionInd is: %d", directionInd));
        updateDirections(directions);
        needAlert = true;
    }

    public void onPrevBtnClicked(View view) {
        directionInd--;
        Log.d("ZooSeeker", String.format("directionInd is: %d", directionInd));
        updateDirections(directions);
        needAlert = true;
    }

    public void onSkipBtnClicked(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder
                .setTitle("Alert!")
                .setMessage("Are you sure you want to skip this exhibit?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    planViewModel.skip(directionInd);
                    needAlert = true;
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }


    private void SetBtnVisibility() {
        if (directionInd == 0) prevBtn.setVisibility(View.GONE);
        else prevBtn.setVisibility(View.VISIBLE);

        if (directionInd == directions.size() - 1) nextBtn.setVisibility(View.GONE);
        else nextBtn.setVisibility(View.VISIBLE);

        if (directionInd == directions.size() - 1 || directions.size() == 2)
            skipBtn.setVisibility(View.GONE);
        else skipBtn.setVisibility(View.VISIBLE);
    }

    public void onPlanBtnClicked(View view) {
        planViewModel.generateDirections();
        finish();
    }

    @Override
    public void updateLocation(String nearestExhibit) {
        needAlert = needAlert && !nearestExhibit.equals(directions.get(directionInd).directionInfo.startVertexId);
        if (!needAlert) return;
        Log.d("Test", "updateLocation in DirectionActivity called");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder
                .setTitle("Detected off-route")
                .setMessage("We have detected that your location is off-route. Would you like to replan?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    planViewModel.replan(directionInd, nearestExhibit);
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);
        AlertDialog alertDialog = alertBuilder.create();
        needAlert = false;
        alertDialog.show();
    }

    public void onApplyBtnClicked(View view) {
        ((BasicLocationSubject) manualLocationSubject).changeLocation(
                Double.parseDouble(latitudeInput.getText().toString()),
                Double.parseDouble(longitudeInput.getText().toString())
        );
    }
}