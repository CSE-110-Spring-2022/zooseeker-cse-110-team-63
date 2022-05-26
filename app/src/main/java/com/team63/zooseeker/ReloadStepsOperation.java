package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReloadStepsOperation implements DirectionsOperation {
    @Override
    public List<Direction> operate(List<Direction> directions,
                                   Map<String, ZooData.VertexInfo> vInfoMap,
                                   Map<String, ZooData.EdgeInfo> eInfoMap,
                                   RouteGenerator routeGen,
                                   Graph<String, IdentifiedWeightedEdge> G,
                                   String entranceExit,
                                   StepRenderer stepRenderer) {
        List<Direction> newDirections = new ArrayList<>();
        for (Direction direction : directions) {
            GraphPath<String, IdentifiedWeightedEdge> path = routeGen.setG(G)
                    .setEntrance(direction.directionInfo.startVertexId)
                    .setExit(direction.directionInfo.endVertexId)
                    .setExhibits(new ArrayList<>())
                    .getRoute()
                    .get(0);
            List<Step> newSteps = stepRenderer.getSteps(path, vInfoMap, eInfoMap, G);
            direction.steps.clear();
            direction.steps.addAll(newSteps);
            newDirections.add(direction);
        }
        return newDirections;
    }
}
