    package com.soumya.telugupanchangam.receivers;

    import android.Manifest;
    import android.app.Notification;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.os.Build;

    import androidx.core.app.ActivityCompat;
    import androidx.core.app.NotificationCompat;
    import androidx.core.app.NotificationManagerCompat;

    import com.soumya.telugupanchangam.R;
    import com.soumya.telugupanchangam.activities.EventActivity;
    import com.soumya.telugupanchangam.utils.AppConstants;
    import com.soumya.telugupanchangam.utils.utils;

    public class EventReminderReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            String eventName = intent.getStringExtra(AppConstants.eventName);
            String description = intent.getStringExtra(AppConstants.eventDesc);
            String eventType = intent.getStringExtra(AppConstants.eventType);

            Intent openAppIntent = new Intent(context, EventActivity.class);
            openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(
                    context,
                    utils.generateNotificationId(),
                    openAppIntent,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        AppConstants.CHANNEL_ID,
                        AppConstants.CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // Create the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.app_icon_om)
                    .setContentTitle(eventName)
                    .setContentText(description)
                    .setSubText(eventType)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);

            // Show the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManager.notify(utils.generateNotificationId(), builder.build());
        }



    }
