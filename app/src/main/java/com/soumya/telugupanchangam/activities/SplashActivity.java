package com.soumya.telugupanchangam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.widget.CheckBox;
import android.widget.TextView;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.utils.PanchangConstants;
import com.soumya.telugupanchangam.utils.PanchangSharedPref;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG_NAME = SplashActivity.class.getName();
    private static final int SPLASH_DURATION = 3000;

    private TextView app_title_tv, termstv,privacy_tv;
    private CheckBox terms_check;
    Handler handler;
     PanchangSharedPref sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = PanchangSharedPref.getInstance(this);
        termstv = findViewById(R.id.termstextview);
        app_title_tv = findViewById(R.id.app_title_tv);
        privacy_tv = findViewById(R.id.privacy_textview);
        terms_check = findViewById(R.id.termsCheckbox);
        // Retrieve the checkbox state from shared preferences and set it
        String languangecode = sharedPreferences.getStringVal(PanchangConstants.SLECTEDLAUNGE_CODE, PanchangConstants.LANG_TELUGU);
        if(languangecode != null && !languangecode.isEmpty()){
            changeAppLanguage(getApplicationContext(),languangecode);
            app_title_tv.setText(R.string.app_name);
        }
        else {
            changeAppLanguage(getApplicationContext(),PanchangConstants.LANG_TELUGU);
            app_title_tv.setText(R.string.app_name);
        }
        handler =  new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_DURATION);
    }
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