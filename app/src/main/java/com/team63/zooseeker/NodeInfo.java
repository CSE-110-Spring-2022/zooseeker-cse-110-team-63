package com.team63.zooseeker;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "node_info")
public class NodeInfo {
    // Public fields

    @PrimaryKey(autoGenerate = true)
    public String id;

    public String kind;
    public String name;
    public String[] tags;

    // Constructor matching fields above
    NodeInfo(String id, String kind, String name, String[] tags) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        System.arraycopy(tags, 0, this.tags, 0, tags.length);
    }

    // Factory method for loading our JSON
    public static List<NodeInfo> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeInfo>>(){}.getType();
            return gson.fromJson(reader, type);
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
}
