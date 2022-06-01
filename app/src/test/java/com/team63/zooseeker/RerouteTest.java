package com.team63.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class RerouteTest {
    static final Double doubleDelta = 0.001; // tolerance for Double precision error
    Context context;
    Graph<String, IdentifiedWeightedEdge> G;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;
    Planner planner;

    @Before
    public void initializeFields() {
        context = ApplicationProvider.getApplicationContext();
        G = ZooData.loadZooGraphJSON(context, context.getString(R.string.zoo_graph_v2));
        vInfo = ZooData.loadVertexInfoJSON(context, context.getString(R.string.vertex_info_v2));
        eInfo = ZooData.loadEdgeInfoJSON(context, context.getString(R.string.edge_info_v2));
        PlannerBuilder pb = new PlannerBuilder();
        planner = pb.setG(G)
                .setVInfoMap(vInfo)
                .setEInfoMap(eInfo)
                .setRouteGenerator(new NNRouteGenerator())
                .make();
    }

    // First test - rerouting with a normal set of directions
    // with directionInd somewhere in the middle
    // and new location being different, nothing abnormal
    @Test
    public void testNormalReroute() {
        RouteGenerator routeGen = new NNRouteGenerator();
        List<String> exhibits = Arrays.asList(
                "flamingo",
                "koi",
                "capuchin",
                "gorilla"
        );
        List<Direction> directions = planner
                .performOperation(new PlanExhibitsOperation(exhibits))
                .getDirections();

        // confirm new directions work as intended
        {
            Direction toFlamingo = directions.get(0);
            assertEquals(1100, toFlamingo.steps.get(0).distance, doubleDelta);
            assertEquals("Gate Path", toFlamingo.steps.get(0).street);
            assertEquals("Front Street", toFlamingo.steps.get(0).destination);
            assertEquals(2700, toFlamingo.steps.get(1).distance, doubleDelta);
            assertEquals("Front Street", toFlamingo.steps.get(1).street);
            assertEquals("Monkey Trail", toFlamingo.steps.get(1).destination);
            assertEquals(1500, toFlamingo.steps.get(2).distance, doubleDelta);
            assertEquals("Monkey Trail", toFlamingo.steps.get(2).street);
            assertEquals("Flamingos", toFlamingo.steps.get(2).destination);
        }
        // (entrance_exit_gate)
        // flamingo first
        // capuchin
        // gorilla
        // koi

        MockLocationObserver mockLocationObserver =
                new MockLocationObserver(directions, G, vInfo, eInfo, 0);

        MockLocationSubject mockLocationSubject =
                new MockLocationSubject();

        mockLocationSubject.registerObserver(mockLocationObserver);

        mockLocationSubject.setNearestLocation("crocodile");

        List<Direction> newDirections = mockLocationObserver.getDirectionsList();
        assertEquals(newDirections.get(0).directionInfo.name, "Capuchin Monkeys");
        assertEquals(newDirections.get(1).directionInfo.name, "Flamingos");
        assertEquals(newDirections.get(2).directionInfo.name, "Gorillas");
        assertEquals(newDirections.get(3).directionInfo.name, "Koi Fish");
        assertEquals(newDirections.get(4).directionInfo.name, "Entrance and Exit Gate");
    }

    @Test
    public void testBasicReroute() {
        RouteGenerator routeGen = new NNRouteGenerator();
        List<String> exhibits = Arrays.asList(
                "flamingo",
                "koi",
                "capuchin",
                "gorilla"
        );
        // flamingo, capuchin, gorilla, koi
        List<Direction> directions = planner
                .performOperation(new PlanExhibitsOperation(exhibits))
                .getDirections();

        MockLocationObserver mockLocationObserver =
                new MockLocationObserver(directions, G, vInfo, eInfo, 1);

        BasicLocationSubject basicLocationSubject =
                new BasicLocationSubject(vInfo);

        basicLocationSubject.registerObserver(mockLocationObserver);

        // this time set location with latitude and longitude, to the south intersection near koi
        basicLocationSubject.changeLocation(32.72624997716322,-117.15599314253906);

        List<Direction> newDirections = mockLocationObserver.getDirectionsList();
        assertEquals(newDirections.get(0).directionInfo.name, "Flamingos");
        assertEquals(newDirections.get(1).directionInfo.name, "Koi Fish");
        assertEquals(newDirections.get(2).directionInfo.name, "Capuchin Monkeys");
        assertEquals(newDirections.get(3).directionInfo.name, "Gorillas");
        assertEquals(newDirections.get(4).directionInfo.name, "Entrance and Exit Gate");
    }

    // Second test of the original two - last step is a group
    // in at least one (here: one) of the directions
    @Test
    public void testGroupReroute() {
        List<String> exhibits = Arrays.asList(
                "crocodile",
                "gorilla",
                "mynah",
                "dove"
        );
        // crocodile
        // gorilla
        // owens_aviary (Owens Aviary and find Bali Mynah and Emerald Dove inside)
        // entrance_exit_gate
        List<Direction> directions = planner
                .performOperation(new PlanExhibitsOperation(exhibits))
                .getDirections();

        MockLocationObserver mockLocationObserver =
                new MockLocationObserver(directions, G, vInfo, eInfo, 0);

        BasicLocationSubject basicLocationSubject =
                new BasicLocationSubject(vInfo);

        basicLocationSubject.registerObserver(mockLocationObserver);

        // this time set location with latitude and longitude, this case @ orangutans
        basicLocationSubject.changeLocation(32.735851415117665,-117.16626781198586);
        List<Direction> newDirections = mockLocationObserver.getDirectionsList();
        assertEquals(newDirections.get(0).directionInfo.name, "Owens Aviary");
        // check if last step in group direction is properly rendered
        assertEquals(newDirections.get(0).steps.get(1).destination,
                "Owens Aviary and find Bali Mynah and Emerald Dove inside");
        assertEquals(newDirections.get(1).directionInfo.name, "Crocodiles");
        assertEquals(newDirections.get(2).directionInfo.name, "Gorillas");
        assertEquals(newDirections.get(3).directionInfo.name, "Entrance and Exit Gate");

    }


}