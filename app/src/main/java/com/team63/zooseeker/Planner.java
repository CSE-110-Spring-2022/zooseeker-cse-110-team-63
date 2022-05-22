package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


// TODO: Add PlannerBuilder class to hide defaults, perhaps
// TODO: Unit-testing for Planner planExhibits and planDirections
public class Planner {
    private final Map<String, ZooData.VertexInfo> vInfoMap;
    private final Map<String, ZooData.EdgeInfo> eInfoMap;
    private final RouteGenerator routeGen;
    private final Graph <String, IdentifiedWeightedEdge> G;
    private String entranceExit;
    private List<Direction> directions;

    public Planner(RouteGenerator routeGen,
                Map<String, ZooData.VertexInfo> vInfoMap,
                Map<String, ZooData.EdgeInfo> eInfoMap,
                   Graph<String, IdentifiedWeightedEdge> G) {
        this.routeGen = routeGen;
        this.vInfoMap = vInfoMap;
        this.eInfoMap = eInfoMap;
        for (Map.Entry<String, ZooData.VertexInfo> entry : vInfoMap.entrySet()) {
            if (entry.getValue().kind == ZooData.VertexInfo.Kind.GATE) {
                this.entranceExit = entry.getKey();
                break;
            }
        }
        this.G = G;
        this.directions = new ArrayList<>();
    }

    public Planner planExhibits(Collection<String> exhibits) {
        Set<String> ids = new HashSet(exhibits);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = routeGen
                .setG(G)
                .setEntrance(entranceExit)
                .setExit(entranceExit)
                .setExhibits(ids)
                .getRoute();
        directions.clear();
        for (GraphPath<String, IdentifiedWeightedEdge> path : paths) {
            Direction direction = new Direction(path, vInfoMap, eInfoMap);
            directions.add(direction);
        }
        return this;
    }

    public List<Direction> getDirections() {
        for (int i = 0; i < directions.size(); i++) {
            directions.get(i).directionInfo.order = i;
        }
        return new ArrayList<>(directions);
    }

    public Planner setDirections(List<Direction> directions) {
        this.directions.clear();
        this.directions.addAll(directions);
        return this;
    }

    public Planner skip(int i) {
        List<String> vertexSubset = new ArrayList<>(); // remaining vertex ids after skip
        for (int j = i+1; j < directions.size(); j++) {
            vertexSubset.add(directions.get(j).directionInfo.endVertexId);
        }
        vertexSubset.remove(vertexSubset.size()-1); // remove the gate (off-by-one error)
        List<GraphPath<String, IdentifiedWeightedEdge>> recalculatedRouteSection = routeGen
                .setG(G)
                .setEntrance(directions.get(i).directionInfo.startVertexId)
                .setExit(entranceExit)
                .setExhibits(vertexSubset)
                .getRoute();

        List<Direction> newDirections = directions.subList(0, i);
        for (GraphPath<String, IdentifiedWeightedEdge> path : recalculatedRouteSection) {
            Direction direction = new Direction(path, vInfoMap, eInfoMap);
            newDirections.add(direction);
        }
        directions = newDirections;
        return this;
    }
}
