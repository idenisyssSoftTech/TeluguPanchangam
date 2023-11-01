package com.telugu.panchangam.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.telugu.panchangam.BuildConfig;
import com.telugu.panchangam.R;
import com.telugu.panchangam.databases.dbtables.Eventdata;
import com.telugu.panchangam.databases.livedatamodel.EventLiveData;
import com.telugu.panchangam.receivers.EventReminderReceiver;
import com.telugu.panchangam.utils.AppConstants;
import com.telugu.panchangam.utils.PermissionUtils;
import com.telugu.panchangam.utils.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddEvent_Activity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG_NAME = AddEvent_Activity.class.getName();

    private ImageButton timePicker;
    private Button btn_saveEvent;
    private TextInputEditText eventName, eventDescription;
    private TextView eventDate, eventTime;
    private Spinner eventType;
    ArrayAdapter<String> adapterItems;
    private String[] items;
    private EventLiveData eventLiveData;
    private Eventdata eventToEdit;
    private int selectedHour, selectedMinute;
    long selectedTimeMillis;
    long currentTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        //actionBar Title
        utils.setupActionBar(this, getResources().getString(R.string.action_addEvent));
        initViews();
        loadEventToEdit();
    }

    private void initViews() {
        btn_saveEvent = findViewById(R.id.btnSaveEvent);
        btn_saveEvent.setOnClickListener(this);
        eventName = findViewById(R.id.EventName);
        eventTime = findViewById(R.id.EventTime);
        eventDescription = findViewById(R.id.Description);
        eventDate = findViewById(R.id.eventdate);
        eventType = findViewById(R.id.select_Event);
        timePicker = findViewById(R.id.eTime_btn);
        timePicker.setOnClickListener(this);
        items = getResources().getStringArray(R.array.event_types);
        adapterItems = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventType.setAdapter(adapterItems);

        eventLiveData = new ViewModelProvider(this).get(EventLiveData.class);
        String dateString = getIntent().getStringExtra(AppConstants.selectedDate);
        eventDate.setText(dateString);

    }

    // Load event data for editing if provided in the intent
    private void loadEventToEdit() {
        eventToEdit = (Eventdata) getIntent().getSerializableExtra(AppConstants.EXTRA_EVENT_DATA);
        if (eventToEdit != null) {
            populateEventData(eventToEdit);
        }
    }

    // Populate UI fields with event data
    private void populateEventData(Eventdata event) {
        eventName.setText(eventToEdit.getName());
        eventTime.setText(eventToEdit.getTime());
        eventDescription.setText(eventToEdit.getDescription());
        eventDate.setText(eventToEdit.getDate());
        String eventTypeToEdit = eventToEdit.getEventType();
        int spinnerPosition = adapterItems.getPosition(eventTypeToEdit);
        eventType.setSelection(spinnerPosition);
        btn_saveEvent.setText(getString(R.string.update_event));
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSaveEvent) {
            if (checkPermissionMethod()) {
                try {
                    saveEvent();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (v.getId() == R.id.eTime_btn) {
            showTimePickerDialog();
        }

    }

    private void showTimePickerDialog() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1)
                -> {
            selectedHour = hourOfDay;   // Set the selected hour
            selectedMinute = minute1;
            setTimeToTextView(hourOfDay, minute1);
        }, hour, minute, false // 24-hour format (change to true for 24-hour format)
        );
        timePickerDialog.show();
    }

    private void setTimeToTextView(int hourOfDay, int minute) {
        AppConstants.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        AppConstants.calendar.set(Calendar.MINUTE, minute);
        String selectedTime = AppConstants.timeFormat.format(AppConstants.calendar.getTime());
        eventTime.setText(selectedTime);

    }

    private void saveEvent() throws ParseException {
        String name = eventName.getText().toString();
        String date = eventDate.getText().toString();
        String time = eventTime.getText().toString();
        String description = eventDescription.getText().toString();
        String selectedEventType = eventType.getSelectedItem().toString();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, AppConstants.emptyFields, Toast.LENGTH_SHORT).show();
            return;
        }
        // Extract the year, month, and day from the date string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date selectDate = dateFormat.parse(date);
        Calendar selectedDateCalendar = Calendar.getInstance();
        selectedDateCalendar.setTime(selectDate);
        selectedDateCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        selectedDateCalendar.set(Calendar.MINUTE, selectedMinute);
        selectedDateCalendar.set(Calendar.SECOND, 0);
        long selectedTimeMillis = selectedDateCalendar.getTimeInMillis();
        long currentTimeMillis = System.currentTimeMillis();

        Eventdata event = new Eventdata();
        event.setName(name);
        event.setDate(date);
        event.setTime(time);
        event.setDescription(description);
        event.setEventType(selectedEventType);
        event.setSelectedTimeMillis(selectedTimeMillis);

        Log.d(TAG_NAME,"selectedTimeMillis : "+selectedTimeMillis);
        Log.d(TAG_NAME,"currentTimeMillis : "+currentTimeMillis);
