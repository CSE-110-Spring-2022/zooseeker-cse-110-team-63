package com.team63.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DirectionActivity extends AppCompatActivity {
    private List<Direction> directions;
    private List<Step> steps;
    private PlanViewModel planViewModel;
    private int directionInd;

    private TextView exhibitView;
    private TextView directionsView;
    private Button nextBtn;
    private Button prevBtn;
    private Button planBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        exhibitView = findViewById(R.id.exhibit_view);
        directionsView = findViewById(R.id.directions_view);
        nextBtn = findViewById(R.id.next_exhibit_btn);
        prevBtn = findViewById(R.id.previous_exhibit_btn);
        planBtn = findViewById(R.id.plan_btn);

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        directionInd = 0;

//        Direction direction = getIntent().getParcelableExtra("direction");
//        List<Step> steps = (List<Step>) getIntent().getSerializableExtra("steps");
        LiveData<List<Direction>> liveData = planViewModel.getDirections();
//        List<Direction> directions = liveData.getValue();
        liveData.observe(this, this::updateDirections);

//        Log.d("tag", direction.name);
//        Log.d("tag", "" + direction.getDistance());
//        Log.d("tag", steps.get(0).toString());

    }

    public void updateDirections(List<Direction> directions){
        this.directions = directions;
        this.steps = directions.get(directionInd).getSteps();

        String dirStrings = "";
        int count = 1;
        double cumDist = 0;
        for (Step step : steps) {
            dirStrings += count + ": " + step.toString() + ".\n\n";
            cumDist += step.distance;
            count++;
        }
//        String steps = direction.steps.get(0).toString();
        String destination = steps.get(steps.size() - 1).destination;
        directionsView.setText(dirStrings);
        exhibitView.setText(destination + "\n(" + cumDist + " ft)");
    }

    public void onNextBtnClicked(View view) {
        if (directionInd >= directions.size() - 1) {
            Alert("You have reached the end of the plan.");
            return;
        }
        directionInd++;
        updateDirections(directions);
    }

    public void onPrevBtnClicked(View view) {
        if (directionInd <= 0) {
            Alert("This is the beginning of the plan.");
            return;
        }
        directionInd--;
        updateDirections(directions);
    }

    public void onPlanBtnClicked(View view) {
        finish();
    }

    public void Alert(String msg) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}