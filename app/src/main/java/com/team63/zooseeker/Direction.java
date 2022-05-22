package com.team63.zooseeker;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* this implementation of IDirection takes in a GraphPath in the List returned by
 * PlanGenerator.getPath in the constructor, and uses it to construct the pathVie
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
                path.getStartVertex(),
                path.getEndVertex(),
                Objects.requireNonNull(vInfo.get(path.getEndVertex())).name,
                path.getWeight()
                );
        this.steps = computeSteps();
        // this.stepStrings = getTextDirection();
    }

    /*
    private List<String> getTextDirection() {
        ArrayList<String> textDirectionList = new ArrayList<>();
        for (Step step : steps) {
            textDirectionList.add(step.toString());
        }
        return textDirectionList;
    }
    */
    public List<Step> getSteps() {
        return steps;
    }


    private List<Step> computeSteps() {
        ArrayList<Step> stepList = new ArrayList<>();
        Step step = new Step();
        List<IdentifiedWeightedEdge> pathEdges = path.getEdgeList();
        List<String> pathVertices = path.getVertexList();
        int stepCount = 0;
        // TODO: refactor the logic in here
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
                // if we ended the step because we reached the end of the GraphPath
                if (i == pathEdges.size() - 1) {
                    // we mark the end destination of the step
                    step.destination = vInfo.get(path.getEndVertex()).name;
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
                    if (vInfo.get(realTarget).kind == ZooData.VertexInfo.Kind.INTERSECTION) {
                        // set the destination to the next street
                        step.destination = eInfo.get(pathEdges.get(i+1).getId()).street;
                    }
                    // if we ended the step because we have to change street next on an exhibit
                    else {
                        // we set destination to the "real" target vertex of this edge
                        step.destination = vInfo.get(realTarget).name;
                    }
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
