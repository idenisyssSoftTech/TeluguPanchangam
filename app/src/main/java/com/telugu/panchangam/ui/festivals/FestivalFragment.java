package com.telugu.panchangam.ui.festivals;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.telugu.panchangam.BuildConfig;
import com.telugu.panchangam.R;
import com.telugu.panchangam.adapters.FestivalAdapter;
import com.telugu.panchangam.sqliteDB.database.SqliteDBHelper;
import com.telugu.panchangam.utils.AppConstants;
import com.telugu.panchangam.utils.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FestivalFragment extends Fragment {
    private final String TAG_NAME = FestivalFragment.class.getName();
    private SqliteDBHelper dbHelper;
    private int currentDay, currentMonth, currentYear;
    private ImageButton prevButton,nextButton, share_festival_screenshot;
    private TextView updateTextMonth;
    private RecyclerView recyclerView;
    private FestivalAdapter festivalAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_festivals, container, false);
        dbHelper = new SqliteDBHelper(requireContext());
        dbHelper.copyDatabaseFromAssets();

        intiViews(root);
        setListeners();
        // Log the database path
        String dbPath = requireContext().getDatabasePath(AppConstants.DATABASE_NAME).getPath();
        Log.d(TAG_NAME, "Database Path: " + dbPath);

        return root;
    }

    private void intiViews(View root) {
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        recyclerView = root.findViewById(R.id.festivalRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        festivalAdapter = new FestivalAdapter(new ArrayList<>());
        recyclerView.setAdapter(festivalAdapter);
        prevButton = root.findViewById(R.id.previous_month);
        nextButton = root.findViewById(R.id.next_month);
        share_festival_screenshot = root.findViewById(R.id.share_screenshot);
        updateTextMonth = root.findViewById(R.id.monthName);
        updateFestivalData();

    }
    private void setListeners() {
        prevButton.setOnClickListener(view -> onMonthChange(-1));
        nextButton.setOnClickListener(view -> onMonthChange(1));
        share_festival_screenshot.setOnClickListener(view -> captureAndShareScreenshot());
    }
    private void captureAndShareScreenshot() {
        ConstraintLayout rootView = requireView().findViewById(R.id.festival_header_and_recycler_layout);
        Bitmap screenshot = takeScreenshot(rootView);
        File tempFile = utils.saveBitmapToFile(requireContext(), screenshot);
        if (tempFile != null) {
            Uri screenshotUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, Objects.requireNonNull(tempFile));
            // Share the screenshot using an intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout TeluguPanchangam App: \n ANDROID:" + AppConstants.appUrl);
            startActivity(Intent.createChooser(shareIntent, "Share With"));
        }else {
            // Handle the case where tempFile is null
            Log.e("Capture Error", "Failed to save screenshot as a temp file.");
        }
    }
    private Bitmap takeScreenshot(ConstraintLayout view) {
        view.setDrawingCacheEnabled(true);
        Bitmap screenshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return screenshot;
    }
    private Bitmap takeScreenshot(RecyclerView recyclerView) {

        Bitmap screenshot;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (adapter != null) {
            screenshot = Bitmap.createBitmap(recyclerView.getWidth(), recyclerView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(screenshot);
            recyclerView.draw(canvas);
        } else {
            // Handle the case where the adapter is null
            screenshot = null;
        }
        return screenshot;
    }

    private void onMonthChange(int change) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);
        calendar.add(Calendar.MONTH, change);

        int newYear = calendar.get(Calendar.YEAR);
        int newMonth = calendar.get(Calendar.MONTH);
        if (isValidMonth(newYear, newMonth)) {
            currentYear = newYear;
            currentMonth = newMonth;
            // Check if it's the current month
            if (newYear == Calendar.getInstance().get(Calendar.YEAR) && newMonth == Calendar.getInstance().get(Calendar.MONTH)) {
                currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            } else {
                currentDay = 1;
            }
            updateFestivalData();
        } else {
            Toast.makeText(requireContext(), getString(R.string.nodatafound), Toast.LENGTH_SHORT).show();
        }
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        if (isValidMonth(currentYear, currentMonth - 1)) {
            prevButton.setVisibility(View.VISIBLE);
        } else {
            prevButton.setVisibility(View.GONE);
        }
        if (isValidMonth(currentYear, currentMonth + 1)) {
            nextButton.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.GONE);
        }
    }
    private boolean isValidMonth(int year, int month) {
        return (year > AppConstants.MIN_YEAR || (year == AppConstants.MIN_YEAR && month >= 0)) &&
                (year < AppConstants.MAX_YEAR || (year == AppConstants.MAX_YEAR && month <= AppConstants.MAX_MONTH));
    }
    // Method to update festival data based on the current month and year
    @SuppressLint("NotifyDataSetChanged")
    private void updateFestivalData() {
        List<String> festivalDataList = dbHelper.getFestivalDataForMonth(utils.getMonthName(currentMonth), currentYear);
        Log.d(TAG_NAME, "Festival_Names : " + festivalDataList);
        festivalAdapter.setData(festivalDataList);
        updateTextMonth.setText(utils.updateFestivalMonth(currentMonth,currentYear));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}