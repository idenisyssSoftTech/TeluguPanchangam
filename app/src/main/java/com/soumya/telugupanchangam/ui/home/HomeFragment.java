package com.soumya.telugupanchangam.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.activities.EventActivity;
import com.soumya.telugupanchangam.customviews.panchangcalenderview.CalenderItem;
import com.soumya.telugupanchangam.customviews.panchangcalenderview.CustumCalenderViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {


    private ExtendedFloatingActionButton fab;
    private RecyclerView calenderRecyclerview;
    private CustumCalenderViewAdapter calenderViewAdapter;
    private int currentMonth;
    private int currentYear;
    GridLayoutManager gridLayoutManager;
    ImageButton prevButton,nextButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        View root =inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the current month and year
        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);


        fab = root.findViewById(R.id.fab);
        calenderRecyclerview = root.findViewById(R.id.calender_view);
        prevButton = root.findViewById(R.id.previous_month);
        nextButton = root.findViewById(R.id.next_month);
        gridLayoutManager = new GridLayoutManager(getActivity(),7);
        List<CalenderItem> calendarItems = generateSampleData(currentYear,currentMonth);
        calenderViewAdapter = new CustumCalenderViewAdapter(getActivity(),calendarItems);
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
        calenderViewAdapter = new CustumCalenderViewAdapter(getActivity(), calendarItems);
        calenderRecyclerview.setAdapter(calenderViewAdapter);
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
        // Handle next month navigation
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}