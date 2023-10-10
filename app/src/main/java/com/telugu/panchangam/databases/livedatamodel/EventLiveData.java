package com.telugu.panchangam.databases.livedatamodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telugu.panchangam.databases.dbtables.Eventdata;

import java.util.List;

public class EventLiveData extends AndroidViewModel {

    private final com.telugu.panchangam.databases.repos.EventRepos repository;

    public EventLiveData(Application application) {
        super(application);
        repository = new com.telugu.panchangam.databases.repos.EventRepos(application);
        LiveData<List<Eventdata>> allEvents = repository.getAllEvents();
    }

    public void insertEvent(Eventdata eventdata){
        repository.insertEventRepo(eventdata);
    }

    public void updateEvent(Eventdata eventdata){
        repository.updateEventRepo(eventdata);
    }
    public void deleteEvent(Eventdata eventdata){
        repository.deleteEventRepo(eventdata);
    }
    public LiveData<List<Eventdata>> getAllEvents() {
        return repository.getAllEvents();
    }

    public LiveData<List<Eventdata>> getEventsByDate(String date) {
        return repository.getEventsByDate(date);
    }
}
