package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SkipOperation implements DirectionsOperation {
    int directionInd;

    public SkipOperation(int directionInd) {
        this.directionInd = directionInd;
    }

    @Override
    public List<Direction> operate(List<Direction> directions,
                                   Map<String, ZooData.VertexInfo> vInfoMap,
                                   Map<String, ZooData.EdgeInfo> eInfoMap,
                                   RouteGenerator routeGen,
                                   Graph<String, IdentifiedWeightedEdge> G,
                                   String entranceExit, StepRenderer stepRenderer) {
        List<String> vertexSubset = new ArrayList<>(); // remaining vertex ids after skip
        for (int j = directionInd + 1; j < directions.size(); j++) {
            vertexSubset.add(directions.get(j).directionInfo.endVertexId);
        }
        vertexSubset.remove(vertexSubset.size()-1); // remove the gate (off-by-one error)
        List<GraphPath<String, IdentifiedWeightedEdge>> recalculatedRouteSection = routeGen
                .setG(G)
                .setEntrance(directions.get(directionInd).directionInfo.startVertexId)
                .setExit(entranceExit)
                .setExhibits(vertexSubset)
                .getRoute();

        List<Direction> newDirections = directions.subList(0, directionInd);
        for (GraphPath<String, IdentifiedWeightedEdge> path : recalculatedRouteSection) {
            Direction direction = new Direction(path, vInfoMap, eInfoMap, stepRenderer);
            newDirections.add(direction);
        }

        return newDirections;
    }
}
