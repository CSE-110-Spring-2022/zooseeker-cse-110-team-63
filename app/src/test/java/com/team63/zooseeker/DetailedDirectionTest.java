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
public class DetailedDirectionTest {
    static final Double doubleDelta = 0.001; // tolerance for Double precision error
    Context context;
    Graph<String, IdentifiedWeightedEdge> G;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;

    @Before
    public void initializeFields() {
        context = ApplicationProvider.getApplicationContext();
        G = ZooData.loadZooGraphJSON(context, context.getString(R.string.zoo_graph_v2));
        vInfo = ZooData.loadVertexInfoJSON(context, context.getString(R.string.vertex_info_v2));
        eInfo = ZooData.loadEdgeInfoJSON(context, context.getString(R.string.edge_info_v2));
    }

    // Test case: The street changes at some point at intersection
    // also street has no real options at another intersection
    @Test
    public void testStreetChangeIntersection() {
        GraphPath<String, IdentifiedWeightedEdge> path =
                new GraphWalk<>
                        (G, Arrays.asList("entrance_exit_gate", "intxn_front_treetops", "intxn_front_monkey", "flamingo"), 5300);
        Direction testPD = new Direction(path, vInfo, eInfo, new DetailedStepRenderer());

        List<Step> stepList = testPD.steps;
        assertEquals(3, stepList.size());
        Step toIntersection1 = stepList.get(0);
        assertEquals(1100, toIntersection1.distance,doubleDelta);
        assertEquals("Front Street", toIntersection1.destination);
        assertEquals("Gate Path", toIntersection1.street);

        Step toIntersection2 = stepList.get(1);
        assertEquals(2700, toIntersection2.distance, doubleDelta);
        assertEquals("Monkey Trail", toIntersection2.destination);
        assertEquals("Front Street", toIntersection2.street);

        Step toFlamingos = stepList.get(2);
        assertEquals(1500, toFlamingos.distance, doubleDelta);
        assertEquals("Flamingos", toFlamingos.destination);
        assertEquals("Monkey Trail", toFlamingos.street);
    }

    // Test case: The street does not change, even though at a intersection
    @Test
    public void testStreetNoChangeIntersection() {
        GraphPath<String, IdentifiedWeightedEdge> path =
                new GraphWalk<>
                        (G, Arrays.asList("intxn_front_treetops", "intxn_treetops_fern_trail",
                                "intxn_treetops_orangutan_trail", "siamang"),
                                3700);
        Direction testPD = new Direction(path, vInfo, eInfo, new DetailedStepRenderer());
        List<Step> stepList = testPD.steps;

        Step toFernTrail = stepList.get(0);
        assertEquals(1100, toFernTrail.distance,doubleDelta);
        assertEquals("Fern Canyon Trail", toFernTrail.destination);
        assertEquals("Treetops Way", toFernTrail.street);

        Step toOrangutanTrail = stepList.get(1);
        assertEquals(1400, toOrangutanTrail.distance,doubleDelta);
        assertEquals("Orangutan Trail", toOrangutanTrail.destination);
        assertEquals("Treetops Way", toOrangutanTrail.street);

        Step toSiamangTrail = stepList.get(2);
        assertEquals(1200, toSiamangTrail.distance,doubleDelta);
        assertEquals("Siamangs", toSiamangTrail.destination);
        assertEquals("Orangutan Trail", toSiamangTrail.street);
    }
}
