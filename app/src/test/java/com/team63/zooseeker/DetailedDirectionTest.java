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

    @Test
    public void detailedDirectionsTest ()
    {
        RouteGenerator routeGen = new NNRouteGenerator();
        List<String> exhibits = Arrays.asList(
                "hippo",
                "capuchin"
        );
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = routeGen
                .setG(G)
                .setEntrance("entrance_exit_gate")
                .setExit("entrance_exit_gate")
                .setExhibits(exhibits)
                .getRoute();
        List<Direction> directions = new ArrayList<>();

        for (GraphPath<String, IdentifiedWeightedEdge> path : paths) {
            directions.add(new Direction(path, vInfo, eInfo, new DetailedStepRenderer()));
        }

        Direction toHippos = directions.get(0);
        assertEquals(10, toHippos.steps.get(0).distance, doubleDelta);
        assertEquals("Gate Path", toHippos.steps.get(0).street);
        assertEquals("Front Street", toHippos.steps.get(0).destination);

        assertEquals(30, toHippos.steps.get(1).distance, doubleDelta);
        assertEquals("Treetops Way", toHippos.steps.get(1).street);
        assertEquals("Fern Canyon Trail", toHippos.steps.get(1).destination);

        assertEquals(30, toHippos.steps.get(2).distance, doubleDelta);
        assertEquals("Treetops Way", toHippos.steps.get(2).street);
        assertEquals("Orangutan Trail", toHippos.steps.get(2).destination);

        assertEquals(100, toHippos.steps.get(3).distance, doubleDelta);
        assertEquals("Treetops Way", toHippos.steps.get(3).street);
        assertEquals("Hippo Trail", toHippos.steps.get(3).destination);

        assertEquals(30, toHippos.steps.get(4).distance, doubleDelta);
        assertEquals("Hippo Trail", toHippos.steps.get(4).street);
        assertEquals("Hippos", toHippos.steps.get(4).destination);

        Direction toMonke = directions.get(1);
        assertEquals(10, toMonke.steps.get(0).distance, doubleDelta);
        assertEquals("Hippo Trail", toMonke.steps.get(0).street);
        assertEquals("Crocodiles", toMonke.steps.get(0).destination);

        assertEquals(30, toMonke.steps.get(1).distance, doubleDelta);
        assertEquals("Hippo Trail", toMonke.steps.get(1).street);
        assertEquals("Monkey Trail", toMonke.steps.get(1).destination);

        assertEquals(50, toMonke.steps.get(2).distance, doubleDelta);
        assertEquals("Monkey Trail", toMonke.steps.get(2).street);
        assertEquals("Capuchin Monkeys", toMonke.steps.get(2).destination);

        Direction toExit = directions.get(2);
        assertEquals(150, toExit.steps.get(0).distance, doubleDelta);
        assertEquals("Monkey Trail", toExit.steps.get(0).street);
        assertEquals("Flamingos", toExit.steps.get(0).destination);

        assertEquals(30, toExit.steps.get(1).distance, doubleDelta);
        assertEquals("Monkey Trail", toExit.steps.get(1).street);
        assertEquals("Front Street", toExit.steps.get(1).destination);

        assertEquals(50, toExit.steps.get(2).distance, doubleDelta);
        assertEquals("Front Street", toExit.steps.get(2).street);
        assertEquals("Gate Path", toExit.steps.get(2).destination);

        assertEquals(10, toExit.steps.get(3).distance, doubleDelta);
        assertEquals("Gate Path", toExit.steps.get(3).street);
        assertEquals("Entrance and Exit Gate", toExit.steps.get(3).destination);




    }

    @Test
    public void singleTest ()
    {
        RouteGenerator routeGen = new NNRouteGenerator();
        List<String> exhibits = Arrays.asList(
                "siamang"
        );
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = routeGen
                .setG(G)
                .setEntrance("entrance_exit_gate")
                .setExit("entrance_exit_gate")
                .setExhibits(exhibits)
                .getRoute();
        List<Direction> directions = new ArrayList<>();

        for (GraphPath<String, IdentifiedWeightedEdge> path : paths) {
            directions.add(new Direction(path, vInfo, eInfo, new DetailedStepRenderer()));
        }

        Direction toMonke = directions.get(0);
        assertEquals(10, toMonke.steps.get(0).distance, doubleDelta);
        assertEquals("Gate Path", toMonke.steps.get(0).street);
        assertEquals("Front Street", toMonke.steps.get(0).destination);

        assertEquals(30, toMonke.steps.get(1).distance, doubleDelta);
        assertEquals("Treetops Way", toMonke.steps.get(1).street);
        assertEquals("Fern Canyon Trail", toMonke.steps.get(1).destination);

        assertEquals(30, toMonke.steps.get(2).distance, doubleDelta);
        assertEquals("Treetops Way", toMonke.steps.get(2).street);
        assertEquals("Orangutan Trail", toMonke.steps.get(2).destination);

        assertEquals(60, toMonke.steps.get(3).distance, doubleDelta);
        assertEquals("Orangutan Trail", toMonke.steps.get(3).street);
        assertEquals("Siamangs", toMonke.steps.get(3).destination);

        Direction toExit = directions.get(1);
        assertEquals(60, toExit.steps.get(0).distance, doubleDelta);
        assertEquals("Orangutan Trail", toExit.steps.get(0).street);
        assertEquals("Treetops Way", toExit.steps.get(0).destination);

        assertEquals(30,toExit.steps.get(1).distance,doubleDelta);
        assertEquals("Treetops Way",toExit.steps.get(1).street);
        assertEquals("Fern Canyon Trail",toExit.steps.get(1).destination);

        assertEquals(30,toExit.steps.get(2).distance,doubleDelta);
        assertEquals("Treetops Way",toExit.steps.get(2).street);
        assertEquals("Gate Path",toExit.steps.get(2).destination);

        assertEquals(10,toExit.steps.get(3).distance,doubleDelta);
        assertEquals("Gate Path",toExit.steps.get(3).street);
        assertEquals("Entrance and Exit Gate",toExit.steps.get(3).destination);

    }

}