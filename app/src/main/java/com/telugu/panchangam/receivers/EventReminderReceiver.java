        package com.telugu.panchangam.receivers;

        import android.Manifest;
        import android.app.Application;
        import android.app.Notification;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.util.Log;

        import androidx.core.app.ActivityCompat;
        import androidx.core.app.NotificationCompat;
        import androidx.core.app.NotificationManagerCompat;
        import androidx.core.content.ContextCompat;

        import com.telugu.panchangam.R;
        import com.telugu.panchangam.activities.HomeActivity;
        import com.telugu.panchangam.databases.dbtables.Eventdata;
        import com.telugu.panchangam.databases.dbtables.NotificationsTable;
        import com.telugu.panchangam.databases.repos.MyNotifyRepo;
        import com.telugu.panchangam.utils.AppConstants;
        import com.telugu.panchangam.utils.utils;

        public class EventReminderReceiver extends BroadcastReceiver {

            String TAG_NAME = EventReminderReceiver.class.getName();
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG_NAME,"Broadcast start");


                // Extract event information from the intent
    //            Eventdata event = intent.getParcelableExtra(AppConstants.EXTRA_EVENT_DATA);
                Eventdata eventData = (Eventdata) intent.getSerializableExtra(AppConstants.EXTRA_EVENT_DATA);
                if (eventData != null) {
                    String eventName = eventData.getName();
                    String description = eventData.getDescription();
                    String eventType = eventData.getEventType();
                    String eventTime = eventData.getTime();
                    String eventDate = eventData.getDate();


                    Intent openAppIntent = new Intent(context, HomeActivity.class);
                    openAppIntent.putExtra("FRAGMENT_TO_SHOW", "notifications");
//                    openAppIntent.putExtra(AppConstants.EXTRA_FRAGMENT_TO_SHOW, AppConstants.FRAGMENT_NOTIFICATIONS1);
                    openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent contentIntent = PendingIntent.getActivity(
                            context,
                            utils.generateNotificationId(),
                            openAppIntent,
                            PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
                    );

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createNotificationChannel(context);
                    }

                    Log.d(TAG_NAME, "create notificationCompat");
                    // Create the notification
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID)
                            .setSmallIcon(R.drawable.app_icon_om)
                            .setContentTitle(eventName)
                            .setContentText(description)
                            .setSubText(eventType)
                            .setColor(ContextCompat.getColor(context, R.color.purple_500))
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
                    Log.d(TAG_NAME, "send notification");
                    // Insert notification data into the database
                    insertNotificationData(context, eventName, description, eventType, eventTime, eventDate);
                }
            }

            private void createNotificationChannel(Context context) {
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
            }

            private void insertNotificationData(Context context, String eventName, String description, String eventType, String eventTime, String eventDate) {
                NotificationsTable item = new NotificationsTable();
                item.setNotify_event_name(eventName);
                item.setNotify_description(description);
                item.setNotify_eventtype(eventType);
                item.setNotify_time(eventTime);
                item.setNotify_date(eventDate);

                // Initialize the repository and insert the data
                MyNotifyRepo repo = new MyNotifyRepo((Application) context.getApplicationContext());
                repo.insertNotification(item);
            }
        }
