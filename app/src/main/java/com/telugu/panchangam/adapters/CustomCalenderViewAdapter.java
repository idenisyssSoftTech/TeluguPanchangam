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

import java.util.List;

public class CustomCalenderViewAdapter extends RecyclerView.Adapter<CustomCalenderViewAdapter.MYCalenderItemView> {

    private final String TAG_NAME = "CustumCalenderViewAdapter";
    private final List<CalenderItem> items;
    private final Context context;
    private final int currentMonth;
    private final int currentYear,day;
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
        this.day = day;
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
            if (position == selectedPosition) {
                holder.dayTextView.setBackgroundResource(R.drawable.circle_background); // Apply the selected background
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.dayTextView.setBackgroundResource(0); // Clear background for non-selected items
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.purple_700));
            }
            String dayString = item.getDay();
            if (dayString != null && !dayString.isEmpty()) {
                holder.date_view_layout.setOnClickListener(view -> {
                    if (onDateChangedCallBack != null) {
                        Log.d(TAG_NAME, "Item clicked at position: " + position);
                        int previousSelected = selectedPosition;
                        selectedPosition = position;
                        Log.d(TAG_NAME, "previousSelected: " + previousSelected + ", selectedPosition: " + selectedPosition);

                        notifyItemChanged(previousSelected);
                        notifyItemChanged(selectedPosition);

                        int clickedDay = Integer.parseInt(dayString);
                        onDateChangedCallBack.onDateChanged(selectedPosition, previousSelected, clickedDay, currentMonth, currentYear);

                    }
                });
            }
        } else {
            // Handle the first row (headers), you can set different styling if needed
            holder.dayTextView.setBackgroundResource(0); // Clear background for headers
            holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.purple_700));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class MYCalenderItemView extends RecyclerView.ViewHolder{
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
