package com.soumya.telugupanchangam.database2;


import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.soumya.telugupanchangam.database2.dao.NotificationDAO;
import com.soumya.telugupanchangam.database2.entities.NotificationsTable;

@Database(entities = {NotificationsTable.class}, version = 1)
public abstract class PanchangamDataBase extends RoomDatabase {
    private static PanchangamDataBase instance;
    public static synchronized PanchangamDataBase getInstance(Application application) {

        if (instance == null) {
            instance = Room.databaseBuilder(
                            application.getApplicationContext(),
                            PanchangamDataBase.class,
                            "panchang_notifications"
                    )
                    .fallbackToDestructiveMigration() // This is for migration strategy
                    .build();
        }
        return instance;
    }

    public abstract NotificationDAO notificationDAO();

}
