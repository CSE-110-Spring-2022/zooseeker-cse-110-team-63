package com.team63.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NodeInfoDao {
    @Insert
    List<Long> insertAll(List<NodeInfo> nodeInfo);

    @Query("SELECT * FROM `node_info` ORDER BY `name`")
    List<NodeInfo> getExhibits();

    @Query("SELECT * FROM `node_info` WHERE `selected`")
    List<NodeInfo> getSelectedExhibits();

    @Query("SELECT * FROM `node_info`")
    LiveData<List<NodeInfo>> getExhibitsLive();

    @Query("SELECT * FROM `node_info` WHERE `selected`")
    LiveData<List<NodeInfo>> getSelectedExhibitsLive();

    @Update
    int update(NodeInfo nodeInfo);

}
