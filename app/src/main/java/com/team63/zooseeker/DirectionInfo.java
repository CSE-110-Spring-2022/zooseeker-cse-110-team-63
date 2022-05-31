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
    public String startVertexId;

    @NonNull
    public String endVertexId;

    @NonNull
    public String name; // name of destination
    public String startName; // name of location tourist is starting from
    public Double distance;

    @Ignore
    public int cumDistance; // for convenience, in PlanItemAdapter

    public int order;

    public DirectionInfo(String startVertexId, String endVertexId, String startName, String name, Double distance) {
        this.startVertexId = startVertexId;
        this.endVertexId = endVertexId;
        this.startName = startName;
        this.name = name;
        this.distance = distance;
    }
}
