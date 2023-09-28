package com.soumya.telugupanchangam.databases.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.soumya.telugupanchangam.databases.dbtables.Eventdata;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Eventdata eventdata);

    @Update
    void update(Eventdata eventdata);

    @Delete
    void delete(Eventdata eventdata);

    @Query("SELECT * FROM eventsdata")
    LiveData<List<Eventdata>> getAllEvents();



    @Query("SELECT * FROM eventsdata WHERE date = :date")
    LiveData<List<Eventdata>> getEventsByDate(String date);
}
