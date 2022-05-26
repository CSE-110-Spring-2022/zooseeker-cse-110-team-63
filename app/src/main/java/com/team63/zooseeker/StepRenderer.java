package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.Map;

public interface StepRenderer {
    public List<Step> getSteps(GraphPath<String, IdentifiedWeightedEdge> path,
                               Map<String, ZooData.VertexInfo> vInfoMap,
                               Map<String, ZooData.EdgeInfo> eInfoMap,
                               Graph<String, IdentifiedWeightedEdge> G);
}
