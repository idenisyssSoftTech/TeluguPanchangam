package com.soumya.telugupanchangam.databases.repos;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.soumya.telugupanchangam.databases.AppDatabase;
import com.soumya.telugupanchangam.databases.daos.EventDao;
import com.soumya.telugupanchangam.databases.dbtables.Eventdata;

import java.util.List;

public class EventRepos {
    private final EventDao eventDao;
    private final LiveData<List<Eventdata>> allEvents;

    public EventRepos(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        eventDao = db.eventDao();
        allEvents = eventDao.getAllEvents();
    }

    public LiveData<List<Eventdata>> getAllEvents() {
        return allEvents;
    }

    public void insert(Eventdata event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(event);
        });
    }

    public void update(Eventdata event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.update(event);
        });
    }

    public void delete(Eventdata event) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.delete(event);
        });
    }

    public LiveData<List<Eventdata>> getEventsByDate(String date) {
        return eventDao.getEventsByDate(date);
    }
}
