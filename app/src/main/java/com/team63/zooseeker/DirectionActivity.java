package com.team63.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DirectionActivity extends AppCompatActivity {
//    private ArrayList<Direction> directionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        Direction direction = getIntent().getParcelableExtra("direction");
        List<Step> steps = (List<Step>) getIntent().getSerializableExtra("steps");

        TextView exhibitView = findViewById(R.id.exhibit_view);
        TextView directionsView = findViewById(R.id.directions_view);

//        Log.d("tag", direction.name);
//        Log.d("tag", "" + direction.getDistance());
//        Log.d("tag", steps.get(0).toString());
        String directions = "";
        for (Step step : steps) {
            directions += step.toString();
        }
//        String steps = direction.steps.get(0).toString();
        directionsView.setText(directions);
    }
}