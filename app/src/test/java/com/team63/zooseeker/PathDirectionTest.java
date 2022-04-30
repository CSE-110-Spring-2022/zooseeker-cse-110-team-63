package com.team63.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.GraphWalk;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class PathDirectionTest {
    static final Double doubleDelta = 0.001; // tolerance for Double precision error
    static final String testGraphFile = "sample_zoo_graph.json";
    static final String testVInfoFile = "sample_node_info.json";
    static final String testEInfoFile = "sample_edge_info.json";
    Context context;
    Graph<String, IdentifiedWeightedEdge> G;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;

    @Before
    public void initializeFields() {
        context = ApplicationProvider.getApplicationContext();
        G = ZooData.loadZooGraphJSON(context, testGraphFile);
        vInfo = ZooData.loadVertexInfoJSON(context, testVInfoFile);
        eInfo = ZooData.loadEdgeInfoJSON(context, testEInfoFile);
    }

    // Test case: The street changes at some point at intersection
    @Test
    public void testStreetChangeIntersection() {
        GraphPath<String, IdentifiedWeightedEdge> path =
                new GraphWalk<>
                        (G, Arrays.asList("entrance_exit_gate", "entrance_plaza", "gorillas", "lions"), 410);
        PathDirection testPD = new PathDirection(path, vInfo, eInfo);
        List<Step> stepList = testPD.breakIntoSteps();
        assertEquals(2, stepList.size());
        assertEquals(10, stepList.get(0).distance,doubleDelta);
        assertEquals("Africa Rocks Street", stepList.get(0).destination);
        assertEquals("Entrance Way", stepList.get(0).street);
        assertEquals(400, stepList.get(1).distance, doubleDelta);
        assertEquals("Lions", stepList.get(1).destination);
        assertEquals("Africa Rocks Street", stepList.get(1).street);

        List<String> directions = testPD.getTextDirection();
        assertEquals("Proceed on Entrance Way 10 ft towards Africa Rocks Street", directions.get(0));
        assertEquals("Proceed on Africa Rocks Street 400 ft towards Lions", directions.get(1));
    }

    // Test case: The street does not change at any point along the path, going "backwards"
    @Test
    public void testStreetNoChange() {
        GraphPath<String, IdentifiedWeightedEdge> path =
                new GraphWalk<>
                        (G, Arrays.asList("elephant_odyssey", "lions", "gorillas"), 400);
        PathDirection testPD = new PathDirection(path, vInfo, eInfo);
        List<Step> stepList = testPD.breakIntoSteps();
        assertEquals(1, stepList.size());
        assertEquals(400, stepList.get(0).distance,doubleDelta);
        assertEquals("Gorillas", stepList.get(0).destination);
        assertEquals("Africa Rocks Street", stepList.get(0).street);

        List<String> directions = testPD.getTextDirection();
        assertEquals("Proceed on Africa Rocks Street 400 ft towards Gorillas", directions.get(0));
    }
}
