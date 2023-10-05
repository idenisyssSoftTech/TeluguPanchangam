package com.soumya.telugupanchangam.databases.daos;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.soumya.telugupanchangam.databases.dbtables.NotificationsTable;

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
