package com.team63.zooseeker;

import android.app.Application;
import android.content.Context;

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

    private LiveData<List<NodeInfo>> liveExhibits;
    private LiveData<List<NodeInfo>> liveSelectedExhibits;
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

    public LiveData<List<NodeInfo>> getSelectedExhibits() {
        if (liveSelectedExhibits == null) {
            liveSelectedExhibits = nodeInfoDao.getSelectedExhibits();
        }
        return liveSelectedExhibits;
    }

    public LiveData<List<NodeInfo>> getExhibits() {
        if (liveExhibits == null) {
            liveExhibits = nodeInfoDao.getExhibits();
        }
        return liveExhibits;
    }

    public LiveData<List<Direction>> getDirections() {
        if (liveDirections == null) {
            liveDirections = directionDao.getDirections();
        }
        return liveDirections;
    }

    public void generateDirections(Graph<String, IdentifiedWeightedEdge> G,
                                   Map<String, ZooData.VertexInfo> vInfoMap,
                                   Map<String, ZooData.EdgeInfo> eInfoMap)
    {
        directionDao.deleteAllDirectionInfos();
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

    public void selectItem(NodeInfo nodeInfo) {
        nodeInfo.selected = true;
        nodeInfoDao.update(nodeInfo);
    }
}
