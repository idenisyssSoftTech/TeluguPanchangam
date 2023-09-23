package com.soumya.telugupanchangam.databases.livedatamodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.soumya.telugupanchangam.databases.dbtables.Eventdata;
import com.soumya.telugupanchangam.databases.repos.EventRepos;

import java.util.List;

public class EventLiveData extends AndroidViewModel {

    private final EventRepos repository;

    public EventLiveData(Application application) {
        super(application);
        repository = new EventRepos(application);
        LiveData<List<Eventdata>> allEvents = repository.getAllEvents();
    }

    public void insert(Eventdata eventdata){
        repository.insert(eventdata);
    }

    public LiveData<List<Eventdata>> getAllEvents() {
        return repository.getAllEvents();
    }

    public LiveData<List<Eventdata>> getEventsByDate(String date) {
        return repository.getEventsByDate(date);
    }
}
