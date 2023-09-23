package com.soumya.telugupanchangam.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PanchangSharedPref {
    private static final String PREFS_NAME = "MyPanchangSharePref";
    private static PanchangSharedPref instance;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private PanchangSharedPref(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static PanchangSharedPref getInstance(Context context) {
        if (instance == null) {
            instance = new PanchangSharedPref(context);
        }
        return instance;
    }

    // Store a string value in shared preferences
    public void saveStringVal(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    // Retrieve a string value from shared preferences
    public String  getStringVal(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    // Store a boolean value in shared preferences
    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Retrieve a boolean value from shared preferences
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }
}
