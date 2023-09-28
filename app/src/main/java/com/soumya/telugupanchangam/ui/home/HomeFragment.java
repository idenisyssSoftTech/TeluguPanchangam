package com.soumya.telugupanchangam.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.activities.EventActivity;
import com.soumya.telugupanchangam.customviews.panchangcalenderview.CalenderItem;
import com.soumya.telugupanchangam.customviews.panchangcalenderview.CustumCalenderViewAdapter;
import com.soumya.telugupanchangam.customviews.panchangcalenderview.OnDateChangedCallBack;
import com.soumya.telugupanchangam.utils.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements OnDateChangedCallBack{


    private ExtendedFloatingActionButton fab;
    private RecyclerView calenderRecyclerview;
    private CustumCalenderViewAdapter calenderViewAdapter;
    private int currentDay;
    private int currentMonth;
    private int currentYear;
    GridLayoutManager gridLayoutManager;
    ImageButton prevButton,nextButton;
    private TextView updateTextMonth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        View root =inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the current month and year
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);


        fab = root.findViewById(R.id.fab);
        calenderRecyclerview = root.findViewById(R.id.calender_view);
        prevButton = root.findViewById(R.id.previous_month);
        nextButton = root.findViewById(R.id.next_month);

        updateTextMonth = root.findViewById(R.id.monthName);
        updateTextMonth.setText(utils.updateMonth(currentMonth,currentYear, currentDay));

        gridLayoutManager = new GridLayoutManager(getActivity(),7);
        List<CalenderItem> calendarItems = generateSampleData(currentYear,currentMonth);
        calenderViewAdapter = new CustumCalenderViewAdapter(getActivity(),calendarItems,this,currentMonth,currentYear);
        calenderRecyclerview.setLayoutManager(gridLayoutManager);
        calenderRecyclerview.setAdapter(calenderViewAdapter);


        fab.setOnClickListener(view -> startActivity(new Intent(requireContext(), EventActivity.class)));
        prevButton.setOnClickListener(view -> onPreviousMonthClicked());
        nextButton.setOnClickListener(view -> onNextMonthClicked());

        return root;
    }

    private List<CalenderItem> generateSampleData(int year, int month) {
        // Generate your calendar data here, e.g., for a single month
        List<CalenderItem> items = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= daysInMonth; i++) {
            String day = String.valueOf(i);
            String event = "Event for day " + day; // You can customize this

            items.add(new CalenderItem(day, event));
        }
        return items;

    }
    private void updateCalendar(int year, int month) {
        List<CalenderItem> calendarItems = generateSampleData(year, month);
        calenderViewAdapter = new CustumCalenderViewAdapter(getActivity(), calendarItems,this,month,year);
        calenderRecyclerview.setAdapter(calenderViewAdapter);
        updateTextMonth.setText(utils.updateMonth(currentMonth,currentYear,currentDay));
    }
    private void onPreviousMonthClicked() {
        // Handle previous month navigation
        if (currentMonth == 0) {
            currentYear--;
            currentMonth = 11;
        } else {
            currentMonth--;
        }

        updateCalendar(currentYear, currentMonth);
    }

    private void onNextMonthClicked() {
        if (currentMonth == 11) {
            currentYear++;
            currentMonth = 0;
        } else {
            currentMonth++;
        }

        updateCalendar(currentYear, currentMonth);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDateChanged(int day, int month, int year) {
        // Handle item click here, for example, update the displayed day, month, and year
        currentDay = day;
        currentMonth = month;
        currentYear = year;

        // Update the calendar and month text
        updateCalendar(currentYear, currentMonth);
        updateTextMonth.setText(utils.updateMonth(currentMonth, currentYear, currentDay));
    }
}