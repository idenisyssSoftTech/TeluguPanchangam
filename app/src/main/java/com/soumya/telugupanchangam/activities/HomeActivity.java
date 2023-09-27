package com.soumya.telugupanchangam.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.databinding.ActivityHomeBinding;
import com.soumya.telugupanchangam.utils.PermissionUtils;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private final String TAG_NAME = "HomeActivity";

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NightMode off
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        checkPermissionMethod();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkPermissionMethod() {
        if(PermissionUtils.checkPermissions(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // If you don't have access, launch a new activity to show the user the system's dialog
                    // to allow access to the external storage
                    Log.d(TAG_NAME,"ALL Permissions granted");
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }

        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                multiPermissionLancher.launch(new String[] { Manifest.permission.POST_NOTIFICATIONS});
            }else {
                multiPermissionLancher.launch(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
        }
    }

    private final ActivityResultLauncher<String[]> multiPermissionLancher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                boolean allGranted = true;
                for (String key : result.keySet()) {
                    allGranted = allGranted && Boolean.TRUE.equals(result.get(key));
                }
                if (allGranted) {
                    Log.d(TAG_NAME,"ALL Permissions granted");
                } else {
                    Log.d(TAG_NAME,"ALL Permissions not granted");
                }

            });

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionMethod();
    }
}