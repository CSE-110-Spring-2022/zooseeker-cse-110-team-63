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
  
    @Query("SELECT * FROM `node_info` WHERE `kind`='gate'")
    List<NodeInfo> getGates();

    @Update
    int update(NodeInfo nodeInfo);

    @Delete
    void delete(NodeInfo nodeInfo);

    @Query("SELECT * FROM `node_info` WHERE `orderInPlan`=:i")
    List<NodeInfo> getNodeInfosByOrder(int i);

    @Query("DELETE FROM `node_info`")
    void deleteAllNodeInfos();
  
    @Query ("UPDATE `node_info` SET `orderInPlan`=-1, `selected`=0")
    void resetOrderAndSelect();

}
