package com.telugu.panchangam.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.telugu.panchangam.databases.daos.EventDao;
import com.telugu.panchangam.databases.dbtables.Eventdata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Eventdata.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract EventDao eventDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "event_database.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
