package com.team63.zooseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private Button skipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        exhibitView = findViewById(R.id.exhibit_view);
        directionsView = findViewById(R.id.directions_view);
        nextBtn = findViewById(R.id.next_exhibit_btn);
        prevBtn = findViewById(R.id.previous_exhibit_btn);
        skipBtn = findViewById(R.id.skip_exhibit_btn);

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        directionInd = 0;
        directionsView.setMovementMethod(new ScrollingMovementMethod());

        LiveData<List<Direction>> liveData = planViewModel.getDirections();
        liveData.observe(this, this::updateDirections);

        prevBtn.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
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

        String destination = steps.get(steps.size() - 1).destination;
        directionsView.setText(dirStrings);
        exhibitView.setText(destination + "\n(" + cumDist + " ft)");

        SetBtnVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_btn:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNextBtnClicked(View view) {
        directionInd++;
        updateDirections(directions);
    }

    public void onPrevBtnClicked(View view) {
        directionInd--;
        updateDirections(directions);
    }

    public void onSkipBtnClicked(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder
                .setTitle("Alert!")
                .setMessage("Are you sure you want to skip this exhibit?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    planViewModel.recalculate(directionInd);
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void SetBtnVisibility() {
        if (directionInd == 0) prevBtn.setVisibility(View.GONE);
        else prevBtn.setVisibility(View.VISIBLE);

        if (directionInd == directions.size() - 1) nextBtn.setVisibility(View.GONE);
        else nextBtn.setVisibility(View.VISIBLE);

        if (directionInd == directions.size() - 1 || directions.size() == 2)
            skipBtn.setVisibility(View.GONE);
        else skipBtn.setVisibility(View.VISIBLE);
    }

    public void onPlanBtnClicked(View view) {
        planViewModel.generateDirections();
        finish();
    }
}