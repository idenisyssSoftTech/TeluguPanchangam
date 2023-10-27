package com.telugu.panchangam;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
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

import com.telugu.panchangam.databinding.ActivityHomeBinding;
import com.telugu.panchangam.sqliteDB.database.SqliteDBHelper;
import com.telugu.panchangam.utils.PanchangConstants;
import com.telugu.panchangam.utils.PanchangSharedPref;
import com.telugu.panchangam.utils.PermissionUtils;

import java.util.Locale;
import java.util.Objects;
public class HomeActivity extends AppCompatActivity {

    private final String TAG_NAME = "HomeActivity";
    private Context      context;
    PanchangSharedPref sharedPreferences;
    boolean initialNavigationPerformed = false;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NightMode off
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = PanchangSharedPref.getInstance(this);

        // Retrieve the checkbox state from shared preferences and set it
        String languangecode = sharedPreferences.getStringVal(PanchangConstants.SELECTEDLAUNGE_CODE, PanchangConstants.LANG_TELUGU);
        if (languangecode != null && !languangecode.isEmpty()) {
            changeAppLanguage(getApplicationContext(), languangecode);
        } else {
            changeAppLanguage(getApplicationContext(), PanchangConstants.LANG_TELUGU);
        }

        try {
            SqliteDBHelper dbHelper = new SqliteDBHelper(this);
            dbHelper.copyDatabaseFromAssets();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkPermissionMethod();

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



        Log.d(TAG_NAME,"initialNavigationPref :"+initialNavigationPerformed);
        if (!initialNavigationPerformed) {
            Intent intent = getIntent();
            String fragmentToShow = intent.getStringExtra("FRAGMENT_TO_SHOW");
            initialNavigationPerformed = true;
            Log.d(TAG_NAME, "FRAGMENT_TO_SHOW: " + fragmentToShow);
            if (fragmentToShow != null && fragmentToShow.equals("notifications")) {
                navController.navigate(R.id.navigation_notifications);
                Log.d(TAG_NAME, "Notification Fragment :" );
            } else {
                navController.navigate(R.id.navigation_home);
                Log.d(TAG_NAME, "INSIDE HOME FRAGMENT" );
            }

        }else{
            navController.navigate(R.id.navigation_home);
            Log.d(TAG_NAME, "OUTSIDE HOME FRAGMENT");
        }

        binding.navView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                navController.navigate(R.id.navigation_home);
            } else if (itemId == R.id.navigation_dashboard) {
                navController.navigate(R.id.navigation_dashboard);
            } else if (itemId == R.id.navigation_notifications) {
                navController.navigate(R.id.navigation_notifications);
            }
            else if (itemId == R.id.navigation_settings) {
                navController.navigate(R.id.navigation_settings);
            }
            return true;
        });
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
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkPermissionMethod() {
        if (PermissionUtils.checkPermissions(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Log.d(TAG_NAME, "ALL Permissions granted");
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                multiPermissionLancher.launch(new String[]{Manifest.permission.POST_NOTIFICATIONS});
            } else {
                multiPermissionLancher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
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
                    Log.d(TAG_NAME, "ALL Permissions granted");
                } else {
                    Log.d(TAG_NAME, "ALL Permissions not granted");
                }

            });

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionMethod();
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        // Check if you're not in the home fragment
        if (navController.getCurrentDestination() != null && navController.getCurrentDestination().getId() != R.id.navigation_home) {
            navController.navigate(R.id.navigation_home);
        } else {
            // If you are already in the home fragment, proceed with the default behavior
            super.onBackPressed();
            finish();
        }
    }
}