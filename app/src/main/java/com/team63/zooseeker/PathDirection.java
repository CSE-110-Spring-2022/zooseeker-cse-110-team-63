package com.team63.zooseeker;

import android.content.Context;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/* this implementation of IDirection takes in a GraphPath in the List returned by
 * PlanGenerator.getPath in the constructor, and uses it to construct the path
 */
public class PathDirection implements IDirection {
    public static final String STEP_TEMPLATE = "%d. %s on %s %d ft towards %s";
    public static final String FIRST_LAST_VERB = "Proceed";
    public static final String DEFAULT_VERB = "Continue";
    String name; // name of destination
    Double distance;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;
    GraphPath<String, IdentifiedWeightedEdge> path;

    // path is the path from one exhibit to another.
    // vInfoFile is a String of the filename for the node_info JSON file in the assets directory
    // eInfoFile is same as vInfoFile, but for the edge_info JSON file instead
    public PathDirection(GraphPath<String, IdentifiedWeightedEdge> path, Context context, String vInfoFile, String eInfoFile) {
        // code for setting fields adapted from App.java in the ZooSeeker assets
        vInfo = ZooData.loadVertexInfoJSON(context, vInfoFile);
        eInfo = ZooData.loadEdgeInfoJSON(context, eInfoFile);
        this.path = path;
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
    public String getTextDirection() {
        Graph<String, IdentifiedWeightedEdge> G = path.getGraph();
        StringBuilder textDirectionBuilder = new StringBuilder();
        int i = 1;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            //
            String verb =
                    (i == 1 || i == path.getEdgeList().size()) ? FIRST_LAST_VERB : DEFAULT_VERB;
            // if it's an intersection, make nextTarget the street name
            String whereTo;
            if (Objects.requireNonNull(vInfo.get(G.getEdgeTarget(e))).kind ==
                    ZooData.VertexInfo.Kind.INTERSECTION) {
                IdentifiedWeightedEdge nextEdge = path.getEdgeList().get(i); // next step's edge
                whereTo = Objects.requireNonNull(eInfo.get(nextEdge.getId())).street;
                // the street we "arrive" at
            }
            else {
                whereTo = Objects.requireNonNull(vInfo.get(G.getEdgeTarget(e))).name;
            }
            String step = String.format(Locale.US, STEP_TEMPLATE,
                    i,
                    verb,
                    Objects.requireNonNull(eInfo.get(e.getId())).street,
                    roundDistance(G.getEdgeWeight(e)),
                    whereTo
                    );
            textDirectionBuilder = textDirectionBuilder.append(step);
            textDirectionBuilder.append('\n');
            i++;
        }
        return textDirectionBuilder.toString();
    }

    // helper method that rounds distance to 1 sig fig
    private int roundDistance(double d) {
        int exponent = 0;
        while (d >= 10) {
            exponent++;
            d /= 10;
        }
        int roundedD = (int) d;
        for (int i = 0; i < d; i++) {
            roundedD *= 10;
        }
        return roundedD;
    }
}
