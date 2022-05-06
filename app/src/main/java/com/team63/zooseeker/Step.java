package com.team63.zooseeker;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

// TODO: Turn this into a database thing
@Entity(tableName="step")
public class Step { // a single step, a single direction is composed of these
    public static final String STEP_TEMPLATE = "Proceed on %s %d ft towards %s";

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long directionId;
    public double distance;
    public int order;

    @NonNull
    public String street;

    @NonNull
    public String destination;

    public Step() {
        this.distance = 0;
        this.street = "";
        this.destination = "";
    }

    public Step(double distance, String street, String destination) {
        this.distance = distance;
        this.street = street;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, STEP_TEMPLATE,
                this.street,
                Direction.roundDistance(this.distance),
                this.destination
        );
    }


}