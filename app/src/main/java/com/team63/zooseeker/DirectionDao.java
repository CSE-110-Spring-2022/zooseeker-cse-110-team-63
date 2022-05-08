package com.team63.zooseeker;

import android.util.Log;

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
    abstract Long insertDirectionInfo(DirectionInfo directionInfo);

    @Insert
    public abstract void insertSteps(List<Step> step);

    public void insertDirection(Direction direction) {
        Long directionId = insertDirectionInfo(direction.directionInfo);
        List<Step> steps = direction.steps;
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).directionId = directionId;
            steps.get(i).order = i;
        }
        insertSteps(steps);
    }


    public void insertDirections(List<Direction> directions) {
        for (int i = 0; i < directions.size(); i++) {
            Direction direction = directions.get(i);
            direction.directionInfo.order = i;
            insertDirection(direction);
        }
    }

    @Transaction
    @Query("SELECT * from `direction_info` ORDER BY `order`")
    abstract LiveData<List<Direction>> getDirections();

    @Query("DELETE FROM `direction_info`")
    abstract void deleteAllDirectionInfos();

    @Query("DELETE FROM `step`")
    abstract void deleteAllSteps();
}
