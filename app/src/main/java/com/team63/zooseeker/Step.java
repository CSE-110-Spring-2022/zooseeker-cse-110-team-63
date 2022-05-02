package com.team63.zooseeker;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Locale;

// TODO: Turn this into a database thing
public class Step implements Serializable { // a single step, a single direction is composed of these
    public static final String STEP_TEMPLATE = "Proceed on %s %d ft towards %s";
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

    protected Step(Parcel in) {
        distance = in.readDouble();
        street = in.readString();
        destination = in.readString();
    }

    @Override
    public String toString() {
        return String.format(Locale.US, STEP_TEMPLATE,
                this.street,
                roundDistance(this.distance),
                this.destination
        );
    }

    // helper method that rounds distance to 1 sig fig
    static int roundDistance(double d) {
        int exponent = 0;
        while (d >= 10) {
            exponent++;
            d /= 10;
        }
        int roundedD = (int) d;
        for (int i = 0; i < exponent; i++) {
            roundedD *= 10;
        }
        return roundedD;
    }
}