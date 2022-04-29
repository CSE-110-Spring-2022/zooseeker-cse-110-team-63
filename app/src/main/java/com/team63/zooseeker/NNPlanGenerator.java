package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: WRITE UNIT TESTS FOR THIS CLASS
public class NNPlanGenerator extends PlanGenerator {
    public NNPlanGenerator(Graph<String, IdentifiedWeightedEdge> G) {
        super(G);
    }

    @Override
    public List<GraphPath<String, IdentifiedWeightedEdge>> getPlan(String entrance, Collection<String> exhibits, String exit) {
        if (exhibits.size() == 0) {
            return new ArrayList<>();
        }
        HashSet<String> exhibitSet = new HashSet<>(exhibits);
        ArrayList<GraphPath<String, IdentifiedWeightedEdge>> plan =
                new ArrayList<>();
        String v = entrance;
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
        plan.add(DijkstraShortestPath.findPathBetween(G, v, exit)); // from last exhibit to exit
        return plan;
    }

    private GraphPath<String, IdentifiedWeightedEdge> pathToClosestExhibit(String source, Set<String> targets) {
        DijkstraManyToManyShortestPaths<String, IdentifiedWeightedEdge> shortestPathFinder =
                new DijkstraManyToManyShortestPaths<>(G);
        HashSet<String> sourceSet = new HashSet<>();
        sourceSet.add(source);
        ManyToManyShortestPathsAlgorithm.ManyToManyShortestPaths<String, IdentifiedWeightedEdge>
                paths = shortestPathFinder.getManyToManyPaths(sourceSet, targets);
        double minDistance = Double.MAX_VALUE;
        GraphPath<String, IdentifiedWeightedEdge> minPath = null;
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
