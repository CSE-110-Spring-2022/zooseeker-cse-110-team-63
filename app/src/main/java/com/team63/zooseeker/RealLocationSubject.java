package com.team63.zooseeker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RealLocationSubject implements LocationSubject {
    LocationManager locationManager;
    private double latitude, longitude;
    private Map<String, ZooData.VertexInfo> zooMap;
    private String nearestExhibit;

    private List<LocationObserver> observers = new ArrayList<>();

    public RealLocationSubject(LocationManager locationManager, Map<String, ZooData.VertexInfo> zooMap) {
        this.locationManager = locationManager;
        this.zooMap = zooMap;
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (latitude != location.getLatitude() || longitude != location.getLongitude()) {
                    Log.d("ZooSeeker", String.format("Location changed: %s", location));
                    changeLocation(location.getLatitude(), location.getLongitude());
                }
            }
        };


        var provider = LocationManager.GPS_PROVIDER;
        try {
            locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
        }
        catch(SecurityException se){
            se.printStackTrace();
        }
    }

    public void changeLocation(double newLat, double newLng){
        latitude = newLat;
        longitude = newLng;
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
        double closestDist = Double.MAX_VALUE;
        String currentNearestExhibit = "";
        for(var vertex : zooMap.values()){
            double currentDist = getDist(latitude, vertex.lat, longitude, vertex.lng);
            if(currentDist < closestDist){
                closestDist = currentDist;
                currentNearestExhibit = vertex.id;
            }
        }
        if(!currentNearestExhibit.equals(nearestExhibit)){
            nearestExhibit = currentNearestExhibit;
            notifyObservers(nearestExhibit);
        }

        // this should have to compute distance with latitude and longitude
        //double currentDistanceFromImmediate = immediateExhibit.directionInfo.distance;


        // assuming the check has passed and we need to replan, TODO
    }

    private double getDist(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

    @Override
    public void registerObserver(LocationObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(LocationObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String s) {
        for (LocationObserver o : observers) {
            o.updateLocation(s);
        }
    }
}
