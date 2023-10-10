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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.telugu.panchangam.R;
import com.telugu.panchangam.activities.EventActivity;
import com.telugu.panchangam.adapters.PanchTeAdapter;
import com.telugu.panchangam.customviews.panchangcalenderview.CalenderItem;
import com.telugu.panchangam.adapters.CustumCalenderViewAdapter;
import com.telugu.panchangam.customviews.panchangcalenderview.OnDateChangedCallBack;
import com.telugu.panchangam.sqliteDB.database.SqliteDBHelper;
import com.telugu.panchangam.utils.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements OnDateChangedCallBack{

    private final String TAG_NAME = HomeFragment.class.getName();
    private SqliteDBHelper dbHelper;
    private ExtendedFloatingActionButton fab;
    private RecyclerView calenderRecyclerview, panchTeRecyclerView;
    private CustumCalenderViewAdapter calenderViewAdapter;
    private PanchTeAdapter panchTeAdapter;
    private int currentDay, currentMonth, currentYear;
    GridLayoutManager gridLayoutManager;
    ImageButton prevButton,nextButton;
    private TextView updateTextMonth;
    private ScrollView scrollView;
    private boolean isTextVisible = true;

    private Context context;
    private int selectedDatePosition = -1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_home, container, false);

        initViews(root);
        setListeners();
        updateCalendar(currentYear, currentMonth,currentDay);
        // Log the database path
        String dbPath = requireContext().getDatabasePath(SqliteDBHelper.DATABASE_NAME).getPath();
        Log.d(TAG_NAME, "Database Path: " + dbPath);

        return root;
    }

    private void setListeners() {
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                // Scrolling down
                if (isTextVisible) {
                    fab.shrink();
                    isTextVisible = false;
                }
            } else if (scrollY < oldScrollY) {
                // Scrolling up
                if (!isTextVisible) {
                    fab.extend();
                    isTextVisible = true;
                }
            }
        });
        fab.setOnClickListener(view -> startActivity(new Intent(requireContext(), EventActivity.class)));
        prevButton.setOnClickListener(view -> onMonthChange(-1));
        nextButton.setOnClickListener(view -> onMonthChange(1));
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
    private void updateCalendar(int year, int month,int day) {
        List<CalenderItem> calendarItems = generateSampleData(year, month,day);
        calenderViewAdapter = new CustumCalenderViewAdapter(getActivity(), calendarItems,this,month,year,day);
        calenderRecyclerview.setAdapter(calenderViewAdapter);
        String dateVaaram = utils.updateMonth(currentMonth,currentYear,currentDay);
        Log.d(TAG_NAME,"full date: "+dateVaaram);
        String[] parts = dateVaaram.split(",");
        String Vaaram = parts[0];
        String date = "";
        if (parts.length > 1) {
            date = parts[1];
        }
        updateTextMonth.setText(utils.updateMonth(currentMonth,currentYear,currentDay));
        List<String> panchTeData = dbHelper.getPanchTeDataForDate(Vaaram, date);
        Log.d(TAG_NAME, "panchTeData : " + panchTeData);
        panchTeAdapter.setData(panchTeData);
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
            updateCalendar(currentYear, currentMonth, currentDay);
        } else {
            Toast.makeText(requireContext(), getString(R.string.nodatafound), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDateChanged(int day, int month, int year) {
        Log.d(TAG_NAME, "Date changed: " + day + "/" + (month + 1) + "/" + year);
        currentDay = day;
        currentMonth = month;
        currentYear = year;
        updateCalendar(currentYear, currentMonth,currentDay);
        String selectedValue = calculateSelectedValue(day, month, year);
        int position = PanchTeAdapter.panchTeList.indexOf(selectedValue);
        Log.d(TAG_NAME, "Selected Position: " + position);
        panchTeAdapter.highlightDate(position);
        selectedDatePosition = position;
    }

    @SuppressLint("DefaultLocale")
    private String calculateSelectedValue(int day, int month, int year) {
         String formattedDay = String.format("%02d", day);
        String formattedMonth = String.format("%02d", month + 1);
        String selectedValue = formattedDay + "/" + formattedMonth + "/" + year; // Adjust the format as needed
        Log.d(TAG_NAME, "Selected Value: " + selectedValue);
        return selectedValue;
    }
}