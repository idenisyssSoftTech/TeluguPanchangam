package com.soumya.telugupanchangam.services;

import static com.soumya.telugupanchangam.utils.AppConstants.eventTime;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.AlarmManagerCompat;

import com.soumya.telugupanchangam.receivers.EventReminderReceiver;
import com.soumya.telugupanchangam.utils.AppConstants;
import com.soumya.telugupanchangam.utils.utils;


public class NotificationService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String eventName = intent.getStringExtra(AppConstants.eventName);
        String description = intent.getStringExtra(AppConstants.eventDesc);
        String eventType = intent.getStringExtra(AppConstants.eventType);
        long eventTimeMillis = intent.getLongExtra(eventTime,0);

        // Schedule your notifications here (use event date and time)
        scheduleNotification(eventName, description, eventType,eventTimeMillis);
        return START_STICKY;
    }

    @SuppressLint({"ScheduleExactAlarm", "ObsoleteSdkInt"})
    private void scheduleNotification(String eventName, String description, String eventType, long eventTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, EventReminderReceiver.class);
        notificationIntent.putExtra(AppConstants.eventName, eventName);
        notificationIntent.putExtra(AppConstants.eventDesc, description);
        notificationIntent.putExtra(AppConstants.eventType, eventType);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                utils.generateNotificationId(),
                notificationIntent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );
        long currentTimeInMillis = System.currentTimeMillis();
        if (eventTimeMillis > currentTimeInMillis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.RTC_WAKEUP,
                    eventTimeMillis,
                    pendingIntent
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    eventTimeMillis,
                    pendingIntent
            );
        } else {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    eventTimeMillis,
                    pendingIntent
            );
        }
    } else {
            Toast.makeText(this, "Event time has already passed", Toast.LENGTH_SHORT).show();
        }

        }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
