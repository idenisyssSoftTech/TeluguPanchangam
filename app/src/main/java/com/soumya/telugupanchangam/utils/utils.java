package com.soumya.telugupanchangam.utils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Calendar;

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
    public static int generateNotificationId() {
        return (int) System.currentTimeMillis();
    }
}
