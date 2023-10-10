package com.telugu.panchangam.databases.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.telugu.panchangam.databases.AppDatabase;
import com.telugu.panchangam.databases.daos.EventDao;
import com.telugu.panchangam.databases.dbtables.Eventdata;

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

    public void insertEventRepo(Eventdata event) {
        new InsertAsyncTask(eventDao).execute(event);
    }


    public void updateEventRepo(Eventdata event) {
        new UpdateAsyncTask(eventDao).execute(event);
    }

    public void deleteEventRepo(Eventdata event) {
    new DeleteAsyncTask(eventDao).execute(event);
    }

    public LiveData<List<Eventdata>> getEventsByDate(String date) {
        return eventDao.getEventsByDate(date);
    }


    private static class InsertAsyncTask extends AsyncTask<Eventdata, Void, Void> {
        private final EventDao yourDao;

        private InsertAsyncTask(EventDao yourDao) {
            this.yourDao = yourDao;
        }

        @Override
        protected Void doInBackground(Eventdata... entities) {
            yourDao.insert(entities[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Eventdata, Void, Void> {
        private final EventDao yourDao;

        private UpdateAsyncTask(EventDao yourDao) {
            this.yourDao = yourDao;
        }

        @Override
        protected Void doInBackground(Eventdata... entities) {
            yourDao.update(entities[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Eventdata, Void, Void> {
        private final EventDao yourDao;

        private DeleteAsyncTask(EventDao yourDao) {
            this.yourDao = yourDao;
        }

        @Override
        protected Void doInBackground(Eventdata... entities) {
            yourDao.delete(entities[0]);
            return null;
        }
    }
}
