package com.team63.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.GraphWalk;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class DirectionTest {
    static final Double doubleDelta = 0.001; // tolerance for Double precision error
    Context context;
    Graph<String, IdentifiedWeightedEdge> G;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;

    @Before
    public void initializeFields() {
        context = ApplicationProvider.getApplicationContext();
        G = ZooData.loadZooGraphJSON(context, context.getString(R.string.zoo_graph_v1));
        vInfo = ZooData.loadVertexInfoJSON(context, context.getString(R.string.vertex_info_v1));
        eInfo = ZooData.loadEdgeInfoJSON(context, context.getString(R.string.edge_info_v1));
    }

    // Test case: The street changes at some point at intersection
    @Test
    public void testStreetChangeIntersection() {
        GraphPath<String, IdentifiedWeightedEdge> path =
                new GraphWalk<>
                        (G, Arrays.asList("entrance_exit_gate", "entrance_plaza", "gorillas", "lions"), 410);
        Direction testPD = new Direction(path, vInfo, eInfo, new BigStepRenderer());
        assertEquals("Lions", testPD.directionInfo.name);
        List<Step> stepList = testPD.steps;
        assertEquals(2, stepList.size());
        assertEquals(10, stepList.get(0).distance,doubleDelta);
        assertEquals("Africa Rocks Street", stepList.get(0).destination);
        assertEquals("Entrance Way", stepList.get(0).street);
        assertEquals(400, stepList.get(1).distance, doubleDelta);
        assertEquals("Lions", stepList.get(1).destination);
        assertEquals("Africa Rocks Street", stepList.get(1).street);
    }

    // Test case: The street does not change at any point along the path, going "backwards"
    @Test
    public void testStreetNoChange() {
        GraphPath<String, IdentifiedWeightedEdge> path =
                new GraphWalk<>
                        (G, Arrays.asList("elephant_odyssey", "lions", "gorillas"), 400);
        Direction testPD = new Direction(path, vInfo, eInfo, new BigStepRenderer());
        assertEquals("Gorillas", testPD.directionInfo.name);
        List<Step> stepList = testPD.steps;
        assertEquals(1, stepList.size());
        assertEquals(400, stepList.get(0).distance,doubleDelta);
        assertEquals("Gorillas", stepList.get(0).destination);
        assertEquals("Africa Rocks Street", stepList.get(0).street);
    }

    @Test
    public void testWithRealRoute() {
        RouteGenerator routeGen = new NNRouteGenerator();
        List<String> exhibits = Arrays.asList(
                "elephant_odyssey",
                "arctic_foxes",
                "gators",
                "lions"
                );
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = routeGen
                .setG(G)
                .setEntrance("entrance_exit_gate")
                .setExit("entrance_exit_gate")
                .setExhibits(exhibits)
                .getRoute();
        List<Direction> directions = new ArrayList<>();

        for (GraphPath<String, IdentifiedWeightedEdge> path : paths) {
            directions.add(new Direction(path, vInfo, eInfo, new BigStepRenderer()));
        }

        Direction toGators = directions.get(0);
        assertEquals(10, toGators.steps.get(0).distance, doubleDelta);
        assertEquals("Entrance Way", toGators.steps.get(0).street);
        assertEquals("Reptile Road", toGators.steps.get(0).destination);
        assertEquals(100, toGators.steps.get(1).distance, doubleDelta);
        assertEquals("Reptile Road", toGators.steps.get(1).street);
        assertEquals("Alligators", toGators.steps.get(1).destination);

        Direction toLions = directions.get(1);
        assertEquals(200, toLions.steps.get(0).distance, doubleDelta);
        assertEquals("Sharp Teeth Shortcut", toLions.steps.get(0).street);
        assertEquals("Lions", toLions.steps.get(0).destination);

        Direction toElephants = directions.get(2);
        assertEquals(200, toElephants.steps.get(0).distance, doubleDelta);
        assertEquals("Africa Rocks Street", toElephants.steps.get(0).street);
        assertEquals("Elephant Odyssey", toElephants.steps.get(0).destination);

        Direction toFoxes = directions.get(3);
        assertEquals(200, toFoxes.steps.get(0).distance, doubleDelta);
        assertEquals("Africa Rocks Street", toFoxes.steps.get(0).street);
        assertEquals("Lions", toFoxes.steps.get(0).destination);
        assertEquals(200, toFoxes.steps.get(1).distance, doubleDelta);
        assertEquals("Sharp Teeth Shortcut", toFoxes.steps.get(1).street);
        assertEquals("Alligators", toFoxes.steps.get(1).destination);
        assertEquals(100, toFoxes.steps.get(2).distance, doubleDelta);
        assertEquals("Reptile Road", toFoxes.steps.get(2).street);
        assertEquals("Arctic Avenue", toFoxes.steps.get(2).destination);
        assertEquals(300, toFoxes.steps.get(3).distance, doubleDelta);
        assertEquals("Arctic Avenue", toFoxes.steps.get(3).street);
        assertEquals("Arctic Foxes", toFoxes.steps.get(3).destination);

        Direction toExit = directions.get(4);
        assertEquals(300, toExit.steps.get(0).distance, doubleDelta);
        assertEquals("Arctic Avenue", toExit.steps.get(0).street);
        assertEquals("Entrance Way", toExit.steps.get(0).destination);
        assertEquals(10, toExit.steps.get(1).distance, doubleDelta);
        assertEquals("Entrance Way", toExit.steps.get(1).street);
        assertEquals("Entrance and Exit Gate", toExit.steps.get(1).destination);
    }
}
