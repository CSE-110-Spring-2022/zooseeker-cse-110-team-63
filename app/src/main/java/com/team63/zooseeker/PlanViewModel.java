package com.team63.zooseeker;

import android.app.Application;
import android.content.Context;
import android.text.Layout;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanViewModel extends AndroidViewModel {
    private final String ENTRANCE_EXIT = "entrance_exit_gate";

    private LiveData<List<NodeInfo>> liveNodeInfos;
    private LiveData<List<NodeInfo>> liveSelectedNodeInfos;
    private LiveData<List<Direction>> liveDirections;
    private final NodeInfoDao nodeInfoDao;
    private final DirectionDao directionDao;

    public PlanViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        NodeDatabase db = NodeDatabase.getSingleton(context);
        nodeInfoDao = db.nodeInfoDao();
        directionDao = db.directionDao();
    }

    public List<NodeInfo> getSelectedItems() {
        return nodeInfoDao.getSelectedExhibits();
    }

    public List<NodeInfo> getExhibits() {
        return nodeInfoDao.getExhibits();
    }

    public LiveData<List<NodeInfo>> getExhibitsLive() {
        if (liveNodeInfos == null) {
            liveNodeInfos = nodeInfoDao.getExhibitsLive();
        }
        return liveNodeInfos;
    }

    public LiveData<List<NodeInfo>> getSelectedExhibitsLive() {
        if (liveSelectedNodeInfos == null) {
            liveSelectedNodeInfos = nodeInfoDao.getSelectedExhibitsLive();
        }
        return liveSelectedNodeInfos;
    }

    public LiveData<List<Direction>> getDirectionsLive() {
        if (liveDirections == null) {
            liveDirections = directionDao.getDirectionsLive();
        }
        return liveDirections;
    }

    public void generateDirections(Graph<String, IdentifiedWeightedEdge> G,
                                   Map<String, ZooData.VertexInfo> vInfoMap,
                                   Map<String, ZooData.EdgeInfo> eInfoMap)
    {
        directionDao.deleteAllDirections();
        directionDao.deleteAllSteps();
        RouteGenerator routeGen = new NNRouteGenerator(G);

        List<GraphPath<String, IdentifiedWeightedEdge>> paths
                = routeGen.getRoute(ENTRANCE_EXIT, nodeInfoDao.getSelectedExhibitIds());
        ArrayList<Direction> directions = new ArrayList<>();
        for (GraphPath<String, IdentifiedWeightedEdge> path : paths) {
            Direction direction = new Direction(path, vInfoMap, eInfoMap);
            directions.add(direction);
        }
        directionDao.insertDirections(directions);
    }

    public void toggleSelectItem(NodeInfo nodeInfo) {
        nodeInfo.selected = !nodeInfo.selected;
        nodeInfoDao.update(nodeInfo);
    }
}
