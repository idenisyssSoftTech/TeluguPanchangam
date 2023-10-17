package com.telugu.panchangam.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.telugu.panchangam.R;
import com.telugu.panchangam.adapters.EventAdapter;
import com.telugu.panchangam.databases.callbacksinterfaces.DeleteEventItemCallback;
import com.telugu.panchangam.databases.dbtables.Eventdata;
import com.telugu.panchangam.databases.livedatamodel.EventLiveData;
import com.telugu.panchangam.databases.repos.EventRepos;
import com.telugu.panchangam.receivers.EventReminderReceiver;
import com.telugu.panchangam.utils.AppConstants;
import com.telugu.panchangam.utils.utils;

import java.util.Calendar;
import java.util.List;

public class EventActivity extends AppCompatActivity implements View.OnClickListener, DeleteEventItemCallback , EventAdapter.EditEventCallback {
    private final String TAG_NAME = "EventActivity";
    private CalendarView eventCalenderView;
    private FloatingActionButton event_fab;
    private NestedScrollView scrollView;
    private String selectedDateString;
    private EventRepos eventRepos;
    private EventLiveData eventLiveData;
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private boolean isTextVisible = true;

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
        utils.setupActionBar(this,getResources().getString(R.string.events));
        eventCalenderView = findViewById(R.id.EventCalenderView);
        event_fab = findViewById(R.id.event_fab);
        event_fab.setOnClickListener(this);
        scrollView = findViewById(R.id.eventScrollview);

        eventAdapter = new EventAdapter(this,this);
        eventRecyclerView = findViewById(R.id.event_recyc);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(eventAdapter);

        eventCalenderView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            selectedDateString = AppConstants.dateFormat.format(selectedDate.getTime());
            // Update the LiveData observation when the user selects a date
            updateLiveDataObservation(selectedDateString);
        });
        final int[] oldScrollY = {scrollView.getScrollY()};
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY > oldScrollY[0]) {
                    // Scrolling down
                    if (isTextVisible) {
                        event_fab.hide();
                        isTextVisible = false;
                    }
                } else if (scrollY < oldScrollY[0]) {
                    // Scrolling up
                    if (!isTextVisible) {
                        event_fab.show();
                        isTextVisible = true;
                    }
                }
                oldScrollY[0] = scrollY;
            }
        });

    }
    private void updateLiveDataObservation(String selectedDate) {
        if (selectedDate != null && !selectedDate.isEmpty()) {
            // Observe events filtered by selected date
            eventLiveData.getEventsByDate(selectedDate).observe(this, events -> {
                Log.d(TAG_NAME, "Filtered LiveData onChanged with " + events.size() + " events");
                // Update the event list when any changes occur
                updateEventList(events);
            });
        } else {
            // If no date is selected, display events for the current date
            String currentDate = utils.currentDate();
            eventLiveData.getEventsByDate(currentDate).observe(this, events -> {
                Log.d(TAG_NAME, "Filtered LiveData onChanged with " + events.size() + " events");
                updateEventList(events);
            });
        }
    }

    private void updateEventList(List<Eventdata> events) {
        Log.d(TAG_NAME, "updateEventList called with " + events.size() + " events");
        eventAdapter.submitList(events);
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

    @Override
    public void onDeleteItem(Eventdata event) {
        cancelScheduledNotification(event);
    }

    private void cancelScheduledNotification(Eventdata event) {
        // Retrieve the notificationId associated with the event
        int notificationId = event.getNotificationId();
        Log.d(TAG_NAME, "Canceling alarm for notificationId: " + notificationId);

        Intent notificationIntent = new Intent(EventActivity.this, EventReminderReceiver.class);
        notificationIntent.putExtra(AppConstants.EXTRA_EVENT_DATA, event);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                notificationId,
                notificationIntent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d(TAG_NAME, "Alarm canceled for notificationId: " + notificationId);

        // Delete the event from the database
        eventLiveData.deleteEvent(event);
        Log.d(TAG_NAME, "Event deleted from the database.");

    }

    @Override
    public void onEditEvent(Eventdata event) {
        // Create an intent to start the EditEventActivity
        Intent intent = new Intent(this, AddEvent_Activity.class);
        intent.putExtra(AppConstants.EXTRA_EVENT_DATA, event);
        startActivity(intent);
    }
}