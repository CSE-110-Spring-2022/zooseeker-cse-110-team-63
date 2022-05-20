package com.team63.zooseeker;

import androidx.room.Ignore;

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
    private List<Direction> directions;
    private Map<String, ZooData.VertexInfo> vInfoMap;
    private Map<String, ZooData.EdgeInfo> eInfoMap;
    private String entranceExit;
    private RouteGenerator routeGen;
    private Graph <String, IdentifiedWeightedEdge> G;

    public Planner(RouteGenerator routeGen,
                Map<String, ZooData.VertexInfo> vInfoMap,
                Map<String, ZooData.EdgeInfo> eInfoMap,
                   Graph<String, IdentifiedWeightedEdge> G,
                   List<Direction> directions) {
        this.routeGen = routeGen;
        this.vInfoMap = vInfoMap;
        this.eInfoMap = eInfoMap;
        for (Map.Entry<String, ZooData.VertexInfo> entry : vInfoMap.entrySet()) {
            if (entry.getValue().kind == ZooData.VertexInfo.Kind.GATE) {
                this.entranceExit = entry.getKey();
            }
        }
        this.G = G;
        this.directions = directions;
    }

    public Planner planExhibits(Collection<String> exhibits, String entranceExit) {
        Set<String> ids = new HashSet(exhibits);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = routeGen
                .setG(G)
                .setEntrance(entranceExit)
                .setExit(entranceExit)
                .addExhibits(ids)
                .getRoute();
        directions = new ArrayList<>();
        for (GraphPath<String, IdentifiedWeightedEdge> path : paths) {
            Direction direction = new Direction(path, vInfoMap, eInfoMap);
            directions.add(direction);
        }
        return this;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public Planner setDirections(List<Direction> directions) {
        this.directions = directions;
        return this;
    }

    public Planner skip(int i) {
        List<String> vertexSubset = new ArrayList<>(); // remaining vertex ids after skip
        for (Direction direction : directions) {
            if (direction.directionInfo.order > i) {
                vertexSubset.add(direction.directionInfo.endVertexId);
            }
        }
        vertexSubset.remove(vertexSubset.size()-1); // remove the gate (off-by-one error)
        List<GraphPath<String, IdentifiedWeightedEdge>> recalculatedRouteSection = routeGen
                .setG(G)
                .setEntrance(directions.get(i).directionInfo.startVertexId)
                .setExit(entranceExit)
                .addExhibits(vertexSubset)
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
