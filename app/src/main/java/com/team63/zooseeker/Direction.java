package com.team63.zooseeker;

import static java.util.stream.Collectors.toList;

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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

// looks like IDirection no longer exists
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


    public Direction(GraphPath<String, IdentifiedWeightedEdge> path, Map<String, ZooData.VertexInfo> vInfo,
                     Map<String, ZooData.EdgeInfo> eInfo, StepRenderer stepRenderer) {
        // code for setting fields adapted from App.java in the ZooSeeker assets
        this.vInfo = vInfo;
        this.eInfo = eInfo;
        this.path = path;
        this.directionInfo = new DirectionInfo(
                path.getStartVertex(),
                path.getEndVertex(),
                vInfo.get(path.getEndVertex()).name,
                path.getWeight()
                );
        this.G = path.getGraph();
        this.steps = computeSteps(stepRenderer);
    }

    public Direction(GraphPath<String, IdentifiedWeightedEdge> path,
                     List<String> exhibitsInGroup,
                     Map<String, ZooData.VertexInfo> vInfo,
                     Map<String, ZooData.EdgeInfo> eInfo, StepRenderer stepRenderer) {
        this(path, vInfo, eInfo, stepRenderer);
        this.steps.get(steps.size() - 1).destination =
                String.format(Locale.US, "%s and find %s inside",
                    vInfo.get(path.getEndVertex()).name,
                    getReadableList(exhibitsInGroup.stream()
                            .map(x -> vInfo.get(x).name)
                            .collect(toList()))
                );
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

    private List<Step> computeSteps(StepRenderer stepRenderer) {
        return stepRenderer.getSteps(path, vInfo, eInfo);
    }

    // helper method that rounds distance to 1 sig fig
    static int roundDistance(double d) {
        return (int) Math.round(d);
    }

    static String getReadableList(List<String> stringList) {
        if (stringList.size() == 0) return "";
        if (stringList.size() == 1) return stringList.get(0);
        StringBuilder result = new StringBuilder();
        result.append(String.join(", ", stringList.subList(0, stringList.size() - 1)));
        result.append(" and ");
        result.append(stringList.get(stringList.size() - 1));
        return result.toString();
    }
}