//        if (selectedTimeMillis > currentTimeMillis) {
            if (eventToEdit == null) {
                // New event: Insert the event into the Room Database
//                insertEvent(name, date, time, description, selectedEventType, selectedTimeMillis);
                insertEvent(event);
            } else {
                // Edit existing event: Update the event in the Room Database
                updateEvent(event);
            }
//        } else {
//            Toast.makeText(this, "Event time has already passed, please select a future time!", Toast.LENGTH_LONG).show();
//        }
        if (selectedTimeMillis > currentTimeMillis) {
            scheduleNotificationTime(event);
        }
    }

//    private void insertEvent(String name, String date, String time, String description, String selectedEventType, long selectedTimeMillis) {
    private void insertEvent(Eventdata event) {
        new Thread(() -> {
            eventLiveData.insertEvent(event);
            runOnUiThread(() -> {
                Toast.makeText(this, AppConstants.saveEvent, Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();

//        scheduleNotificationTime(event);
    }

    private void updateEvent(Eventdata event) {
        eventToEdit.setName(event.getName());
        eventToEdit.setDate(event.getDate());
        eventToEdit.setTime(event.getTime());
        eventToEdit.setDescription(event.getDescription());
        eventToEdit.setEventType(event.getEventType());
        eventToEdit.setSelectedTimeMillis(event.getSelectedTimeMillis());

        new Thread(() -> {
            eventLiveData.updateEvent(eventToEdit);
            runOnUiThread(() -> {
                Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();

        if (selectedTimeMillis > currentTimeMillis) {
            cancelAndRescheduleNotification(eventToEdit);
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotificationTime(Eventdata event) {
        Intent notificationIntent = new Intent(this, EventReminderReceiver.class);
        notificationIntent.putExtra(AppConstants.EXTRA_EVENT_DATA, event);


        int notificationId = utils.generateNotificationId();
        Log.d(TAG_NAME, "scheduled alarm for notificationId: " + notificationId);
        event.setNotificationId(notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                notificationId,
                notificationIntent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG_NAME, "current time : " + event.getSelectedTimeMillis());
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.RTC_WAKEUP,
                    event.getSelectedTimeMillis(),
                    pendingIntent
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.d(TAG_NAME, "current time : " + event.getSelectedTimeMillis());
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    event.getSelectedTimeMillis(),
                    pendingIntent
            );
        } else {
            Log.d(TAG_NAME, "current time : " + event.getSelectedTimeMillis());
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    event.getSelectedTimeMillis(),
                    pendingIntent
            );
        }

    }

    // Helper method to cancel and reschedule the notification for an updated event
    private void cancelAndRescheduleNotification(Eventdata event) {
        // Cancel the existing notification for the event
        cancelScheduledNotification(event);

        // Schedule a new notification for the updated event
        scheduleNotificationTime(event);
    }

    private void cancelScheduledNotification(Eventdata event) {
        // Retrieve the notificationId associated with the event
        int notificationId = event.getNotificationId();
        Log.d(TAG_NAME, "Canceling alarm for notificationId: " + notificationId);

        Intent notificationIntent = new Intent(this, EventReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                notificationId,
                notificationIntent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d(TAG_NAME, "Alarm canceled for notificationId: " + notificationId);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkPermissionMethod() {
        boolean isGranted = false;
        if (PermissionUtils.checkPermissions(this)) {
            isGranted = true;
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showPermissionRationaleDialog();
            } else {
                multiPermissionLancher.launch(new String[]{android.Manifest.permission.POST_NOTIFICATIONS});
            }
        }
        return isGranted;
    }

    private final ActivityResultLauncher<String[]> multiPermissionLancher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                boolean allGranted = true;
                for (String key : result.keySet()) {
                    allGranted = allGranted && Boolean.TRUE.equals(result.get(key));
                }
                if (allGranted) {
                    Log.d(TAG_NAME, "ALL Permissions granted");
                } else {
                    showPermissionSettingsDialog();
                }

            });

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void showPermissionRationaleDialog() {
        PermissionUtils.showCustomDialog(this, "Post Notification Permission",
                "This app needs the Post Notification permission. Please allow the permission.",
                getString(R.string.ok), (dialog, which) -> {
//                        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                    multiPermissionLancher.launch(new String[]{Manifest.permission.POST_NOTIFICATIONS});
                }, getString(R.string.cancel), null);
    }


    private void showPermissionSettingsDialog() {
        PermissionUtils.showCustomDialog(this, "Post Notification Permission!..",
                "The app needs permission to function. Please allow this permission in the app settings. ",
                "Go To Settings", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    startActivity(intent);
                },
                getString(R.string.cancel), null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}