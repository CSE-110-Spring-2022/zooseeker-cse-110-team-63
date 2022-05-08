package com.team63.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

// Credit to
// https://stackoverflow.com/a/47112918
// for teaching me how to insert relation entities in Dao
@Dao
public abstract class DirectionDao {
    @Insert
    abstract void insertDirectionInfo(DirectionInfo directionInfo);

    @Insert
    public abstract void insertSteps(List<Step> step);

    public void insertDirection(Direction direction) {
        List<Step> steps = direction.steps;
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).directionId = direction.directionInfo.id;
            steps.get(i).order = i;
        }
        insertSteps(steps);
        insertDirectionInfo(direction.directionInfo);
    }

    public void insertDirections(List<Direction> directions) {
        for (int i = 0; i < directions.size(); i++) {
            Direction direction = directions.get(i);
            direction.directionInfo.order = i;
            insertDirection(direction);
        }
    }

    @Query("SELECT * from `direction_info` ORDER BY `order`")
    abstract List<Direction> getDirections();

    @Transaction
    @Query("SELECT * from `direction_info` ORDER BY `order`")
    abstract LiveData<List<Direction>> getDirectionsLive();

    @Query("DELETE FROM `direction_info`")
    abstract void deleteAllDirections();

    @Query("DELETE FROM `step`")
    abstract void deleteAllSteps();
}
