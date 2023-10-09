package com.soumya.telugupanchangam.databases.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.soumya.telugupanchangam.databases.PanchangamDataBase;
import com.soumya.telugupanchangam.databases.daos.NotificationDAO;
import com.soumya.telugupanchangam.databases.dbtables.NotificationsTable;

import java.util.List;

public class MyNotifyRepo {

    private final NotificationDAO notificationDAO;
    private final LiveData<List<NotificationsTable>> all_notify_data_list;

    public MyNotifyRepo(Application application){
        PanchangamDataBase database = PanchangamDataBase.getInstance(application);
        notificationDAO = database.notificationDAO();
        all_notify_data_list = notificationDAO.getallNotifyData();
    }


    public LiveData<List<NotificationsTable>> getAllData() {
        return all_notify_data_list;
    }

    public void insertNotification(NotificationsTable entity) {
        new InsertAsyncTask(notificationDAO).execute(entity);
    }

    public void update(NotificationsTable entity) {
        new UpdateAsyncTask(notificationDAO).execute(entity);
    }

    public void delete(NotificationsTable entity) {

      //  notificationDAO.delete(entity);
       new DeleteAsyncTask(notificationDAO).execute(entity);

    }

    private static class InsertAsyncTask extends AsyncTask<NotificationsTable, Void, Void> {
        private final NotificationDAO yourDao;

        private InsertAsyncTask(NotificationDAO yourDao) {
            this.yourDao = yourDao;
        }

        @Override
        protected Void doInBackground(NotificationsTable... entities) {
            yourDao.insert(entities[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<NotificationsTable, Void, Void> {
        private final NotificationDAO yourDao;

        private UpdateAsyncTask(NotificationDAO yourDao) {
            this.yourDao = yourDao;
        }

        @Override
        protected Void doInBackground(NotificationsTable... entities) {
            yourDao.update(entities[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<NotificationsTable, Void, Void> {
        private final NotificationDAO yourDao;

        private DeleteAsyncTask(NotificationDAO yourDao) {
            this.yourDao = yourDao;
        }

        @Override
        protected Void doInBackground(NotificationsTable... entities) {
            yourDao.delete(entities[0]);
            return null;
        }
    }
}
