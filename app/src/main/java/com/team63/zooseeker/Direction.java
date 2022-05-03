package com.team63.zooseeker;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* this implementation of IDirection takes in a GraphPath in the List returned by
 * PlanGenerator.getPath in the constructor, and uses it to construct the path
 */
public class Direction {
    @Embedded DirectionInfo directionInfo;
    // public List<String> stepStrings;
    @Relation(
        parentColumn = "id",
        entityColumn = "directionId"
    )
    public List<Step> steps;

    @Ignore
    private Map<String, ZooData.VertexInfo> vInfo;

    @Ignore
    private Map<String, ZooData.EdgeInfo> eInfo;

    @Ignore
    private GraphPath<String, IdentifiedWeightedEdge> path;

    @Ignore
    private Graph<String, IdentifiedWeightedEdge> G;

    public Direction() {}

    /* path is the path from one exhibit to another.
     * vInfo is a Map of strings to ZooData.VertexInfo objects, that can be obtained by calling
     * the static ZooData.loadVertexInfoJSON method
     * eInfo is a Map of strings to ZooData.VertexInfo objects, that can be obtained by calling
     * the static ZooData.loadEdgeInfoJSON method
     */
    public Direction(GraphPath<String, IdentifiedWeightedEdge> path, Map<String, ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> eInfo) {
        // code for setting fields adapted from App.java in the ZooSeeker assets
        this.vInfo = vInfo;
        this.eInfo = eInfo;
        this.path = path;
        this.G = path.getGraph();
        this.directionInfo = new DirectionInfo(
                Objects.requireNonNull(vInfo.get(path.getEndVertex())).name,
                path.getWeight()
                );
        this.steps = computeSteps();
        // this.stepStrings = getTextDirection();
    }

    /*
    private List<String> getTextDirection() {
        ArrayList<String> textDirectionList = new ArrayList<>();
        for (Step step : getSteps()) {
            textDirectionList.add(step.getText());
        }
        return textDirectionList;
    }
    */

    private List<Step> computeSteps() {
        ArrayList<Step> stepList = new ArrayList<>();
        Step step = new Step();
        List<IdentifiedWeightedEdge> pathEdges = path.getEdgeList();
        List<String> pathVertices = path.getVertexList();
        int stepCount = 0;
        for (int i = 0; i < pathEdges.size(); i++) {
            IdentifiedWeightedEdge currEdge = pathEdges.get(i);
            if (step.street.equals("")) { // if step is fresh, we provide a street for it
                step.street = eInfo.get(currEdge.getId()).street;
            }
            step.distance += G.getEdgeWeight(currEdge); // add edge distance to total step dist
            // if we reach the end of the GraphPath
            if (i == pathEdges.size() - 1
            // or if the edge ahead of us changes street, we end the step.
                    || (!step.street.equals(eInfo.get(pathEdges.get(i+1).getId()).street))) {
                // if we ended the step because we have to change street next
                if (i < pathEdges.size() - 1 &&
                        vInfo.get(G.getEdgeTarget(currEdge)).kind == ZooData.VertexInfo.Kind.INTERSECTION) {
                    // set the destination to the next street
                    step.destination = eInfo.get(pathEdges.get(i+1).getId()).street;
                }
                // if we ended the step because we reached the end of the GraphPath
                else {
                    // we mark the end destination of the step
                    step.destination = vInfo.get(path.getEndVertex()).name;
                }
                stepList.add(step); // we "cut off" the step and put it in the list
                step = new Step(); // make step point to a new Step object
                stepCount++;
            }
        }
        return stepList;
    }

    // helper method that rounds distance to 1 sig fig
    static int roundDistance(double d) {
        int exponent = 0;
        while (d >= 10) {
            exponent++;
            d /= 10;
        }
        int roundedD = (int) d;
        for (int i = 0; i < exponent; i++) {
            roundedD *= 10;
        }
        return roundedD;
    }
}
