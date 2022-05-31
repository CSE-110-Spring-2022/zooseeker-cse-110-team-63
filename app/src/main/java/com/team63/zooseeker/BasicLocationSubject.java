package com.team63.zooseeker;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class BasicLocationSubject implements LocationSubject {
    private double latitude, longitude;
    private Map<String, ZooData.VertexInfo> zooMap;
    private String nearestExhibit;

    public BasicLocationSubject(Map<String, ZooData.VertexInfo> zooMap) {
        this.zooMap = zooMap;
    }

    private HashSet<LocationObserver> observers = new HashSet<>();

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
        String nearestExhibit = "";
        for(var vertex : zooMap.values()){
            // if our exhibit is in a group, it has no coordinates, so we should not
            // consider it when searching for the closest exhibit
            if (vertex.groupId != null) continue;
            double currentDist = getDist(latitude, vertex.lat, longitude, vertex.lng);
            if(currentDist < closestDist){
                closestDist = currentDist;
                nearestExhibit = vertex.id;
            }
        }
        this.nearestExhibit = nearestExhibit;
        notifyObservers();

        // this should have to compute distance with latitude and longitude
        //double currentDistanceFromImmediate = immediateExhibit.directionInfo.distance;


        // assuming the check has passed and we need to replan, TODO
    }

    protected double getDist(double x1, double x2, double y1, double y2){
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
    public void notifyObservers() {
        Log.d("Test", String.format("Number of observers notified: %d", observers.size()));
        for (LocationObserver o : observers) {
            o.updateLocation(nearestExhibit);
        }
    }
}
