package com.team63.zooseeker;

import android.content.Context;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/* this implementation of IDirection takes in a GraphPath in the List returned by
 * PlanGenerator.getPath in the constructor, and uses it to construct the path
 */
public class PathDirection implements IDirection {
    public static final String STEP_TEMPLATE = "Proceed on %s %d ft towards %s";
    String name; // name of destination
    Double distance;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;
    GraphPath<String, IdentifiedWeightedEdge> path;
    Graph<String, IdentifiedWeightedEdge> G;

    public static class Step { // a single step, a single direction is composed of these
        public Step() {
            this.distance = 0;
            this.street = null;
            this.destination = null;
        }
        public double distance;
        String street;
        String destination;
    }

    // path is the path from one exhibit to another.
    // vInfoFile is a String of the filename for the node_info JSON file in the assets directory
    // eInfoFile is same as vInfoFile, but for the edge_info JSON file instead
    public PathDirection(GraphPath<String, IdentifiedWeightedEdge> path, Context context, String vInfoFile, String eInfoFile) {
        // code for setting fields adapted from App.java in the ZooSeeker assets
        vInfo = ZooData.loadVertexInfoJSON(context, vInfoFile);
        eInfo = ZooData.loadEdgeInfoJSON(context, eInfoFile);
        this.path = path;
        this.G = path.getGraph();
        this.name = Objects.requireNonNull(vInfo.get(path.getStartVertex())).name;
        this.distance = path.getWeight();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDistance() {
        return roundDistance(distance);
    }

    @Override
    public List<String> getTextDirection() {
        ArrayList<String> textDirectionList = new ArrayList<>();
        for (Step step : breakIntoSteps()) {
            String stepString = String.format(Locale.US, STEP_TEMPLATE,
                    step.street,
                    roundDistance(step.distance),
                    step.destination
                    );
            textDirectionList.add(stepString);
        }
        return textDirectionList;
    }

    public List<Step> breakIntoSteps() {
        ArrayList<Step> stepList = new ArrayList<>();
        Step step = new Step();
        List<IdentifiedWeightedEdge> pathEdges = path.getEdgeList();
        for (int i = 0; i < pathEdges.size(); i++) {
            IdentifiedWeightedEdge currEdge = pathEdges.get(i);
            if (step.street == null) { // if step is fresh, we provide a street for it
                step.street = eInfo.get(currEdge.getId()).street;
            }
            step.distance += G.getEdgeWeight(currEdge); // add edge distance to total step dist
            // if we reach the end of the GraphPath
            if (i == pathEdges.size() - 1
            // or if the edge ahead of us changes street
                    || (!step.street.equals(eInfo.get(pathEdges.get(i+1).getId()).street))) {
                step.destination = G.getEdgeTarget(currEdge);// we mark the end destination of the step
                stepList.add(step); // we "cut off" the step and put it in the list
                step = new Step(); // make step point to a new Step object
            }
        }
        return stepList;
    }

    // helper method that rounds distance to 1 sig fig
    private int roundDistance(double d) {
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
