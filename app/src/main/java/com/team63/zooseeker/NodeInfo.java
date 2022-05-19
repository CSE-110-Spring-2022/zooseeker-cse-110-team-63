package com.team63.zooseeker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// Used to load json file, but cannot be loaded to DB because of string array
@Entity (tableName = "node_info")
public class NodeInfo {
    // Public fields
    @PrimaryKey (autoGenerate = true)
    public long fakeId;

    @NonNull
    public String id;
    public String kind;
    public String name;
    public String concatTags;
    public boolean selected = false;
    public int orderInPlan;

    @Ignore
    public String[] tags;

    NodeInfo() {}

    @Ignore
    NodeInfo(String id, String kind, String name, String[] tags) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        System.arraycopy(tags, 0, this.tags, 0, tags.length);
        this.concatTags = String.join(", ", tags);
        this.orderInPlan = -1;
    }

    // Factory method for loading our JSON
    public static List<NodeInfo> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeInfo>>(){}.getType();
            List<NodeInfo> fromJsonList = gson.fromJson(reader, type);
            // use for loop to populate concatTags field given auto-populated tags list
            for (int i = 0; i < fromJsonList.size(); i++) {
                fromJsonList.get(i).populateConcatTags();
            }
            return fromJsonList;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                ", kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }

    private void populateConcatTags() {
        this.concatTags = String.join(",", this.tags);
    }
}
