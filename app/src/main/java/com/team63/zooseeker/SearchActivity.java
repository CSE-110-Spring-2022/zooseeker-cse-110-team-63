package com.team63.zooseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

public class SearchActivity extends AppCompatActivity {
    PlanViewModel viewModel;
    SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        viewModel = new ViewModelProvider(this)
                .get(PlanViewModel.class);

        Toolbar toolbar = findViewById(R.id.navi_toolbar);

        // Credit to
        // https://stackoverflow.com/a/45655977
        // for teaching me how to even give a custom toolbar a natural back button
        // and also teaching me that I have to set the action bar to the toolbar
        // Credit to
        // https://stackoverflow.com/a/26515159
        // for teaching me to change app manifest
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchAdapter adapter = new SearchAdapter();

        // credit to
        // https://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
        // for divider tutorial
        RecyclerView recyclerView = findViewById(R.id.search_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickedHandler(viewModel::selectItem);
        viewModel.getExhibits().observe(this, adapter::setSearchItems);

        SearchView searchView = (SearchView) findViewById(R.id.nav_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.onActionViewExpanded();
    }

    // https://stackoverflow.com/questions/7479992/handling-a-menu-item-click-event-android
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                findViewById(R.id.nav_search).clearFocus();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}