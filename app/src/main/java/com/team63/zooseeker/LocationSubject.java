package com.team63.zooseeker;
// TODO: Implement LocationSubject
public interface LocationSubject {
    void registerObserver(LocationObserver o);
    void removeObserver(LocationObserver o);
    void notifyObservers(String s);
}
