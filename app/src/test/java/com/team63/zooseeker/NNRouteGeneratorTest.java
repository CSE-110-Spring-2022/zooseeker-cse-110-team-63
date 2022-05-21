package com.team63.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class NNRouteGeneratorTest {
    static final String entranceExit = "entrance_exit_gate"; // string ID of entrance/exit gate
    static final Double doubleDelta = 0.001; // tolerance for Double precision error
    Graph<String, IdentifiedWeightedEdge> G;
    NNRouteGenerator testPlanGenerator;
    @Before
    public void initializeFields() {
        Context context = ApplicationProvider.getApplicationContext();
        G = ZooData.loadZooGraphJSON(context,context.getString(R.string.test_zoo_graph));
        testPlanGenerator = new NNRouteGenerator();
    }

    @Test
    public void testNoExhibits() {
        ArrayList<String> exhibits = new ArrayList<>();
        List<GraphPath<String, IdentifiedWeightedEdge>> plan = testPlanGenerator
            .setEntrance(entranceExit)
            .setExit(entranceExit)
            .setExhibits(exhibits)
            .getRoute();

        assertEquals(plan.size(), 1);
        assertEquals(plan.get(0).getStartVertex(), entranceExit);
        assertEquals(plan.get(0).getEndVertex(), entranceExit);
        assertEquals(plan.get(0).getWeight(),0, doubleDelta);
    }

    @Test
    public void testOneExhibit() {
        String exhibit = "arctic_foxes";
        ArrayList<String> exhibits = new ArrayList<>();
        exhibits.add(exhibit);
        List<GraphPath<String, IdentifiedWeightedEdge>> plan = testPlanGenerator
                .setEntrance(entranceExit)
                .setExit(entranceExit)
                .setExhibits(exhibits)
                .getRoute();

        assertEquals(plan.size(), 2);
        assertEquals(plan.get(0).getStartVertex(), entranceExit);
        assertEquals(plan.get(0).getEndVertex(), exhibit);
        assertEquals(plan.get(0).getWeight(), 310, doubleDelta);
        assertEquals(plan.get(1).getStartVertex(), exhibit);
        assertEquals(plan.get(1).getEndVertex(), entranceExit);
        assertEquals(plan.get(1).getWeight(), 310, doubleDelta);
    }

    @Test
    public void testMultipleExhibits() {
        ArrayList<String> exhibits = new ArrayList<>();
        exhibits.add("elephant_odyssey");
        exhibits.add("arctic_foxes");
        exhibits.add("gorillas");
        exhibits.add("gators");

        List<GraphPath<String, IdentifiedWeightedEdge>> plan = testPlanGenerator
                .setEntrance(entranceExit)
                .setExit(entranceExit)
                .setExhibits(exhibits)
                .getRoute();

        assertEquals(plan.size(), exhibits.size() + 1);

        assertEquals(plan.get(0).getStartVertex(), entranceExit);
        assertEquals(plan.get(0).getEndVertex(), "gators");
        assertEquals(plan.get(0).getWeight(), 110, doubleDelta);

        assertEquals(plan.get(1).getStartVertex(), "gators");
        assertEquals(plan.get(1).getEndVertex(), "gorillas");
        assertEquals(plan.get(1).getWeight(), 300, doubleDelta);

        assertEquals(plan.get(2).getStartVertex(), "gorillas");
        assertEquals(plan.get(2).getEndVertex(), "elephant_odyssey");
        assertEquals(plan.get(2).getWeight(), 400, doubleDelta);

        assertEquals(plan.get(3).getStartVertex(), "elephant_odyssey");
        assertEquals(plan.get(3).getEndVertex(), "arctic_foxes");
        assertEquals(plan.get(3).getWeight(), 800, doubleDelta);

        assertEquals(plan.get(4).getStartVertex(), "arctic_foxes");
        assertEquals(plan.get(4).getEndVertex(), entranceExit);
        assertEquals(plan.get(4).getWeight(), 310, doubleDelta);
    }
}
