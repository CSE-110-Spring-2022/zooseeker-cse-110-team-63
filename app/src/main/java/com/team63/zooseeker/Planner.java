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
public class Planner {
    private List<Direction> directions;
    private Map<String, ZooData.VertexInfo> vInfoMap;
    private Map<String, ZooData.EdgeInfo> eInfoMap;
    private RouteGenerator routeGen;
    private String entranceExit;
    private Graph <String, IdentifiedWeightedEdge> G;

    public Planner(RouteGenerator routeGen, String entranceExit,
                Map<String, ZooData.VertexInfo> vInfoMap,
                Map<String, ZooData.EdgeInfo> eInfoMap,
                   Graph<String, IdentifiedWeightedEdge> G) {
        this.routeGen = routeGen;
        this.entranceExit = entranceExit;
        this.vInfoMap = vInfoMap;
        this.eInfoMap = eInfoMap;
        this.G = G;
    }

    public Planner planExhibits(Collection<String> exhibits) {
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
        routeGen.clear();
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
