package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RerouteOperation implements DirectionsOperation {
    int directionInd;
    String newLocation;

    public RerouteOperation(int directionInd, String newLocation) {
        this.directionInd = directionInd;
        this.newLocation = newLocation;
    }

    @Override
    public List<Direction> operate(List<Direction> directions,
                                   Map<String, ZooData.VertexInfo> vInfoMap,
                                   Map<String, ZooData.EdgeInfo> eInfoMap,
                                   RouteGenerator routeGen,
                                   Graph<String, IdentifiedWeightedEdge> G,
                                   String entranceExit) {
        List<String> vertexSubset = new ArrayList<>(); // set of exhibits that need rerouting
        for (int j = directionInd; j < directions.size(); j++) {
            vertexSubset.add(directions.get(j).directionInfo.endVertexId);
        }
        vertexSubset.remove(vertexSubset.size()-1); // remove the gate (off-by-one error)
        List<GraphPath<String, IdentifiedWeightedEdge>> recalculatedRouteSection = routeGen
                .setG(G)
                .setEntrance(newLocation)
                .setExit(entranceExit)
                .setExhibits(vertexSubset)
                .getRoute();

        List<Direction> newDirections = directions.subList(0, directionInd);
        for (GraphPath<String, IdentifiedWeightedEdge> path : recalculatedRouteSection) {
            Direction direction = new Direction(path, vInfoMap, eInfoMap);
            newDirections.add(direction);
        }

        return newDirections;
    }
}
