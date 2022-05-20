package com.team63.zooseeker;

import java.util.ArrayList;
import java.util.List;

public class MockLocationSubject implements LocationSubject {
    private List<LocationObserver> observers = new ArrayList<>();
    private final double latitude;
    private final double longitude;

    MockLocationSubject(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
            o.updateLocation(latitude, longitude);
        }
    }
}
