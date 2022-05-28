package com.team63.zooseeker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirectionActivity extends AppCompatActivity implements LocationObserver {
    private List<Direction> directions;
    private List<Step> steps;
    private PlanViewModel planViewModel;
    private int directionInd;

    private TextView exhibitView;
    private TextView directionsView;
    private Button nextBtn;
    private Button prevBtn;
    private Button skipBtn;
    private Button planBtn;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude, longitude;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
                perms.forEach((perm, isGranted) -> {
                    Log.i("ZooSeeker", String.format("Permission %s granted: %s", perm, isGranted));
                });
            });

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
        var provider = LocationManager.GPS_PROVIDER;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // we are setting up a listener (but are still in onCreate()!)
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if(latitude != location.getLatitude() || longitude != location.getLongitude()) {
                    Log.d("ZooSeeker", String.format("Location changed: %s", location));
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    // TODO: at this point, calculate new plan and prompt the user to switch plans if the new plan is better
                    // 1. we CHECK if we are close to later exhibits in the plan. If so and we need a replan,
                    // 2. we START directions from " the current location, which for TSP purposes
                    // would be the graph node closest to the actual lat/long location." @831

                    // to check if we are close to later exhibits in the plan, we must
                    // 1. get those exhibits and their locations
                    //   a. this includes the immediate next exhibit
                    //   b. and the set of exhibits after that
                    // 2. check our distance from each of those
                    // 3. check to see if any in the planned set are closer than immediate.
                    List<Direction> locationListenerDirections =
                            planViewModel.getDirections().getValue();

                    Direction immediateExhibit = locationListenerDirections.get(directionInd);
                    List<Direction> restOfExhibits =
                            locationListenerDirections.subList(directionInd + 1, locationListenerDirections.size());

                    // this should have to compute distance with latitude and longitude
                    //double currentDistanceFromImmediate = immediateExhibit.directionInfo.distance;


                    // assuming the check has passed and we need to replan, TODO



                }
            }
        };
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);

        exhibitView = findViewById(R.id.exhibit_view);
        directionsView = findViewById(R.id.directions_view);
        nextBtn = findViewById(R.id.next_exhibit_btn);
        prevBtn = findViewById(R.id.previous_exhibit_btn);
        skipBtn = findViewById(R.id.skip_exhibit_btn);
        planBtn = findViewById(R.id.plan_btn);

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        directionInd = 0;
        Log.d("ZooSeeker", String.format("directionInd is: %d", directionInd));

        LiveData<List<Direction>> liveData = planViewModel.getDirections();
        liveData.observe(this, this::updateDirections);

        prevBtn.setVisibility(View.GONE);
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

        String destination = steps.get(steps.size() - 1).destination;
        directionsView.setText(dirStrings);
        exhibitView.setText(destination + "\n(" + cumDist + " ft)");

        SetBtnVisibility();
    }

    public void onNextBtnClicked(View view) {
        directionInd++;
        Log.d("ZooSeeker", String.format("directionInd is: %d", directionInd));
        updateDirections(directions);
    }

    public void onPrevBtnClicked(View view) {
        directionInd--;
        Log.d("ZooSeeker", String.format("directionInd is: %d", directionInd));
        updateDirections(directions);
    }

    public void onSkipBtnClicked(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder
                .setTitle("Alert!")
                .setMessage("Are you sure you want to skip this exhibit?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    planViewModel.recalculate(directionInd);
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

    // code in updateLocation is not doing anything. It is simply obeying the design pattern.
    @Override
    public void updateLocation() {
//        Log.d("ZooSeeker", "updateLocation");
//        var provider = LocationManager.GPS_PROVIDER;
//        var locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        var locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(@NonNull Location location) {
//                if(latitude != location.getLatitude() || longitude != location.getLongitude()) {
//                    latitude = location.getLatitude();
//                    longitude = location.getLongitude();
//                    Log.d("ZooSeeker", String.format("latitude: %f longitude: %f", location));
//                }
//            }
//        };
    }
}