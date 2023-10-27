package com.telugu.panchangam.ui.home;

import android.content.Context;
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
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.telugu.panchangam.BuildConfig;
import com.telugu.panchangam.R;
import com.telugu.panchangam.activities.EventActivity;
import com.telugu.panchangam.adapters.CustomCalenderViewAdapter;
import com.telugu.panchangam.adapters.PanchTeAdapter;
import com.telugu.panchangam.customviews.panchangcalenderview.CalenderItem;
import com.telugu.panchangam.customviews.panchangcalenderview.OnDateChangedCallBack;
import com.telugu.panchangam.sqliteDB.database.SqliteDBHelper;
import com.telugu.panchangam.utils.AppConstants;
import com.telugu.panchangam.utils.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements OnDateChangedCallBack{

    private final String TAG_NAME = "HomeFragment";
    private SqliteDBHelper dbHelper;
    private ExtendedFloatingActionButton fab;
    private RecyclerView calenderRecyclerview, panchTeRecyclerView;
    private PanchTeAdapter panchTeAdapter;
    private int currentDay, currentMonth, currentYear;
    GridLayoutManager gridLayoutManager;
    ImageButton prevButton,nextButton, share_screenshotData;
    private TextView updateTextMonth;
    private NestedScrollView scrollView;
    private boolean isTextVisible = true;

    public Context context;



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
        share_screenshotData = root.findViewById(R.id.share_screenshot);
        share_screenshotData.setOnClickListener(view -> captureAndShareScreenshot());
        updateTextMonth = root.findViewById(R.id.monthName);

        scrollView = root.findViewById(R.id.home_scrollView);

        dbHelper = new SqliteDBHelper(requireContext());
        dbHelper.copyDatabaseFromAssets();
    }

    private void setListeners() {
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

    private void captureAndShareScreenshot() {
//        View rootView = requireActivity().getWindow().getDecorView().findViewById(R.id.panchTeRecyclerView);
        Bitmap screenshot = takeScreenshot(panchTeRecyclerView);
            File tempFile = utils.saveBitmapToFile(requireContext(), screenshot);
            if (tempFile != null) {
                Uri screenshotUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, Objects.requireNonNull(tempFile));
                String appUrl = "https://play.google.com/store/apps/details?id=com.abhiram.qrbarscanner";
                // Share the screenshot using an intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout TeluguPanchangam App: \n ANDROID:" + appUrl);
                startActivity(Intent.createChooser(shareIntent, "Share Screenshot"));
            }else {
                // Handle the case where tempFile is null
                Log.e("Capture Error", "Failed to save screenshot as a temp file.");
            }
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
//        view.setDrawingCacheEnabled(true);
//        Bitmap screenshot = Bitmap.createBitmap(view.getDrawingCache());
//        view.setDrawingCacheEnabled(false);
        return screenshot;
    }
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

    /** click button to change months **/
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
            updateCalendar(currentYear, currentMonth, currentDay, -1, -1);
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