package com.team63.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// https://stackoverflow.com/questions/44667160/android-room-insert-relation-entities-using-room
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

    @Query("SELECT * from `direction_info` ORDER BY `order`")
    abstract LiveData<List<Direction>> getDirectionsLive();

    @Query("DELETE FROM `direction_info`")
    abstract void deleteAllDirections();

    @Query("DELETE FROM `step`")
    abstract void deleteAllSteps();
}
