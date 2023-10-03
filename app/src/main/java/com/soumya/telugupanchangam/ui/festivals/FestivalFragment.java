package com.soumya.telugupanchangam.ui.festivals;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.adapters.FestivalAdapter;
import com.soumya.telugupanchangam.sqliteDB.database.FestivalDBHelper;
import com.soumya.telugupanchangam.utils.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FestivalFragment extends Fragment {
    private final String TAG_NAME = "FestivalFragment";
    private FestivalDBHelper dbHelper;
    private int currentDay, currentMonth, currentYear;
    private ImageButton prevButton,nextButton;
    private TextView updateTextMonth;
    private RecyclerView recyclerView;
    private FestivalAdapter festivalAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_festivals, container, false);
        dbHelper = new FestivalDBHelper(requireContext());
        dbHelper.copyDatabaseFromAssets();

        intiViews(root);
        setListeners();
        // Log the database path
        String dbPath = requireContext().getDatabasePath(FestivalDBHelper.DATABASE_NAME).getPath();
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
        updateTextMonth = root.findViewById(R.id.monthName);
        updateFestivalData();

    }
    private void setListeners() {
        prevButton.setOnClickListener(view -> onMonthChange(-1));
        nextButton.setOnClickListener(view -> onMonthChange(1));
    }
    private void onMonthChange(int change) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);
        calendar.add(Calendar.MONTH, change);

        int newYear = calendar.get(Calendar.YEAR);
        int newMonth = calendar.get(Calendar.MONTH);

        // Check if the new month is within the allowed range (2023 and 2024)
        if (newYear == 2022 || newYear == 2023 && newMonth <= 11) {
            currentYear = newYear;
            currentMonth = newMonth;
            updateFestivalData();
        } else {
            Toast.makeText(requireContext(), "సమాచారం అందుబాటులో లేదు", Toast.LENGTH_SHORT).show();
        }
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