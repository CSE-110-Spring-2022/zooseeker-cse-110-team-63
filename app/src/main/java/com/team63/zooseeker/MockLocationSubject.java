package com.team63.zooseeker;

import java.util.ArrayList;
import java.util.List;

public class MockLocationSubject implements LocationSubject {
    private List<LocationObserver> observers = new ArrayList<>();

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
//            o.updateLocation(latitude, longitude);
            o.updateLocation(s);
        }
    }
}
