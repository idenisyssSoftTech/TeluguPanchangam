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

    public static String updateMonth(int currentMonth, int currentYear, int currentDay){
        // Set the locale to Telugu
        Locale teluguLocale = new Locale("te");
        // Create a DateFormat to display the month name
        SimpleDateFormat monthFormat = new SimpleDateFormat("EEEE,dd MMMM yyyy", teluguLocale);

        // Set the formatted month and year as the TextView's text
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currentDay);
        return monthFormat.format(calendar.getTime());
    }

    public static String updateFestivalMonth(int currentMonth, int currentYear){
        // Set the locale to Telugu
        Locale teluguLocale = new Locale("te");
        // Create a DateFormat to display the month name
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM - yyyy", teluguLocale);

        // Set the formatted month and year as the TextView's text
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        return monthFormat.format(calendar.getTime());
    }
    public static int generateNotificationId() {
        return (int) System.currentTimeMillis();
    }

    public static String getMonthName(int month) {
        switch (month) {
            case 0:
                return "jan";
            case 1:
                return "feb";
            case 2:
                return "mar";
            case 3:
                return "apr";
            case 4:
                return "may";
            case 5:
                return "jun";
            case 6:
                return "jul";
            case 7:
                return "aug";
            case 8:
                return "sep";
            case 9:
                return "oct";
            case 10:
                return "nov";
            case 11:
                return "dec";

            default:
                return "";
        }
    }
}
