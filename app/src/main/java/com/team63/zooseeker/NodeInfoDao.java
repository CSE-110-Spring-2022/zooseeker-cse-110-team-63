package com.team63.zooseeker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NodeInfoDao {
    @Insert
    List<Long> insertAll(List<NodeInfo> nodeInfo);

    @Query("SELECT `name` FROM `node_info` ORDER BY `name`")
    String[] getExhibit();
}
