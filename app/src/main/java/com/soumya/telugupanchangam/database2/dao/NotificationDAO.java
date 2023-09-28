package com.soumya.telugupanchangam.database2.dao;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.soumya.telugupanchangam.database2.entities.NotificationsTable;

import java.util.List;

@Dao
public interface NotificationDAO {

    @Insert
    void insert(NotificationsTable entity);

    @Update
    void update(NotificationsTable entity);

    @Delete
    void delete(NotificationsTable entity);

    @Query("DELETE FROM notifications")
    void deleteAll();

    @Query("SELECT * FROM notifications")
    LiveData<List<NotificationsTable>> getallNotifyData();
}
