package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
        Set<String> ids = new HashSet<>();
        Map<String, List<String>> groups = new HashMap<>();
        for (String exhibit : exhibits) {
            // add exhibit ID itself if it has no group ID,
            // or else add its group ID (since it is represented on zoograph as group ID)
            if (vInfoMap.get(exhibit).groupId == null) {
                ids.add(exhibit);
            }
            else {
                // put it in groups map
                if (!groups.containsKey(vInfoMap.get(exhibit).groupId)) {
                    List<String> newBucket = new ArrayList<>();
                    groups.put(vInfoMap.get(exhibit).groupId, newBucket);
                }
                // put the group ID of the exhibit, not the exhibit itself
                // (since thta's how it's represented in the graph JSON file)
                groups.get(vInfoMap.get(exhibit).groupId).add(exhibit);
                ids.add(vInfoMap.get(exhibit).groupId);
            }
        }
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = routeGen
                .setG(G)
                .setEntrance(entranceExit)
                .setExit(entranceExit)
                .setExhibits(ids)
                .getRoute();
        for (GraphPath<String, IdentifiedWeightedEdge> path : paths) {
            Direction direction;
            if (vInfoMap.get(path.getEndVertex()).kind == ZooData.VertexInfo.Kind.EXHIBIT_GROUP) {
                direction = new Direction(path, groups.get(path.getEndVertex()), vInfoMap,
                        eInfoMap, stepRenderer);
            }
            else {
                direction = new Direction(path, vInfoMap, eInfoMap, stepRenderer);
            }
            newDirections.add(direction);
        }
        return newDirections;
    }
}
