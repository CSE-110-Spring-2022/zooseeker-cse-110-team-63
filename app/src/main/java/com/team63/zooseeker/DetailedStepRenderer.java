package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DetailedStepRenderer implements StepRenderer {
    @Override
    public List<Step> getSteps(GraphPath<String, IdentifiedWeightedEdge> path,
                               Map<String, ZooData.VertexInfo> vInfoMap,
                               Map<String, ZooData.EdgeInfo> eInfoMap) {
        ArrayList<Step> stepList = new ArrayList<>();
        Step step = new Step();
        List<IdentifiedWeightedEdge> pathEdges = path.getEdgeList();
        List<String> pathVertices = path.getVertexList();
        int stepCount = 0;
        Graph<String, IdentifiedWeightedEdge> G = path.getGraph();
        for (int i = 0; i < pathEdges.size(); i++) {
            IdentifiedWeightedEdge currEdge = pathEdges.get(i);
            if (step.street.equals("")) { // if step is fresh, we provide a street for it
                step.street = eInfoMap.get(currEdge.getId()).street;
            }
            step.distance += G.getEdgeWeight(currEdge); // add edge distance to total step dist
            // if we reached the end of the GraphPath
            if (i == pathEdges.size() - 1) {
                // we mark the end destination of the step
                step.destination = vInfoMap.get(path.getEndVertex()).name;
            }
            else {
                // find the real target (necessary bc of how JGraphT stores unordered edges)
                String realTarget = "";
                Set<String> currEdgeEnds = new HashSet<>(Arrays.asList(
                        G.getEdgeTarget(pathEdges.get(i)),
                        G.getEdgeSource(pathEdges.get(i))
                ));
                Set<String> nextEdgeEnds = new HashSet<>(Arrays.asList(
                        G.getEdgeTarget(pathEdges.get(i+1)),
                        G.getEdgeSource(pathEdges.get(i+1))
                ));
                // find the vertex in common with the current edge and
                // next edge to find the "true" destination of this edge
                for (String currEdgeEnd : currEdgeEnds) {
                    if (nextEdgeEnds.contains(currEdgeEnd)) {
                        realTarget = currEdgeEnd;
                        break;
                    }
                }

                // if we ended the step because we have to change street next on an intersection
                if (vInfoMap.get(realTarget).kind == ZooData.VertexInfo.Kind.INTERSECTION) {
                    // set the destination to the next street
                    Set<IdentifiedWeightedEdge> intersectingEdges = G.edgesOf(realTarget);
                    for (IdentifiedWeightedEdge edge : intersectingEdges) {
                        String candidateDestination = eInfoMap.get(edge.getId()).street;
                        if (!(candidateDestination.equals(step.street))) {
                            step.destination = candidateDestination;
                            break;
                        }
                    }
                }
                // if we ended the step because we have to change street next on an exhibit
                else {
                    // we set destination to the "real" target vertex of this edge
                    step.destination = vInfoMap.get(realTarget).name;
                }
            }
            stepList.add(step); // we "cut off" the step and put it in the list
            step = new Step(); // make step point to a new Step object
            stepCount++;
        }
        return stepList;
    }
}
