package com.team63.zooseeker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PrePlanViewModel extends AndroidViewModel {
    private LiveData<List<NodeInfo>> nodeInfoItems;
    private final NodeInfoDao nodeInfoDao;

    public PrePlanViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        NodeDatabase db = NodeDatabase.getSingleton(context);
        nodeInfoDao = db.nodeInfoDao();
    }

    public List<NodeInfo> getSelectedItems() {
        return nodeInfoDao.getSelectedExhibits();
    }

    public List<NodeInfo> getExhibits() {
        return nodeInfoDao.getExhibits();
    }

    public LiveData<List<NodeInfo>> getExhibitsLive() {
        return nodeInfoDao.getExhibitsLive();
    }

    public LiveData<List<NodeInfo>> getSelectedExhibitsLive() {
        return nodeInfoDao.getSelectedExhibitsLive();
    }

    public void toggleSelectItem(NodeInfo nodeInfo) {
        nodeInfo.selected = !nodeInfo.selected;
        nodeInfoDao.update(nodeInfo);
    }
}
