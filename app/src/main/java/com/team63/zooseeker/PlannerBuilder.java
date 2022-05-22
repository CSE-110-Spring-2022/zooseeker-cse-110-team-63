package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphType;
import org.jgrapht.generate.EmptyGraphGenerator;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PlannerBuilder {
    private Map<String, ZooData.VertexInfo> vInfoMap;
    private Map<String, ZooData.EdgeInfo> eInfoMap;
    private RouteGenerator routeGen;
    private Graph <String, IdentifiedWeightedEdge> G;

    public PlannerBuilder setVInfoMap(Map<String, ZooData.VertexInfo> vInfoMap) {
        this.vInfoMap = vInfoMap;
        return this;
    }

    public PlannerBuilder setEInfoMap(Map<String, ZooData.EdgeInfo> eInfoMap) {
        this.eInfoMap = eInfoMap;
        return this;
    }

    public PlannerBuilder setRouteGenerator(RouteGenerator routeGen) {
        this.routeGen = routeGen;
        return this;
    }

    public PlannerBuilder setG(Graph<String, IdentifiedWeightedEdge> g) {
        G = g;
        return this;
    }

    public PlannerBuilder() {
        vInfoMap = new HashMap<>();
        eInfoMap = new HashMap<>();
        routeGen = new NNRouteGenerator();
        G = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);
    }
    
    public Planner make() {
        return (new Planner(routeGen, vInfoMap, eInfoMap, G));
    }
}
