package com.team63.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.Collection;
import java.util.List;

/*
 * This abstract class has one main method: getPlan(List<String> ids), which takes in a list of
 * node IDs (in the form of Strings), calculates a plan that traverses through the nodes respective
 * of the IDs, then returns this plan iin the form of a List of GraphPaths.
 */
public abstract class PlanGenerator {
    Graph<String, IdentifiedWeightedEdge> G;
    abstract public List<GraphPath<String, IdentifiedWeightedEdge>> getPlan(String entrance, Collection<String> exhibits, String exit);
    // each String in ids is an ID referring to a planned exhibit, NOT INCLUDING ENTRANCE AND EXIT
    // each GraphPath in the List represents a path from one exhibit to another exhibit on plan
}
