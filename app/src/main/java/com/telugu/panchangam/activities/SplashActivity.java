package com.telugu.panchangam.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.telugu.panchangam.HomeActivity;
import com.telugu.panchangam.R;
import com.telugu.panchangam.utils.PanchangConstants;
import com.telugu.panchangam.utils.PanchangSharedPref;

import java.util.Locale;

@SuppressLint("CustomSplashScreen")
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
        String languangecode = sharedPreferences.getStringVal(PanchangConstants.SELECTEDLAUNGE_CODE, PanchangConstants.LANG_TELUGU);
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
    @SuppressLint("ObsoleteSdkInt")
    public void changeAppLanguage(Context context, String languageCode) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // For Android Nougat (API 24) and above
            configuration.setLocale(new Locale(languageCode));
        } else {
            // For versions below Android Nougat
            configuration.locale = new Locale(languageCode);
        }

        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, displayMetrics);

//        Configuration config = getResources().getConfiguration();
//        Configuration newConfig = new Configuration(config);
//        if(true) {
//            //above or equal to API 24
//            LocaleList currentLocales = config.getLocales();
//            Locale newLocale = new Locale(languageCode); // Change to French
//            LocaleList newLocales = new LocaleList(newLocale);
//            // Update the app's configuration with the new locales
//            newConfig.setLocales(newLocales);
//        }
//        else {
//            // below API 24
//            Locale currentLocale = config.locale;
//            Locale newLocale = new Locale("fr"); // Change to French
//            Locale.setDefault(newLocale);
//            // Update the app's configuration with the new locale
//            newConfig.locale = newLocale;
//
//        }
//        getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics());
    }
}