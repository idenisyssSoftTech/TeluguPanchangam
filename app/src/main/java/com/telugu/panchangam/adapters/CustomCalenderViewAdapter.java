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
    private int selectedPosition = RecyclerView.NO_POSITION;
    private final int lastSelectedPosition = RecyclerView.NO_POSITION;
//    private int selectedItemPosition = -1; // Initially, no item is selected

    public CustomCalenderViewAdapter(Context context , List<CalenderItem> items, OnDateChangedCallBack onDateChangedCallBack,
                                     int currentMonth, int currentYear, int day) {
        this.context = context;
        this.items = items;
        this.onDateChangedCallBack = onDateChangedCallBack;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
        this.day = day;
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
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
            } else {
                holder.dayTextView.setBackgroundResource(0); // Clear background for non-selected items
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.purple_700));
            }

            holder.date_view_layout.setOnClickListener(view -> {
                if (onDateChangedCallBack != null) {
                    Log.d(TAG_NAME, "Item clicked at position: " + position);
                    int previousSelected = selectedPosition;
                    selectedPosition = holder.getAdapterPosition();
                    Log.d(TAG_NAME, "previousSelected: " + previousSelected + ", selectedPosition: " + selectedPosition);

                    notifyItemChanged(previousSelected);
                    notifyItemChanged(selectedPosition);
                    int clickedDay = Integer.parseInt(item.getDay());
                    onDateChangedCallBack.onDateChanged(selectedPosition, previousSelected, clickedDay, currentMonth, currentYear);
                }
            });
        } else {
            // Handle the first row (headers), you can set different styling if needed
            holder.dayTextView.setBackgroundResource(0); // Clear background for headers
            holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.purple_700));
        }

//                // Update the background based on the selection state
//        if (position < 7) {
//            holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.purple_700));
//            holder.dayTextView.setBackgroundResource(0); // Clear background
//        } else {
//            if (position == selectedPosition) {
//                holder.dayTextView.setBackgroundResource(R.drawable.circle_background); // Apply the selected background
//                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
//            } else {
//                holder.dayTextView.setBackgroundResource(0); // Clear background for non-selected items
//                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
//            }
//        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick (View view){
//                    if (position >= 7 && onDateChangedCallBack != null ){
//                    Log.d(TAG_NAME, "Item clicked at position: " + position);
//                    int previousSelected = selectedPosition;
//                    selectedPosition = holder.getAdapterPosition();
//                        // Update the isSelected property of items
//                        for (int i = 0; i < items.size(); i++) {
//                            items.get(i).setSelected(i == selectedPosition);
//                        }
//                    notifyItemChanged(previousSelected);
//                    notifyItemChanged(selectedPosition);
//                        int clickedDay = Integer.parseInt(item.getDay());
//                    onDateChangedCallBack.onDateChanged(selectedPosition,previousSelected,clickedDay, currentMonth, currentYear);
//                }
//            }
//        });
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
