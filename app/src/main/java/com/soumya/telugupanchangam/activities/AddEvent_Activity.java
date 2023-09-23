package com.soumya.telugupanchangam.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.databases.dbtables.Eventdata;
import com.soumya.telugupanchangam.databases.livedatamodel.EventLiveData;
import com.soumya.telugupanchangam.services.NotificationService;
import com.soumya.telugupanchangam.utils.AppConstants;
import com.soumya.telugupanchangam.utils.PermissionUtils;
import com.soumya.telugupanchangam.utils.utils;

import java.util.Calendar;

import de.hdodenhof.circleimageview.BuildConfig;

public class AddEvent_Activity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton timePicker;
    private MaterialButton btn_saveEvent;
    private TextInputEditText eventName, eventTime, eventDescription;
    private TextView eventDate;
    private Spinner eventType;
    ArrayAdapter<String> adapterItems;

    private EventLiveData eventLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        //actionBar Title
        utils.setupActionBar(this,getResources().getString(R.string.action_addEvent));
        initViews();

    }

    private void initViews() {
        btn_saveEvent = findViewById(R.id.btnSaveEvent);
        btn_saveEvent.setOnClickListener(this);
        eventName = findViewById(R.id.EventName);
        eventTime = findViewById(R.id.eTime);
        eventDescription  = findViewById(R.id.Description);
        eventDate = findViewById(R.id.eventdate);
        eventType = findViewById(R.id.select_Event);
        timePicker = findViewById(R.id.eTime_fab);
        timePicker.setOnClickListener(this);

        adapterItems = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, AppConstants.items);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventType.setAdapter(adapterItems);

        eventLiveData = new ViewModelProvider(this).get(EventLiveData.class);
        String dateString = getIntent().getStringExtra(AppConstants.selectedDate);
        eventDate.setText(dateString);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSaveEvent){
            if(checkPermissionMethod()) {
                saveEvent();
            }
        }else if(v.getId()==R.id.eTime_fab){
            showTimePickerDialog();
        }

    }
    private void showTimePickerDialog() {
        int hour = AppConstants.calendar.get(Calendar.HOUR_OF_DAY);
        int minute = AppConstants.calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> setTimeToTextView(hourOfDay, minute1), hour, minute, false // 24-hour format (change to true for 24-hour format)
        );
        timePickerDialog.show();
    }

    private void setTimeToTextView(int hourOfDay, int minute) {
        AppConstants.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        AppConstants.calendar.set(Calendar.MINUTE, minute);
        eventTime.setText(AppConstants.timeFormat.format(AppConstants.calendar.getTime()));
    }
    @SuppressLint("ScheduleExactAlarm")
    private void saveEvent() {
        String name = eventName.getText().toString();
        String date = eventDate.getText().toString();
        String time = eventTime.getText().toString();
        String description = eventDescription.getText().toString();
        String selectedEventType = eventType.getSelectedItem().toString();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, AppConstants.emptyFields, Toast.LENGTH_SHORT).show();
            return;
        }
        Eventdata event = new Eventdata();
        event.setName(name);
        event.setDate(date);
        event.setTime(time);
        event.setDescription(description);
        event.setEventType(selectedEventType);

        // Insert the event into the database using a background thread
        new Thread(() -> {
            eventLiveData.insert(event);
            runOnUiThread(() -> {
                Toast.makeText(this, AppConstants.saveEvent, Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();

        long eventTimeInMillis = AppConstants.calendar.getTimeInMillis();
            Intent notificationServiceIntent = new Intent(this, NotificationService.class);
            notificationServiceIntent.putExtra(AppConstants.eventName, name);
            notificationServiceIntent.putExtra(AppConstants.eventDesc, description);
            notificationServiceIntent.putExtra(AppConstants.eventType, selectedEventType);
            notificationServiceIntent.putExtra(AppConstants.eventTime, eventTimeInMillis);
            startService(notificationServiceIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkPermissionMethod() {
        boolean isGranted = false;
        if (PermissionUtils.checkPermissions(this)) {
            isGranted = true;
            }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)){
                showPermissionRationaleDialog();
            }else{
                multiPermissionLancher.launch(new String[] {android.Manifest.permission.POST_NOTIFICATIONS});
            }
        }
        return isGranted;
    }

    private final ActivityResultLauncher<String[]> multiPermissionLancher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                Log.d("vfdcs", "vfc" + result);

                boolean allGranted = true;
                for (String key : result.keySet()) {
                    allGranted = allGranted && Boolean.TRUE.equals(result.get(key));
                }
                if (allGranted) {
                    Log.d("ALL PERMISSIONS","ALL Permissions granted");
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
                        multiPermissionLancher.launch(new String[] {Manifest.permission.POST_NOTIFICATIONS});
                }, getString(R.string.cancel),null);
    }


    private void showPermissionSettingsDialog() {
        PermissionUtils.showCustomDialog(this, "Post Notification Permission!..",
                "The app needs permission to function. Please allow this permission in the app settings. ",
                "Go To Settings", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:"+ BuildConfig.APPLICATION_ID));
                    startActivity(intent);
                },
                getString(R.string.cancel),null);
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