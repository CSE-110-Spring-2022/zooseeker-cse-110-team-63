package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlanExhibitsOperation implements DirectionsOperation {
    Collection<String> exhibits;

    public PlanExhibitsOperation(Collection<String> exhibits) {
        this.exhibits = exhibits;
    }

    @Override
    public List<Direction> operate(List<Direction> directions,
                                   Map<String, ZooData.VertexInfo> vInfoMap,
                                   Map<String, ZooData.EdgeInfo> eInfoMap,
                                   RouteGenerator routeGen,
                                   Graph<String, IdentifiedWeightedEdge> G,
                                   String entranceExit, StepRenderer stepRenderer) {
        List<Direction> newDirections = new ArrayList<>(directions);
        Set<String> ids = new HashSet(exhibits);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = routeGen
                .setG(G)
                .setEntrance(entranceExit)
                .setExit(entranceExit)
                .setExhibits(ids)
                .getRoute();
        for (GraphPath<String, IdentifiedWeightedEdge> path : paths) {
            Direction direction = new Direction(path, vInfoMap, eInfoMap, stepRenderer);
            newDirections.add(direction);
        }
        return newDirections;
    }
}
