package com.team63.zooseeker;

import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                                   String entranceExit, StepRenderer stepRenderer) {
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

        for (int i = 0; i < recalculatedRouteSection.size(); i++) {
            GraphPath<String, IdentifiedWeightedEdge> path = recalculatedRouteSection.get(i);
            Direction direction = new Direction(path, vInfoMap, eInfoMap, stepRenderer);
            // make destination of final step match the destination of old directions list
            for (int j = directionInd; j < directions.size(); j++) {
                if (directions.get(j).directionInfo.endVertexId.equals(path.getEndVertex())) {
                    Log.d("ZooSeeker,RerouteOperation", "Matching endVertexId found in old directions");
                    String destination = directions.get(j).steps.get(
                            directions.get(j).steps.size() - 1).destination;
                    direction.steps.get(direction.steps.size() - 1).destination = destination;
                    break;
                }
            }
            newDirections.add(direction);
        }
        return newDirections;
    }
}
