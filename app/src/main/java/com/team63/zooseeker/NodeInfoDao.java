package com.team63.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NodeInfoDao {
    @Insert
    void insertAll(List<NodeInfo> nodeInfo);

    @Query("SELECT * FROM `node_info` WHERE `kind`='exhibit' ORDER BY `name`")
    LiveData<List<NodeInfo>> getExhibits();

    @Query("SELECT `id` FROM `node_info` WHERE `kind`='exhibit' AND `selected`")
    List<String> getSelectedExhibitIds();

    @Query("SELECT * FROM `node_info` WHERE `kind`='exhibit' AND `selected`")
    LiveData<List<NodeInfo>> getSelectedExhibits();

    @Update
    int update(NodeInfo nodeInfo);
}
