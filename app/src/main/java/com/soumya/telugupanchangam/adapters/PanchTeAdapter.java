package com.soumya.telugupanchangam.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;

import java.util.List;

public class PanchTeAdapter extends RecyclerView.Adapter<PanchTeAdapter.MyPanchTeItemView> {
    private final String TAG_NAME = PanchTeAdapter.class.getName();
    List<String> panchTeList;


    public PanchTeAdapter(List<String> panchTeList) {
        this.panchTeList = panchTeList;
    }


    @NonNull
    @Override
    public MyPanchTeItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.panch_items,parent,false);
        return new MyPanchTeItemView(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyPanchTeItemView holder, int position) {
        String item = panchTeList.get(position);
        // Parse the concatenated string here and set it to your TextViews as needed
        String[] parts = item.split(" - ");
        if (parts.length >= 7) {
            holder.dateVaaram.setText(parts[0] + " - "+ parts[1]);
            holder.yearName.setText(parts[2]);
            holder.ruthuvuName.setText(parts[3]+" - "+ parts[4]);
            holder.paksham.setText(parts[5]+" - "+parts[6]);
        }
    }

    @Override
    public int getItemCount() {
        return panchTeList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<String> newFestivalList) {
        panchTeList.clear();
        panchTeList.addAll(newFestivalList);
        Log.d(TAG_NAME,"panchTeAdapter in recylerview :"+panchTeList);
        notifyDataSetChanged();
    }


    public static class MyPanchTeItemView extends RecyclerView.ViewHolder {
        TextView panchTitle, dateVaaram, yearName, ruthuvuName, paksham;
        public MyPanchTeItemView(@NonNull View itemView) {
            super(itemView);
            panchTitle = itemView.findViewById(R.id.panch_title);
            dateVaaram = itemView.findViewById(R.id.Date_vaaram);
            yearName = itemView.findViewById(R.id.yearName);
            ruthuvuName = itemView.findViewById(R.id.ruthuName);
            paksham = itemView.findViewById(R.id.paksham);
        }
    }
}
