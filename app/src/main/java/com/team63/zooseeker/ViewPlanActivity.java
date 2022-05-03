package com.team63.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewPlanActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public PlanViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plan);
        viewModel = new ViewModelProvider(this)
                .get(PlanViewModel.class);

        PlanItemAdapter adapter = new PlanItemAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.plan_items);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adapter);
        viewModel.getDirectionsLive().observe(this, adapter::setPlanItems);
        viewModel.getDirectionsLive().observe(this, this::setDirectionCount);
    }

    public void setDirectionCount(List<Direction> directions) {
        TextView planCount = findViewById(R.id.plan_count);
        planCount.setText(String.format(Locale.US, "Plan (%d)", directions.size()));
    }

    public void goBack(View view) {
        this.finish();
    }
}