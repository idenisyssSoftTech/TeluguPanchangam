package com.soumya.telugupanchangam.customviews.panchangcalenderview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CustumCalenderViewAdapter extends RecyclerView.Adapter<CustumCalenderViewAdapter.MYCalenderItemView> {

    private final List<CalenderItem> items;
    private final Context context;
    private final int currentMonth;
    private final int currentYear,day;
    OnDateChangedCallBack onDateChangedCallBack;

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

    @Override
    public void onBindViewHolder(@NonNull CustumCalenderViewAdapter.MYCalenderItemView holder, int position) {
        CalenderItem item = items.get(position);

        // Set the formatted month and year as the TextView's text
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,  item.getYear());
        calendar.set(Calendar.MONTH,  item.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, item.getIday());

        SimpleDateFormat monthFormat = new SimpleDateFormat("EEE");
        monthFormat.format(calendar.getTime());


        holder.dayTextView.setText(item.getDaynumber());
        holder.eventTextView.setText(item.getEvent());


        // Handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDateChangedCallBack != null) {
                    int day = Integer.parseInt(item.getDaynumber());
                    onDateChangedCallBack.onDateChanged(day, currentMonth, currentYear,item.getIday());
                }
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
