package com.team63.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NavActivity extends AppCompatActivity {
    ListView listView;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_menu);

        listView = findViewById(R.id.listView);

        NodeInfoDao nodeInfoDao = NodeDatabase.getSingleton(this).nodeInfoDao();
        String[] exhibits = nodeInfoDao.getExhibit();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,exhibits);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navi_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.nav_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search for exhibits");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}