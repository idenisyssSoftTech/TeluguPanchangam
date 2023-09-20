package com.soumya.telugupanchangam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CheckBox;
import android.widget.TextView;

import com.soumya.telugupanchangam.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG_NAME = SplashActivity.class.getName();
    private static final int SPLASH_DURATION = 3000;

    private TextView termstv,privacy_tv;
    private CheckBox terms_check;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        termstv = findViewById(R.id.termstextview);
        privacy_tv = findViewById(R.id.privacy_textview);
        terms_check = findViewById(R.id.termsCheckbox);

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
}