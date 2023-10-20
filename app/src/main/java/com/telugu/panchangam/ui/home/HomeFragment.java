package com.telugu.panchangam.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.telugu.panchangam.R;
import com.telugu.panchangam.activities.EventActivity;
import com.telugu.panchangam.adapters.CustomCalenderViewAdapter;
import com.telugu.panchangam.adapters.PanchTeAdapter;
import com.telugu.panchangam.customviews.panchangcalenderview.CalenderItem;
import com.telugu.panchangam.customviews.panchangcalenderview.OnDateChangedCallBack;
import com.telugu.panchangam.sqliteDB.database.SqliteDBHelper;
import com.telugu.panchangam.utils.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements OnDateChangedCallBack{

    private final String TAG_NAME = "HomeFragment";
    private SqliteDBHelper dbHelper;
    private ExtendedFloatingActionButton fab;
    private RecyclerView calenderRecyclerview, panchTeRecyclerView;
    private CustomCalenderViewAdapter calenderViewAdapter;
    private PanchTeAdapter panchTeAdapter;
    private int currentDay, currentMonth, currentYear;
    GridLayoutManager gridLayoutManager;
    ImageButton prevButton,nextButton;
    private TextView updateTextMonth;
    private NestedScrollView scrollView;
    private boolean isTextVisible = true;

    public Context context;
    private final int selectedDatePosition = -1;

    // Constants
    private static final int MIN_YEAR = 2022;
    private static final int MAX_YEAR = 2023;
    private static final int MAX_MONTH = 11;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_home, container, false);

        initViews(root);
        setListeners();
        updateCalendar(currentYear, currentMonth,currentDay,-1, -1);
        // Log the database path
        String dbPath = requireContext().getDatabasePath(SqliteDBHelper.DATABASE_NAME).getPath();
        Log.d(TAG_NAME, "Database Path: " + dbPath);

        return root;
    }

    private void initViews(View root) {
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        fab = root.findViewById(R.id.fab);

        calenderRecyclerview = root.findViewById(R.id.calender_view);
        gridLayoutManager = new GridLayoutManager(getActivity(),7);
        calenderRecyclerview.setLayoutManager(gridLayoutManager);

        panchTeRecyclerView = root.findViewById(R.id.panchTeRecyclerView);
        panchTeAdapter = new PanchTeAdapter(requireContext(),new ArrayList<>());
        panchTeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        panchTeRecyclerView.setAdapter(panchTeAdapter);

        prevButton = root.findViewById(R.id.previous_month);
        nextButton = root.findViewById(R.id.next_month);
        updateTextMonth = root.findViewById(R.id.monthName);

        scrollView = root.findViewById(R.id.home_scrollView);

        dbHelper = new SqliteDBHelper(requireContext());
        dbHelper.copyDatabaseFromAssets();
    }

    private void setListeners() {
    // Initialize oldScrollY with the initial scroll position
        final int[] oldScrollY = {scrollView.getScrollY()};
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = scrollView.getScrollY();
            if (scrollY > oldScrollY[0]) {
                // Scrolling down
                if (isTextVisible) {
                    fab.shrink();
                    isTextVisible = false;
                }
            } else if (scrollY < oldScrollY[0]) {
                // Scrolling up
                if (!isTextVisible) {
                    fab.extend();
                    isTextVisible = true;
                }
            }
            oldScrollY[0] = scrollY;
        });
        fab.setOnClickListener(view -> startActivity(new Intent(requireContext(), EventActivity.class)));
        prevButton.setOnClickListener(view -> onMonthChange(-1));
        nextButton.setOnClickListener(view -> onMonthChange(1));
    }


    @SuppressLint("NotifyDataSetChanged")
    private void updateCalendar(int year, int month, int day, int selectedPosition, int previousPosition) {
        List<CalenderItem> calendarItems = generateSampleData(year, month,day);
        CustomCalenderViewAdapter calenderViewAdapter = new CustomCalenderViewAdapter(getActivity(), calendarItems,
                this,month,year,day, selectedPosition, previousPosition);
        gridLayoutManager = new GridLayoutManager(getActivity(),7);
        calenderRecyclerview.setLayoutManager(gridLayoutManager);
        calenderRecyclerview.setAdapter(calenderViewAdapter);

        String dateVaaram = utils.updateMonth(currentMonth,currentYear,currentDay);
        updateTextMonth.setText(utils.updateFestivalMonth(currentMonth,currentYear));
        Log.d(TAG_NAME,"full date: "+dateVaaram);
        String[] parts = dateVaaram.split(",");
        String Vaaram = parts[0];
        String date = (parts.length > 1) ? parts[1] : "";
        Log.d(TAG_NAME, "date :"+date);
        List<String> panchTeData = dbHelper.getPanchTeDataForDate(Vaaram, date);
        Log.d(TAG_NAME, "panchTeData : " + panchTeData);
        panchTeAdapter.setData(panchTeData);
    }

    private List<CalenderItem> generateSampleData(int year, int month,int iday) {
        List<CalenderItem> items = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] weekNames = getResources().getStringArray(R.array.week_names);
        for (String weekName : weekNames) {
            items.add(new CalenderItem(weekName, ""));
        }
        // Add empty items for the days before the start day
        for (int i = 1; i < startDayOfWeek; i++) {
            items.add(new CalenderItem("", ""));
        }
        for (int i = 1; i <= daysInMonth; i++) {
            String day = String.valueOf(i);
            String event = "Event for day " + day;
            items.add(new CalenderItem(day, event));
        }
        return items;
    }

    private void onMonthChange(int change) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);
        calendar.add(Calendar.MONTH, change);

        int newYear = calendar.get(Calendar.YEAR);
        int newMonth = calendar.get(Calendar.MONTH);
        if (isValidMonth(newYear, newMonth)) {
            // Check if it's the current month
            if (newYear == Calendar.getInstance().get(Calendar.YEAR) && newMonth == Calendar.getInstance().get(Calendar.MONTH)) {
                currentYear = newYear;
                currentMonth = newMonth;
                currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            } else {
                // Set the day to 1 to navigate to the first day of the month
                currentYear = newYear;
                currentMonth = newMonth;
                currentDay = 1;
            }
            updateCalendar(currentYear, currentMonth, currentDay, -1, -1);
        } else {
            Toast.makeText(requireContext(), getString(R.string.nodatafound), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidMonth(int year, int month) {
        return (year == MIN_YEAR || year == MAX_YEAR && month <= MAX_MONTH);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDateChanged(int selectedPosition, int previousPosition, int day, int month, int year) {
        Log.d(TAG_NAME, "Date changed: " + day + "/" + (month + 1) + "/" + year);
        currentDay = day;
        currentMonth = month;
        currentYear = year;
        updateCalendar(currentYear, currentMonth,currentDay, selectedPosition, previousPosition);
    }
}