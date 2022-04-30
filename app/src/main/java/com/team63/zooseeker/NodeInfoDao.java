package com.team63.zooseeker;

import androidx.room.Query;

import java.util.List;

public interface NodeInfoDao {
    @Query("SELECT `name` FROM `node_info` ORDER BY `name`")
    List<String> getExhibit();
}
