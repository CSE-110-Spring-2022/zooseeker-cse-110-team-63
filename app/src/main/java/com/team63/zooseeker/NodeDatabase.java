package com.team63.zooseeker;

import static android.content.Context.MODE_PRIVATE;
import static com.team63.zooseeker.NodeInfo.loadJSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Database(entities = {NodeInfo.class, DirectionInfo.class, Step.class}, version = 2)
public abstract class NodeDatabase extends RoomDatabase {
    private static NodeDatabase singleton = null;

    public abstract NodeInfoDao nodeInfoDao();

    public abstract DirectionDao directionDao();

    public static void resetDatabase(Context context) {
        singleton.clearAllTables();
        List<NodeInfo> nodeInfos = loadJSON(context,
                context.getSharedPreferences("filenames", MODE_PRIVATE)
                        .getString("vertex_info", "fail"));
        singleton.nodeInfoDao().insertAll(nodeInfos);
    }

    public synchronized static NodeDatabase getSingleton(Context context) {
        if (singleton == null) {
            singleton = NodeDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static NodeDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, NodeDatabase.class, "node_info.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        List<NodeInfo> nodeInfos = loadJSON(context,
                                context.getSharedPreferences("filenames", MODE_PRIVATE)
                                        .getString("vertex_info", "fail"));
                        Executors.newSingleThreadExecutor().execute(() -> {
                            getSingleton(context).nodeInfoDao().insertAll(nodeInfos);
                        });
                    }
                })
                .build();
    }
}
