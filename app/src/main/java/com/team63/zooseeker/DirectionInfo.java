package com.team63.zooseeker;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.Map;

@Entity (tableName = "direction_info")
public class DirectionInfo {
    @PrimaryKey (autoGenerate = true)
    public long id;

    @NonNull
    public String name; // name of destination
    public Double distance;

    @Ignore
    public int cumDistance; // for convenience, in PlanItemAdapter

    public int order;

    public DirectionInfo(String name, Double distance) {
        this.name = name;
        this.distance = distance;
    }
}
