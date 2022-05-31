package com.team63.zooseeker;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GPSLocationSubject extends BasicLocationSubject implements LocationSubject {
    LocationManager locationManager;

    public GPSLocationSubject(LocationManager locationManager, Map<String, ZooData.VertexInfo> zooMap) {
        super(zooMap);
        this.locationManager = locationManager;
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("ZooSeeker", String.format("GPS Location updated: %s", location));
                changeLocation(location.getLatitude(), location.getLongitude());
            }// 32.7337949159672, -117.1769866067953
        };


        var provider = LocationManager.GPS_PROVIDER;
        try {
            locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
        }
        catch(SecurityException se){
            se.printStackTrace();
        }
    }
}
