package com.team63.zooseeker;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity(tableName="step", foreignKeys = {@ForeignKey(entity = DirectionInfo.class,
        parentColumns = "id",
        childColumns = "directionId",
        onDelete = ForeignKey.CASCADE)
})
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

    @Ignore
    public Step() {
        this.distance = 0;
        this.street = "";
        this.destination = "";
    }

    public Step(long directionId, double distance, int order, String street, String destination) {
        this.directionId= directionId;
        this.distance = distance;
        this.order = order;
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