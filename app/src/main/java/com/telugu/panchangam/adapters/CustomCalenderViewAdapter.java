package com.telugu.panchangam.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.telugu.panchangam.R;
import com.telugu.panchangam.customviews.panchangcalenderview.CalenderItem;
import com.telugu.panchangam.customviews.panchangcalenderview.OnDateChangedCallBack;

import java.util.Calendar;
import java.util.List;

public class CustomCalenderViewAdapter extends RecyclerView.Adapter<CustomCalenderViewAdapter.MYCalenderItemView> {

    private final String TAG_NAME = "CustomCalenderViewAdapter";
    private final List<CalenderItem> items;
    private final Context context;
    private final int currentMonth;
    private final int currentYear,currentDay;
    OnDateChangedCallBack onDateChangedCallBack;
    private int selectedPosition ;
    private final int previousPosition ;

    public CustomCalenderViewAdapter(Context context , List<CalenderItem> items, OnDateChangedCallBack onDateChangedCallBack,
                                     int currentMonth, int currentYear, int day, int selectedPosition, int previousPosition) {
        this.context = context;
        this.items = items;
        this.onDateChangedCallBack = onDateChangedCallBack;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
        this.currentDay = day;
        this.selectedPosition = selectedPosition;
        this.previousPosition = previousPosition;
    }


    @NonNull
    @Override
    public CustomCalenderViewAdapter.MYCalenderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.calender_date_list,parent,false);
        return new MYCalenderItemView(v);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull CustomCalenderViewAdapter.MYCalenderItemView holder, @SuppressLint("RecyclerView") int position) {
        CalenderItem item = items.get(position);
        holder.dayTextView.setText(String.valueOf(item.getDay()));

        if (position >= 7) { // Exclude the first row (headers) from selection
            String dayString = item.getDay();
            int dayOfMonth; // Default value for dayOfMonth

            if (!dayString.isEmpty() && dayString.matches("\\d+")) {
                dayOfMonth = Integer.parseInt(dayString);
            } else {
                dayOfMonth = -1;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, currentYear);
            calendar.set(Calendar.MONTH, currentMonth);
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, currentDay);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (dayOfMonth == currentDay) {
                holder.dayTextView.setBackgroundResource(R.drawable.current_day_background);
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.dayTextView.setBackgroundResource(0);
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
            }

            if (position == selectedPosition) {
                holder.dayTextView.setBackgroundResource(R.drawable.circle_background);
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
            if (dayOfMonth != -1) {
                holder.date_view_layout.setOnClickListener(view -> {
                    if (onDateChangedCallBack != null) {
                        Log.d(TAG_NAME, "Item clicked at position: " + position);
                        int previousSelected = selectedPosition;
                        selectedPosition = position;
                        Log.d(TAG_NAME, "previousSelected: " + previousSelected + ", selectedPosition: " + selectedPosition);

                        notifyItemChanged(previousSelected);
                        notifyItemChanged(selectedPosition);

                        int clickedDay = dayOfMonth;
                        onDateChangedCallBack.onDateChanged(selectedPosition, previousSelected, clickedDay, currentMonth, currentYear);
                    }
                });
            }
        } else {
            // Handle the first row (headers), you can set different styling if needed
            holder.date_view_layout.setBackgroundResource(0);
            holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.purple_700));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class MYCalenderItemView extends RecyclerView.ViewHolder{
        TextView dayTextView;
        TextView eventTextView;
        LinearLayout date_view_layout;
        public MYCalenderItemView(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            eventTextView = itemView.findViewById(R.id.eventTextView);
            date_view_layout = itemView.findViewById(R.id.date_view_layout);
        }
    }
}
