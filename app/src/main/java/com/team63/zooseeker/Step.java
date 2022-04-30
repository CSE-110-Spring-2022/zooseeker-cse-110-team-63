package com.team63.zooseeker;
// TODO: Turn this into a database thing
public class Step { // a single step, a single direction is composed of these
    public double distance;
    public String street;
    public String destination;

    public Step() {
        this.distance = 0;
        this.street = null;
        this.destination = null;
    }

    public Step(double distance, String street, String destination) {
        this.distance = distance;
        this.street = street;
        this.destination = destination;
    }
}