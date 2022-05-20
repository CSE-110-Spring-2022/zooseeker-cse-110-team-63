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

// TODO: Create Plan class for easier unit testing
// TODO: Add constructor
// TODO: Refactor recalculate method into here
public class Plan {
    public List<Direction> directions;

    @Ignore
    Map<String, ZooData.VertexInfo> vInfoMap;

    @Ignore
    Map<String, ZooData.EdgeInfo> eInfoMap;

    @Ignore
    RouteGenerator routeGen;

    @Ignore
    String entranceExit;

    @Ignore
    Graph<String, IdentifiedWeightedEdge> G;
    public Plan(RouteGenerator routeGen, String entranceExit, Collection<String> exhibits,
                Map<String, ZooData.VertexInfo> vInfoMap,
                Map<String, ZooData.EdgeInfo> eInfoMap) {
        this.routeGen = routeGen;
        this.entranceExit = entranceExit;
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

    }

    public Direction getDirection(int i) {
        return directions.get(i);
    }

    public void skip(int i) {
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
    }
}
