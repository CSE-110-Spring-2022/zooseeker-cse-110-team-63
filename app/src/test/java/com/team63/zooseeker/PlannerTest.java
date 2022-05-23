package com.team63.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class PlannerTest {
    static final Double doubleDelta = 0.001; // tolerance for Double precision error
    Context context;
    Graph<String, IdentifiedWeightedEdge> G;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;
    Planner planner;

    @Before
    public void initializeFields() {
        context = ApplicationProvider.getApplicationContext();
        G = ZooData.loadZooGraphJSON(context, context.getString(R.string.test_zoo_graph));
        vInfo = ZooData.loadVertexInfoJSON(context, context.getString(R.string.test_vertex_info));
        eInfo = ZooData.loadEdgeInfoJSON(context, context.getString(R.string.test_edge_info));
        PlannerBuilder pb = new PlannerBuilder();
        planner = pb.setG(G)
                .setVInfoMap(vInfo)
                .setEInfoMap(eInfo)
                .setRouteGenerator(new NNRouteGenerator())
                .make();
    }

    // tests the planner's planExhibits method, using same test case used in
    // DirectionTest.testWithRealRoute
    @Test
    public void testPlanExhibits() {
        List<String> exhibits = Arrays.asList(
                "elephant_odyssey",
                "arctic_foxes",
                "gators",
                "lions"
        );
        List<Direction> directions = planner
                .performOperation(new PlanExhibitsOperation(exhibits))
                .getDirections();
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

    // testing the skip feature after setting directions
    // under the case of skipping the first one
    @Test
    public void testSkipFirst() {
        List<String> exhibits = Arrays.asList(
                "elephant_odyssey",
                "arctic_foxes",
                "gators",
                "lions"
        );
        List<Direction> directions = planner
                .performOperation(new PlanExhibitsOperation(exhibits))
                .getDirections();
        List<Direction> newDirections = planner
                .setDirections(directions)
                .performOperation(new SkipOperation(0))
                .getDirections();
        Direction toLions = newDirections.get(0);
        assertEquals(10, toLions.steps.get(0).distance, doubleDelta);
        assertEquals("Entrance Way", toLions.steps.get(0).street);
        assertEquals("Reptile Road", toLions.steps.get(0).destination);
        assertEquals(100, toLions.steps.get(1).distance, doubleDelta);
        assertEquals("Reptile Road", toLions.steps.get(1).street);
        assertEquals("Alligators", toLions.steps.get(1).destination);
        assertEquals(200, toLions.steps.get(2).distance, doubleDelta);
        assertEquals("Sharp Teeth Shortcut", toLions.steps.get(2).street);
        assertEquals("Lions", toLions.steps.get(2).destination);

        Direction toElephants = newDirections.get(1);
        assertEquals(200, toElephants.steps.get(0).distance, doubleDelta);
        assertEquals("Africa Rocks Street", toElephants.steps.get(0).street);
        assertEquals("Elephant Odyssey", toElephants.steps.get(0).destination);

        Direction toFoxes = newDirections.get(2);
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

        Direction toExit = newDirections.get(3);
        assertEquals(300, toExit.steps.get(0).distance, doubleDelta);
        assertEquals("Arctic Avenue", toExit.steps.get(0).street);
        assertEquals("Entrance Way", toExit.steps.get(0).destination);
        assertEquals(10, toExit.steps.get(1).distance, doubleDelta);
        assertEquals("Entrance Way", toExit.steps.get(1).street);
        assertEquals("Entrance and Exit Gate", toExit.steps.get(1).destination);
    }

    // under the case of skipping a middle one
    @Test
    public void testSkipMiddle() {
        List<String> exhibits = Arrays.asList(
                "elephant_odyssey",
                "arctic_foxes",
                "gators",
                "lions"
        );
        List<Direction> directions = planner
                .performOperation(new PlanExhibitsOperation(exhibits))
                .getDirections();
        List<Direction> newDirections = planner
                .setDirections(directions)
                .performOperation(new SkipOperation(2))
                .getDirections();

        Direction toFoxes = newDirections.get(2);
        assertEquals(200, toFoxes.steps.get(0).distance, doubleDelta);
        assertEquals("Sharp Teeth Shortcut", toFoxes.steps.get(0).street);
        assertEquals("Alligators", toFoxes.steps.get(0).destination);
        assertEquals(100, toFoxes.steps.get(1).distance, doubleDelta);
        assertEquals("Reptile Road", toFoxes.steps.get(1).street);
        assertEquals("Arctic Avenue", toFoxes.steps.get(1).destination);
        assertEquals(300, toFoxes.steps.get(2).distance, doubleDelta);
        assertEquals("Arctic Avenue", toFoxes.steps.get(2).street);
        assertEquals("Arctic Foxes", toFoxes.steps.get(2).destination);

        Direction toExit = newDirections.get(3);
        assertEquals(300, toExit.steps.get(0).distance, doubleDelta);
        assertEquals("Arctic Avenue", toExit.steps.get(0).street);
        assertEquals("Entrance Way", toExit.steps.get(0).destination);
        assertEquals(10, toExit.steps.get(1).distance, doubleDelta);
        assertEquals("Entrance Way", toExit.steps.get(1).street);
        assertEquals("Entrance and Exit Gate", toExit.steps.get(1).destination);
    }
}
