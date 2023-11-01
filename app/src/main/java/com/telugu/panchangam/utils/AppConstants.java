package com.telugu.panchangam.utils;

import com.telugu.panchangam.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppConstants {
    public static Calendar calendar = Calendar.getInstance();
    public static String CHANNEL_ID = "event_channel_id";
    public static String CHANNEL_NAME = "EventNotifications";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    public static String eventName = "eventName";
    public static String eventDesc = "description";
    public static String eventTime = "eventTime";
    public static String eventDate = "eventDate";
    public static String eventType = "eventType";
    public static String emptyFields = "Please fill in all fields";
    public static String selectedDate = "selectedDate";
    public static String saveEvent = "Event saved!";
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale. getDefault());

    public static final String EXTRA_FRAGMENT_TO_SHOW = "fragment_to_show";

    // Define constants for fragments
    public static final int FRAGMENT_NOTIFICATIONS = 1;
    public static final int FRAGMENT_NOTIFICATIONS1 = R.id.navigation_notifications;

    public static final String EXTRA_EVENT_DATA = "extra_event_data_key";
    public static final int MIN_YEAR = 2023;
    public static final int MAX_YEAR = 2024;
    public static final int MAX_MONTH = 11;

    public static final String DATABASE_NAME = "TPCData15.db";
    public static final int DATABASE_VERSION = 2;
    public static String appUrl = "https://play.google.com/store/apps/details?id=com.telugu.panchangam";
    public static String QrBarAppUrl = "https://play.google.com/store/apps/details?id=com.abhiram.qrbarscanner";


}
