package com.team63.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * MainActivity is what Android looks for first when launching the app.
 * It is coupled with activity_main.xml as the default or main screen.
 */
public class MainActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private PlanViewModel viewModel;
    SharedPreferences preferences;

    /**
     * Run the main screen when the Application is first started.
     *
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // couple MainActivity with activity_main.xml
        setContentView(R.layout.activity_main);

        // put the JSON files into preferences
        preferences = getSharedPreferences("filenames", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (!preferences.contains("vertex_info")) {
            editor.putString("vertex_info", getString(R.string.vertex_info));
        }

        if (!preferences.contains("edge_info")) {
            editor.putString("edge_info", getString(R.string.edge_info));
        }

        if (!preferences.contains("zoo_graph")) {
            editor.putString("zoo_graph", getString(R.string.zoo_graph));
        }

        // important to set the sharedPref values before calling viewModel

        // use commit instead of apply to force this to happen now,
        // because this only happens when the app gets new JSON files and
        // we need it for viewModel
        editor.commit();

        // create ViewModelProvider factory, use it to get a PlanViewModel
        viewModel = new ViewModelProvider(this)
                .get(PlanViewModel.class);

        // fakeSearchBar serves as the supportActionBar to help give a
        // 'back arrow' to exit from searching.
        // See credits for SearchActivity.
        Toolbar fakeSearchBar = findViewById(R.id.fake_searchbar);
        setSupportActionBar(fakeSearchBar);

        // Adapter for our specific dataset to the RecyclerView
        MainAdapter adapter = new MainAdapter();
        // adapter.setHasStableIds(true); do this later;

        // set recyclerView to the recyclerView in activity_main.xml
        recyclerView = findViewById(R.id.pre_plan_items);

        // see credits for SearchActivity
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // divider to separate things in 'Planned Exhibits' and make them look nice
        // (there is a chance we don't want this as it almost makes them look clickable)
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        // give recyclerview the adapter so it can provide views
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
        // to know to setFocusable to false
        editText.setFocusable(false);

        // When editText is clicked, start a SearchActivity
        editText.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });
    }

    public void onPlanBtnClick(View view) {
        Map<String, ZooData.VertexInfo> vInfoMap =
                ZooData.loadVertexInfoJSON(this,
                        preferences.getString("vertex_info", "fail"));
        Log.d("Test", preferences.getString("edge_info", "fail"));
        Map<String, ZooData.EdgeInfo> eInfoMap =
                ZooData.loadEdgeInfoJSON(this,
                        preferences.getString("edge_info", "fail"));
        Graph<String, IdentifiedWeightedEdge> G =
                ZooData.loadZooGraphJSON(this,
                        preferences.getString("zoo_graph", "fail"));
        viewModel.generateDirections(G, vInfoMap, eInfoMap);

        Intent intent = new Intent(this, ViewPlanActivity.class);
        startActivity(intent);
    }

    /**
     * Updates the number of exhibits in the plan at the top of the main screen.
     * In the format 'Planned Exhibits (#)'.
     *
     * @param exhibits The List of planned exhibits.
     */
    public void updateExhibitCount(List<NodeInfo> exhibits) {
        TextView exhibitCount = findViewById(R.id.exhibit_count);
        exhibitCount.setText(String.format(Locale.US, "Planned Exhibits (%d)", exhibits.size()));
    }
}