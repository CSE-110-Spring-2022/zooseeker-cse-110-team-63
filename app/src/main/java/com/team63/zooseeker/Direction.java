package com.team63.zooseeker;

import static com.team63.zooseeker.Step.roundDistance;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* this implementation of IDirection takes in a GraphPath in the List returned by
 * PlanGenerator.getPath in the constructor, and uses it to construct the path
 */
public class Direction implements Parcelable{
    public long id = 0;
    public String name; // name of destination
    public Double distance;
    // public List<String> stepStrings;
    public List<Step> steps;
    public int order;


    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private GraphPath<String, IdentifiedWeightedEdge> path;
    private Graph<String, IdentifiedWeightedEdge> G;


    public Direction(String name, Double distance, /*, List<String> stepStrings*/ int order) {
        this.name = name;
        this.distance = distance;
        // this.stepStrings = stepStrings;
        this.order = order;
    }

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
        this.name = Objects.requireNonNull(vInfo.get(path.getEndVertex())).name;
        this.distance = path.getWeight();
        this.steps = computeSteps();
        // this.stepStrings = getTextDirection();
    }

    protected Direction(Parcel in) {
        id = in.readLong();
        name = in.readString();
        if (in.readByte() == 0) {
            distance = null;
        } else {
            distance = in.readDouble();
        }
        order = in.readInt();
    }

    public static final Creator<Direction> CREATOR = new Creator<Direction>() {
        @Override
        public Direction createFromParcel(Parcel in) {
            return new Direction(in);
        }

        @Override
        public Direction[] newArray(int size) {
            return new Direction[size];
        }
    };

    public int getDistance() {
        return roundDistance(distance);
    }


    public List<String> getTextDirection() {
        ArrayList<String> textDirectionList = new ArrayList<>();
        for (Step step : steps) {
            textDirectionList.add(step.toString());
        }
        return textDirectionList;
    }

//    private List<Step> getSteps() {
//        return steps;
//    }


    private List<Step> computeSteps() {
        ArrayList<Step> stepList = new ArrayList<>();
        Step step = new Step();
        List<IdentifiedWeightedEdge> pathEdges = path.getEdgeList();
        List<String> pathVertices = path.getVertexList();
        for (int i = 0; i < pathEdges.size(); i++) {
            IdentifiedWeightedEdge currEdge = pathEdges.get(i);
            if (step.street == null) { // if step is fresh, we provide a street for it
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
            }
        }
        return stepList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        if (distance == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(distance);
        }
        parcel.writeInt(order);
    }
}
