package com.telugu.panchangam.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.telugu.panchangam.R;
import com.telugu.panchangam.databinding.ActivityHomeBinding;
import com.telugu.panchangam.sqliteDB.database.SqliteDBHelper;
import com.telugu.panchangam.utils.PermissionUtils;

import java.util.Objects;
public class HomeActivity extends AppCompatActivity {

    private final String TAG_NAME = HomeActivity.class.getName();
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NightMode off
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        try {
            SqliteDBHelper dbHelper = new SqliteDBHelper(this);
            dbHelper.copyDatabaseFromAssets();
        }catch (Exception e){
            e.printStackTrace();
        }
        Objects.requireNonNull(getSupportActionBar()).hide();

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

            AppBarConfiguration appBarConfiguration1 = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration1);
            NavigationUI.setupWithNavController(binding.navView, navController);

        checkPermissionMethod();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkPermissionMethod() {
        if(PermissionUtils.checkPermissions(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Log.d(TAG_NAME,"ALL Permissions granted");
                } else {
                    openAllFilesAccessSettings();
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
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void openAllFilesAccessSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
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