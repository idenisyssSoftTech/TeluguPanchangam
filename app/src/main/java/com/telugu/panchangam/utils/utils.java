package com.telugu.panchangam.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.telugu.panchangam.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public static String updateFestivalMonth(int currentMonth, int currentYear, Context context){
//        // Set the locale to Telugu
//        Locale teluguLocale = new Locale("te");
//        // Create a DateFormat to display the month name
//        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM - yyyy", teluguLocale);
//
//        // Set the formatted month and year as the TextView's text
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, currentYear);
//        calendar.set(Calendar.MONTH, currentMonth);
//        return monthFormat.format(calendar.getTime());

        Resources res = context.getResources();
        String[] teluguMonthNames = res.getStringArray(R.array.telugu_month_names);
        // Get the Telugu month name for the current month
        String teluguMonthName = teluguMonthNames[currentMonth];
        // Format the month name along with the year

        return teluguMonthName + " - " + currentYear;
    }

    public static SpannableStringBuilder spanString(Context context, String eventsText, String textToColor){
        SpannableStringBuilder  spannableString = new SpannableStringBuilder (eventsText + textToColor);
        int customColor = ContextCompat.getColor(context, R.color.purple_500);
        // Set text color for "events" part
        spannableString.setSpan(
                new ForegroundColorSpan(customColor),
                0,
                eventsText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Set bold style for the entire span
        StyleSpan boldSpan = new StyleSpan(android.graphics.Typeface.BOLD);
        spannableString.setSpan(
                boldSpan,
                0,
                eventsText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return spannableString;
    }

    public static File saveBitmapToFile(Context context, Bitmap bitmap) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp_image", ".jpg",context.getCacheDir());
            FileOutputStream outStream = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
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
