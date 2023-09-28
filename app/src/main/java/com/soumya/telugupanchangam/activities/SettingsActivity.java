package com.soumya.telugupanchangam.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.LocaleList;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.adapter.SettingsAdapter;
import com.soumya.telugupanchangam.models.SettingsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG_NAME = SettingsActivity.class.getName();
    private  RecyclerView setting_recycler;
    private SettingsAdapter settingsAdapter;
    private LinearLayoutManager linearLayoutManager;
    List<SettingsModel> settingsModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setting_recycler = findViewById(R.id.settings_recyclerview);
        settingsModelList = new ArrayList<>();
        SettingsModel  s1 = new SettingsModel("0","General","G");
        SettingsModel  s2 = new SettingsModel("0","Language Change","");
        SettingsModel  s3 = new SettingsModel("0","Terms & Conditions","");
        SettingsModel  s4 = new SettingsModel("0","Privacy Policy","");
        settingsModelList.add(s1);
        settingsModelList.add(s2);
        settingsModelList.add(s3);
        settingsModelList.add(s4);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        settingsAdapter = new SettingsAdapter(getApplicationContext(),settingsModelList);
        setting_recycler.setAdapter(settingsAdapter);
        setting_recycler.setLayoutManager(linearLayoutManager);

    }

    // Code to change the app's language
    public void changeAppLanguage(Context context, String languageCode) {
        Configuration config = getResources().getConfiguration();
        Configuration newConfig = new Configuration(config);
        if(true) {
            //above or equal to API 24
            LocaleList currentLocales = config.getLocales();
            Locale newLocale = new Locale(languageCode); // Change to French
            LocaleList newLocales = new LocaleList(newLocale);
            // Update the app's configuration with the new locales
            newConfig.setLocales(newLocales);
        }
        else {
           // below API 24
            Locale currentLocale = config.locale;
            Locale newLocale = new Locale("fr"); // Change to French
            Locale.setDefault(newLocale);
            // Update the app's configuration with the new locale
            newConfig.locale = newLocale;

        }
        getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics());
    }
}