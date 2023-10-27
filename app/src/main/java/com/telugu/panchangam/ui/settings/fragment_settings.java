package com.telugu.panchangam.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.telugu.panchangam.BuildConfig;
import com.telugu.panchangam.R;


public class fragment_settings extends Fragment implements View.OnClickListener {
    private Button shareApp, rateUs, shareData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView versionID = root.findViewById(R.id.version);
        versionID.setText(BuildConfig.VERSION_NAME);
        shareApp = root.findViewById(R.id.share_button);
        shareApp.setOnClickListener(this);

        rateUs = root.findViewById(R.id.rate_button);
        rateUs.setOnClickListener(this);

        shareData = root.findViewById(R.id.screenshot_button);
        shareData.setOnClickListener(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if(v == shareApp){
            shareAppMethod();
        } else if (v == rateUs) {
//            openDailpad();
            rateUsMethod();
        }
    }


    private void rateUsMethod() {
        String appPackageName = "com.abhiram.qrbarscanner";
        Uri contentUri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, contentUri);
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // Handle the case where the Play Store app is not available (you can add a message or alternative action).
        }
    }


    private void shareAppMethod() {
        String appUrl = "https://play.google.com/store/apps/details?id=com.abhiram.qrbarscanner";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
        startActivity(Intent.createChooser(intent, "Share with"));
    }


    private void openDailpad() {
        String phoneNumber = "123456789";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // Handle the case where the phone dialer app is not available (you can add a message or alternative action).
        }
    }
    private void openAppInPlayStore() {
        String appName = "QRBarScanner"; // Replace with your app's name
        // Create a Uri for the Play Store search
        Uri searchUri = Uri.parse("https://play.google.com/store/search?q=" + Uri.encode(appName));

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, searchUri);
            intent.setPackage("com.android.vending"); // Specify the Play Store app package name
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // Handle the case where the Play Store app is not available (you can add a message or alternative action).
        }
    }
}

//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));