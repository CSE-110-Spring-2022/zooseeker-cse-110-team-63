package com.team63.zooseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class ViewPlanActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public PlanViewModel viewModel;
    public PlanItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plan);
        viewModel = new ViewModelProvider(this)
                .get(PlanViewModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new PlanItemAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.plan_items);

        // see SearchActivity.java credits for divider
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adapter);
        viewModel.getDirections().observe(this, adapter::setPlanItems);
        viewModel.getDirections().observe(this, this::setDirectionCount);
    }

    public void setDirectionCount(List<Direction> directions) {
        TextView planCount = findViewById(R.id.plan_count);
        planCount.setText(String.format(Locale.US, "Plan (%d)", adapter.getItemCount()));
    }


//     public void onGetDirectionsClicked(View view) {
//         Intent intent = new Intent(this, DirectionActivity.class);
// //        Log.d("tag", plan.get(0).steps.toString());
//         Direction direction = plan.get(0);
//         intent.putExtra("direction", direction);
//         intent.putExtra("steps", (Serializable) direction.steps);
//         startActivity(intent);
//     }

    // https://stackoverflow.com/questions/7479992/handling-a-menu-item-click-event-android
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}