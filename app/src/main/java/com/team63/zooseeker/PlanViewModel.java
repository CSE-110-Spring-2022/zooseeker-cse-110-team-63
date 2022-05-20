package com.team63.zooseeker;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlanViewModel extends AndroidViewModel {
    private Graph<String, IdentifiedWeightedEdge> G;
    private Map<String, ZooData.VertexInfo> vInfoMap;
    private Map<String, ZooData.EdgeInfo> eInfoMap;

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
        SharedPreferences preferences = application.getSharedPreferences("filenames", MODE_PRIVATE);
        G = ZooData.loadZooGraphJSON(application.getApplicationContext(),
                        preferences.getString("zoo_graph", "fail"));
        vInfoMap = ZooData.loadVertexInfoJSON(application.getApplicationContext(),
                preferences.getString("vertex_info", "fail"));
        eInfoMap = ZooData.loadEdgeInfoJSON(application.getApplicationContext(),
                preferences.getString("edge_info", "fail"));
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

    public void generateDirections()
    {
        PlannerBuilder plannerBuilder = new PlannerBuilder();
        Planner planner = plannerBuilder.setRouteGenerator(new NNRouteGenerator())
                .setVInfoMap(vInfoMap).setEInfoMap(eInfoMap)
                .setG(G)
                .make();
        directionDao.insertDirections(planner
                .planExhibits(new HashSet(nodeInfoDao.getSelectedExhibitIds()),
                        nodeInfoDao.getGates().get(0).id)
                .getDirections());
    }

    public void recalculate(int directionInd) {
        PlannerBuilder plannerBuilder = new PlannerBuilder();

        Planner planner = plannerBuilder.setRouteGenerator(new NNRouteGenerator())
                .setEInfoMap(eInfoMap)
                .setVInfoMap(vInfoMap)
                .setG(G)
                .make();

        directionDao.insertDirections(planner.setDirections(directionDao.getDirectionsSync())
                .skip(directionInd)
                .getDirections());
    }

    public void selectItem(NodeInfo nodeInfo) {
        nodeInfo.selected = true;
        nodeInfoDao.update(nodeInfo);
    }
}
