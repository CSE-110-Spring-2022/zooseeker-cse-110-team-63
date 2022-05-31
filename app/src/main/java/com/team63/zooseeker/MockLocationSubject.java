package com.team63.zooseeker;

import java.util.ArrayList;
import java.util.List;

public class MockLocationSubject implements LocationSubject {
    private List<LocationObserver> observers = new ArrayList<>();
    private String nearestLocation;

    public MockLocationSubject(String nearestLocation) {
        setNearestLocation(nearestLocation);
    }

    public void setNearestLocation(String nearestLocation) {
        this.nearestLocation = nearestLocation;
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
        for (LocationObserver o : observers) {
//            o.updateLocation(latitude, longitude);
            o.updateLocation(nearestLocation);
        }
    }
}
