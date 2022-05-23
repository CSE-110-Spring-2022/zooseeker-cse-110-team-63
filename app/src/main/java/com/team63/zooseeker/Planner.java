package com.team63.zooseeker;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    public Planner performOperation(DirectionsOperation op) {
        List<Direction> directionsCopy = new ArrayList<> (directions); // prevent co-modification
        List<Direction> newDirections = op.operate(directionsCopy, vInfoMap, eInfoMap,
                routeGen, G, entranceExit);
        setDirections(newDirections);
        return this;
    }
}
