package com.telugu.panchangam.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

public class CustumCalenderViewAdapter extends RecyclerView.Adapter<CustumCalenderViewAdapter.MYCalenderItemView> {

    private final List<CalenderItem> items;
    private final Context context;
    private final int currentMonth;
    private final int currentYear,day;
    OnDateChangedCallBack onDateChangedCallBack;
    private int selectedItemPosition = -1; // Initially, no item is selected

    public CustumCalenderViewAdapter(Context context , List<CalenderItem> items,OnDateChangedCallBack onDateChangedCallBack, int currentMonth, int currentYear,int day) {
        this.context = context;
        this.items = items;
        this.onDateChangedCallBack = onDateChangedCallBack;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
        this.day = day;
    }


    @NonNull
    @Override
    public CustumCalenderViewAdapter.MYCalenderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.calender_date_list,parent,false);
        return new MYCalenderItemView(v);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull CustumCalenderViewAdapter.MYCalenderItemView holder, @SuppressLint("RecyclerView") int position) {
        CalenderItem item = items.get(position);
        holder.dayTextView.setText(item.getDay());
        holder.eventTextView.setText(item.getEvent());

        if (position < 7) {
            if (position == selectedItemPosition) {
                holder.date_view_layout.setBackgroundColor(Color.WHITE); // Change to your desired color
       			 } else {
          			holder.date_view_layout.setBackgroundColor(Color.TRANSPARENT); // Default color
        		 }
            holder.dayTextView.setTextColor(ContextCompat.getColor(context,R.color.purple_700));
        }

        holder.itemView.setOnClickListener(v -> {
            if (position >= 7 && onDateChangedCallBack != null ) {
          int previousSelectedItemPosition = selectedItemPosition;
                selectedItemPosition = position;
                notifyItemChanged(previousSelectedItemPosition); // Deselect the previous item
                notifyItemChanged(selectedItemPosition); // Select the clicked item

                int clickedDay = Integer.parseInt(item.getDay());
                onDateChangedCallBack.onDateChanged(clickedDay, currentMonth, currentYear);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MYCalenderItemView extends RecyclerView.ViewHolder {
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
