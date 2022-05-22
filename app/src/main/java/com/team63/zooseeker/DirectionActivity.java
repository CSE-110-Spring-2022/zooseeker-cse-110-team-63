package com.team63.zooseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
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
import java.util.List;

public class DirectionActivity extends AppCompatActivity implements LocationObserver, LocationListener {
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

    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        exhibitView = findViewById(R.id.exhibit_view);
        directionsView = findViewById(R.id.directions_view);
        nextBtn = findViewById(R.id.next_exhibit_btn);
        prevBtn = findViewById(R.id.previous_exhibit_btn);
        skipBtn = findViewById(R.id.skip_exhibit_btn);
        planBtn = findViewById(R.id.plan_btn);

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        directionInd = 0;

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
        updateDirections(directions);
    }

    public void onPrevBtnClicked(View view) {
        directionInd--;
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

    @Override
    public void updateLocation() {
        Log.d("ZooSeeker", "updateLocation");
        var provider = LocationManager.GPS_PROVIDER;
        var locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        var locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d("ZooSeeker", String.format("latitude: %f longitude: %f", location));
//                Log.d("LAB7", String.format("Location changed: %s", location));
//
//                var marker = new MarkerOptions()
//                        .position(new LatLng(
//                                location.getLatitude(),
//                                location.getLongitude()
//                        ))
//                        .title("Navigation Step");

//                map.addMarker(marker);
            }
        };
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("ZooSeeker", "LocationChanged");
    }
}