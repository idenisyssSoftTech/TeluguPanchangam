package com.soumya.telugupanchangam.database2.repos;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.soumya.telugupanchangam.database2.PanchangamDataBase;
import com.soumya.telugupanchangam.database2.dao.NotificationDAO;
import com.soumya.telugupanchangam.database2.entities.NotificationsTable;

import java.util.List;

public class MyNotifyRepo {

    private NotificationDAO notificationDAO;
    private LiveData<List<NotificationsTable>> all_notify_data_list;

    public MyNotifyRepo(Application application){
        PanchangamDataBase database = PanchangamDataBase.getInstance(application);
        notificationDAO = database.notificationDAO();
        all_notify_data_list = notificationDAO.getallNotifyData();
    }


    public LiveData<List<NotificationsTable>> getAllData() {
        return all_notify_data_list;
    }

    public void insert(NotificationsTable entity) {
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
        private NotificationDAO yourDao;

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
        private NotificationDAO yourDao;

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
        private NotificationDAO yourDao;

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
