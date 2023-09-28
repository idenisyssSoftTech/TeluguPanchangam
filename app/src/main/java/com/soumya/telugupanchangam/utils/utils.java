package com.soumya.telugupanchangam.utils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class utils {

    public static String currentDate(){
        return AppConstants.dateFormat.format(Calendar.getInstance().getTime());
    }

    public static void setupActionBar(AppCompatActivity activity,String title) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static String updateMonth(int currentMonth, int currentYear){
        // Create a DateFormat to display the month name
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        // Set the formatted month and year as the TextView's text
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        return monthFormat.format(calendar.getTime());
    }
    public static int generateNotificationId() {
        return (int) System.currentTimeMillis();
    }
}
