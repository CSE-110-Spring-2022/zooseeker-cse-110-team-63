package com.team63.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.io.Console;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewPlanActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private List<Direction> plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plan);

        PlanItemAdapter adapter = new PlanItemAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.plan_items);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adapter);

        // demo code - delete later
        Graph<String, IdentifiedWeightedEdge> G = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");
        List<String> targets = Arrays.asList("elephant_odyssey", "arctic_foxes", "gators", "gorillas");
        RouteGenerator demoRouteGen = new NNRouteGenerator(G);
        ArrayList<Direction> demoPlan = new ArrayList<>();
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");
        for (GraphPath<String, IdentifiedWeightedEdge> path : demoRouteGen.getRoute("entrance_exit_gate", targets)) {
            demoPlan.add(new Direction(path, vInfo, eInfo));
        }
        plan = demoPlan;
        // end of demo code

        List<Direction> planWithoutExit = new ArrayList<>(plan);
        planWithoutExit.remove(planWithoutExit.size() - 1);
        adapter.setPlanItems(planWithoutExit);
        TextView planCount = findViewById(R.id.plan_count);
        planCount.setText(String.format(Locale.US, "Plan (%d)", adapter.getItemCount()));
    }


    public void onGetDirectionsClicked(View view) {
        Intent intent = new Intent(this, DirectionActivity.class);
//        Log.d("tag", plan.get(0).steps.toString());
        Direction direction = plan.get(0);
        intent.putExtra("direction", direction);
        intent.putExtra("steps", (Serializable) direction.steps);
        startActivity(intent);
    }
}