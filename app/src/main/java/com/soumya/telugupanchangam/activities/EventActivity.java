package com.soumya.telugupanchangam.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.databases.dbtables.Eventdata;
import com.soumya.telugupanchangam.databases.livedatamodel.EventLiveData;
import com.soumya.telugupanchangam.databases.repos.EventRepos;
import com.soumya.telugupanchangam.utils.AppConstants;
import com.soumya.telugupanchangam.utils.utils;
import java.util.Calendar;
import java.util.List;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {
    private CalendarView eventCalenderView;
    private FloatingActionButton event_fab;
    private TextView eventResult;

    private String selectedDateString;
    private EventRepos eventRepos;
    private EventLiveData eventLiveData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initViews();
        eventRepos = new EventRepos(this.getApplication());
        eventLiveData = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EventLiveData.class);
        // Call this to set up initial observation
        updateLiveDataObservation(null);

    }

    private void initViews() {
        //actionBar Title
        utils.setupActionBar(this,getResources().getString(R.string.action_event));
        eventCalenderView = findViewById(R.id.EventCalenderView);
        eventResult = findViewById(R.id.event_result);
        event_fab = findViewById(R.id.event_fab);
        event_fab.setOnClickListener(this);

        eventCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                selectedDateString = AppConstants.dateFormat.format(selectedDate.getTime());
                // Update the LiveData observation when the user selects a date
                updateLiveDataObservation(selectedDateString);
            }
        });

    }
    private void updateLiveDataObservation(String selectedDate) {
        if (selectedDate != null && !selectedDate.isEmpty()) {
            // Observe events filtered by selected date
            eventLiveData.getEventsByDate(selectedDate).observe(this, new Observer<List<Eventdata>>() {
                @Override
                public void onChanged(List<Eventdata> events) {
                    Log.d("EventActivity", "Filtered LiveData onChanged with " + events.size() + " events");
                    // Update the event list when any changes occur
                    updateEventList(events);
                }
            });
        } else {
            // If no date is selected, display events for the current date
            String currentDate = utils.currentDate();
            eventLiveData.getEventsByDate(currentDate).observe(this, new Observer<List<Eventdata>>() {
                @Override
                public void onChanged(List<Eventdata> events) {
                    Log.d("EventActivity", "Filtered LiveData onChanged with " + events.size() + " events");
                    updateEventList(events);
                }
            });
        }
    }

    private void updateEventList(List<Eventdata> events) {
        Log.d("EventActivity", "updateEventList called with " + events.size() + " events");
        StringBuilder eventStringBuilder = new StringBuilder();
        for (Eventdata event : events) {
            eventStringBuilder.append("Name: ").append(event.getName()).append("\n");
            eventStringBuilder.append("Time: ").append(event.getTime()).append("\n");
            eventStringBuilder.append("Description: ").append(event.getDescription()).append("\n");
            eventStringBuilder.append("Event Type: ").append(event.getEventType()).append("\n\n");
        }
        eventResult.setText(eventStringBuilder.toString());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.event_fab){
            String currentDate = utils.currentDate();
            String dateToPass = selectedDateString != null ? selectedDateString : currentDate;
            Intent intent = new Intent(EventActivity.this, AddEvent_Activity.class);
            intent.putExtra(AppConstants.selectedDate, dateToPass); // Pass the date as a formatted string
            startActivity(intent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLiveDataObservation(selectedDateString);
    }
}