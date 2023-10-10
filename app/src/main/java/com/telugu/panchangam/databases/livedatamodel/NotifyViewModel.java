package com.telugu.panchangam.databases.livedatamodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telugu.panchangam.databases.dbtables.NotificationsTable;
import com.telugu.panchangam.databases.repos.MyNotifyRepo;

import java.util.List;

public class NotifyViewModel extends AndroidViewModel {

    private final MyNotifyRepo repository;
    private final LiveData<List<NotificationsTable>> allData;


    public NotifyViewModel(@NonNull Application application) {
        super(application);
        repository = new MyNotifyRepo(application);
        allData = repository.getAllData();
    }
    public LiveData<List<NotificationsTable>> getAllData() {
        return allData;
    }

    public void insert(NotificationsTable entity) {
        repository.insertNotification(entity);
    }

    public void update(NotificationsTable entity) {
        repository.update(entity);
    }

    public void delete(NotificationsTable entity) {
        repository.delete(entity);
    }

}
