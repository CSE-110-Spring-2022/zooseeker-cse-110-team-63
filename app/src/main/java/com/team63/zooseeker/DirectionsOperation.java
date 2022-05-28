package com.team63.zooseeker;

import org.jgrapht.Graph;

import java.util.List;
import java.util.Map;

public interface DirectionsOperation {
    List<Direction> operate(List<Direction> directions,
                            Map<String, ZooData.VertexInfo> vInfoMap,
                            Map<String, ZooData.EdgeInfo> eInfoMap,
                            RouteGenerator routeGen,
                            Graph<String, IdentifiedWeightedEdge> G,
                            String entranceExit, StepRenderer stepRenderer);
}
