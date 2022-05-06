package com.team63.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jgrapht.Graph;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private PlanViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this)
                .get(PlanViewModel.class);

        Toolbar fakeSearchBar = findViewById(R.id.fake_searchbar);

        // see credits for SearchActivity
        setSupportActionBar(fakeSearchBar);

        MainAdapter adapter = new MainAdapter();
        // adapter.setHasStableIds(true); do this later;

        recyclerView = findViewById(R.id.pre_plan_items);

        // see credits for SearchActivity
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adapter);
        viewModel.getSelectedExhibits().observe(this, adapter::setPrePlanItems);
        viewModel.getSelectedExhibits().observe(this, this::updateExhibitCount);

        SearchView fakeSearchView = findViewById(R.id.fake_search);

        // https://stackoverflow.com/a/31104529
        // taught me that I have to get the EditText object inside the fakeSearchView
        EditText editText = (EditText) fakeSearchView.findViewById(
                // https://stackoverflow.com/a/62012217
                androidx.appcompat.R.id.search_src_text
        );

        // https://www.trinea.cn/android/searchview-setonclicklistener-not-working/
        // to know to setFocusable to fales
        editText.setFocusable(false);
        editText.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });


    }

    public void onPlanBtnClick(View view) {
        Map<String, ZooData.VertexInfo> vInfoMap =
                ZooData.loadVertexInfoJSON(this, ZooData.VERTEX_INFO_FILE);
        Map<String, ZooData.EdgeInfo> eInfoMap =
                ZooData.loadEdgeInfoJSON(this, ZooData.EDGE_INFO_FILE);
        Graph<String, IdentifiedWeightedEdge> G =
                ZooData.loadZooGraphJSON(this, ZooData.GRAPH_INFO_FILE);
        viewModel.generateDirections(G, vInfoMap, eInfoMap);

        Intent intent = new Intent(this, ViewPlanActivity.class);
        startActivity(intent);
    }

    public void updateExhibitCount(List<NodeInfo> exhibits) {
        TextView exhibitCount = findViewById(R.id.exhibit_count);
        exhibitCount.setText(String.format(Locale.US, "Planned Exhibits (%d)", exhibits.size()));
    }
}