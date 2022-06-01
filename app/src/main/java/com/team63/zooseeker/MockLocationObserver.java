package com.team63.zooseeker;

import org.jgrapht.Graph;

import java.util.List;
import java.util.Map;

/**
 * Used for RerouteTest
 */
public class MockLocationObserver implements LocationObserver {

    List<Direction> directionsList;
    Graph<String, IdentifiedWeightedEdge> G;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;
    int directionInd;

    // should perform reroute
    @Override
    public void updateLocation(String nearestExhibit) {
        PlannerBuilder pb = new PlannerBuilder();
        Planner planner = pb.setG(G)
                .setVInfoMap(vInfo)
                .setEInfoMap(eInfo)
                .setRouteGenerator(new NNRouteGenerator())
                .make();

        directionsList = planner.setDirections(directionsList).performOperation(
                new RerouteOperation(directionInd, nearestExhibit)).getDirections();
    }

    public List<Direction> getDirectionsList() {
        return directionsList;
    }

    public MockLocationObserver(List<Direction> directionsList,
                                Graph<String, IdentifiedWeightedEdge> G,
            Map<String, ZooData.VertexInfo>vInfo,
            Map<String, ZooData.EdgeInfo> eInfo, int directionInd) {
        this.directionsList = directionsList;
        this.G = G;
        this.vInfo = vInfo;
        this.eInfo = eInfo;
        this.directionInd = directionInd;
    }
}
