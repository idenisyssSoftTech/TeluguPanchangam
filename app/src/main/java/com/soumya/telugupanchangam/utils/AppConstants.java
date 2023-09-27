package com.soumya.telugupanchangam.utils;

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
    public static String eventType = "eventType";
    public static String emptyFields = "Please fill in all fields";
    public static String selectedDate = "selectedDate";
    public static String saveEvent = "Event saved!";
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
}
