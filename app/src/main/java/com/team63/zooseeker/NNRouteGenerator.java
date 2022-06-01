package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NNRouteGenerator implements RouteGenerator {
    private Graph<String, IdentifiedWeightedEdge> G;
    private List<GraphPath<String, IdentifiedWeightedEdge>> route;
    private String entrance;
    private String exit;
    private Collection<String> exhibits;

    public NNRouteGenerator() {
        this.G = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);
        this.route = new ArrayList<>();
        // defined by ID instead of tags
        this.entrance = "entrance_exit_gate";
        this.exit = this.entrance; // by default, map it
        this.exhibits = new ArrayList<>();
    }

    public RouteGenerator setG(Graph<String, IdentifiedWeightedEdge> G) {
        this.G = G;
        return this;
    }

    public RouteGenerator setEntrance (String entrance) {
        this.entrance = entrance;
        return this;
    }

    public RouteGenerator setExit (String exit) {
        this.exit = exit;
        return this;
    }

    public RouteGenerator setExhibits(Collection<String> ids) {
        exhibits.clear();
        exhibits.addAll(ids);
        return this;
    }

    public List<GraphPath<String, IdentifiedWeightedEdge>> getRoute() {
        calculateRoute();
        return route;
    }

    public RouteGenerator clear() {
        exhibits.clear();
        route.clear();
        return this;
    }

    private void calculateRoute() {
        HashSet<String> exhibitSet = new HashSet<>(exhibits);
        route = new ArrayList<>();
        // v is defined in the constructor, uses ID instead of tags
        String v = entrance;

        // from entrance to last exhibit
        while (true) { // we keep repeating until we break (which happens when set is empty_
            exhibitSet.remove(v);
            if (exhibitSet.isEmpty()) break;
            // keep getting nearest neighbor (the greedy approach)
            GraphPath<String, IdentifiedWeightedEdge> nextPath =
                    pathToClosestExhibit(v, exhibitSet);
            route.add(nextPath);
            v = nextPath.getEndVertex(); // move to next vertex
        }
        route.add(DijkstraShortestPath.findPathBetween(G, v, exit)); // from last exhibit to exit
    }

    /* pathToClosestExhibit is a helper method that, given a source vertex and a list of targets,
     * returns the path to the target closest to the source vertex.
     */
    private GraphPath<String, IdentifiedWeightedEdge> pathToClosestExhibit(String source, Set<String> targets) {
        DijkstraManyToManyShortestPaths<String, IdentifiedWeightedEdge> shortestPathFinder =
                new DijkstraManyToManyShortestPaths<>(G);

        // to use the getManyToManyPaths method, we have to put the source string into a set
        HashSet<String> sourceSet = new HashSet<>();
        sourceSet.add(source);

        // we give our DijkstraManyToManyShortestPaths (from jGraphT) 2 parameters:
        // 1: sourceSet, which is a HashSet<String> of JUST THE SOURCE VERTEX
        // 2: targets, which is a Set<String> of all the other potential targets.
        // Then, jGraphT "Computes shortest paths from all vertices in sources
        // to all vertices in targets."
        // Remember, there is only one vertex in sources, the source.
        // jGraphT returns the computed shortest paths as
        // MTMSPA.MTMSP<String, IdentifiedWeightedEdge>
        // This we can use later to greedily select the best IdentifiedWeightedEdge.
        ManyToManyShortestPathsAlgorithm.ManyToManyShortestPaths<String, IdentifiedWeightedEdge>
                paths = shortestPathFinder.getManyToManyPaths(sourceSet, targets);

        double minDistance = Double.MAX_VALUE;
        GraphPath<String, IdentifiedWeightedEdge> minPath = null;
        // we loop over all targets, and find the one that is closest
        for (String target : targets) {
            GraphPath<String, IdentifiedWeightedEdge> path = paths.getPath(source, target);
            if (path.getWeight() < minDistance) {
                minPath = path;
                minDistance = path.getWeight();
            }
        }
        // Now we can greedily select the best IdentifiedWeightedEdge.
        return minPath;
    }
}
