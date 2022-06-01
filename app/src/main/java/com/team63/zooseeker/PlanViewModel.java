package com.team63.zooseeker;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jgrapht.Graph;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
        directionDao.setDirections(planner
                .performOperation(new PlanExhibitsOperation(new HashSet(nodeInfoDao.getSelectedExhibitIds())))
                .getDirections());
    }

    public void skip(int directionInd) {
        PlannerBuilder plannerBuilder = new PlannerBuilder();

        Planner planner = plannerBuilder.setRouteGenerator(new NNRouteGenerator())
                .setEInfoMap(eInfoMap)
                .setVInfoMap(vInfoMap)
                .setG(G)
                .make();

        directionDao.setDirections(planner.setDirections(directionDao.getDirectionsSync())
                .performOperation(new SkipOperation(directionInd))
                .getDirections());
    }

    public void replan(int directionInd, String exhibitId){
        PlannerBuilder plannerBuilder = new PlannerBuilder();

        Planner planner = plannerBuilder.setRouteGenerator(new NNRouteGenerator())
                .setEInfoMap(eInfoMap)
                .setVInfoMap(vInfoMap)
                .setG(G)
                .make();

        directionDao.setDirections(planner.setDirections(directionDao.getDirectionsSync())
                .performOperation(new RerouteOperation(directionInd, exhibitId))
                .getDirections());
    }

    public void selectItem(NodeInfo nodeInfo) {
        nodeInfo.selected = true;
        nodeInfoDao.update(nodeInfo);
    }

    public void resetId() {
        nodeInfoDao.resetOrderAndSelect();
    }

    public void setDetailedDir(boolean isChecked){
        PlannerBuilder plannerBuilder = new PlannerBuilder();
        StepRenderer stepRenderer;

        Planner planner = plannerBuilder.setRouteGenerator(new NNRouteGenerator())
                .setEInfoMap(eInfoMap)
                .setVInfoMap(vInfoMap)
                .setG(G)
                .make();

        if (isChecked) {stepRenderer = new DetailedStepRenderer();}
        else {stepRenderer = new BigStepRenderer();}
        List<Direction> newDirections = planner.setDirections(directionDao.getDirectionsSync())
                .setStepRenderer(stepRenderer)
                .performOperation(new ReloadStepsOperation())
                .getDirections();
        directionDao.setDirections(newDirections);
    }

    public String getLocationNameById(String id) {
        return vInfoMap.get(id).name;
    }

    public BasicLocationSubject getManualLocationSubject(){
        return new BasicLocationSubject(vInfoMap);
    }

    public GPSLocationSubject getGPSLocationSubject(LocationManager locationManager){
        return new GPSLocationSubject(locationManager, vInfoMap);
    }
}
