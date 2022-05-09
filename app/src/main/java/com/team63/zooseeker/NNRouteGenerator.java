package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NNRouteGenerator extends RouteGenerator {
    public NNRouteGenerator(Graph<String, IdentifiedWeightedEdge> G) {
        super(G);
    }

    @Override
    public List<GraphPath<String, IdentifiedWeightedEdge>> getRoute(String entranceExit, Collection<String> exhibits) {
        HashSet<String> exhibitSet = new HashSet<>(exhibits);
        ArrayList<GraphPath<String, IdentifiedWeightedEdge>> plan =
                new ArrayList<>();
        String v = entranceExit;
        // from entrance to last exhibit
        while (true) { // we keep repeating until we break (which happens when set is empty_
            exhibitSet.remove(v);
            if (exhibitSet.isEmpty()) break;
            // keep getting nearest neighbor (the greedy approach)
            GraphPath<String, IdentifiedWeightedEdge> nextPath =
                    pathToClosestExhibit(v, exhibitSet);
            plan.add(nextPath);
            v = nextPath.getEndVertex(); // move to next vertex
        }
        plan.add(DijkstraShortestPath.findPathBetween(G, v, entranceExit)); // from last exhibit to exit
        return plan;
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
        return minPath;
    }
}
