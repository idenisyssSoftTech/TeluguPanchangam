package com.soumya.telugupanchangam.databases.livedatamodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.soumya.telugupanchangam.databases.dbtables.NotificationsTable;
import com.soumya.telugupanchangam.databases.repos.MyNotifyRepo;

import java.util.List;

public class NotifyViewModel extends AndroidViewModel {

    private MyNotifyRepo repository;
    private LiveData<List<NotificationsTable>> allData;


    public NotifyViewModel(@NonNull Application application) {
        super(application);
        repository = new MyNotifyRepo(application);
        allData = repository.getAllData();
    }
    public LiveData<List<NotificationsTable>> getAllData() {
        return allData;
    }

    public void insert(NotificationsTable entity) {
        repository.insert(entity);
    }

    public void update(NotificationsTable entity) {
        repository.update(entity);
    }

    public void delete(NotificationsTable entity) {
        repository.delete(entity);
    }

}
