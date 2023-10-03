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

public class FestivalAdapter  extends RecyclerView.Adapter<FestivalAdapter.ViewHolder> {
    private final String TAG_NAME = "FestivalAdapter";
    private final List<String> festivalList;

    public FestivalAdapter(List<String> festivalList) {
        this.festivalList = festivalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_festival, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String festival = festivalList.get(position);
        // Split the festival data into FestivalDate, Vaaram, and FestivalName
        String[] festivalData = festival.split(" - ");
        // Check if there are at least two parts in the split
        if (festivalData.length >= 2) {
            String festivalDateVaaram = festivalData[0]+", "+festivalData[1]; // FestivalDate and Vaaram
            String festivalName = festivalData[2]; // FestivalName

            holder.festivalDatesVaaram.setText(festivalDateVaaram);
            holder.festivalNames.setText(festivalName);
        } else {
            // Handle the case where the data format is incorrect
            holder.festivalDatesVaaram.setText("");
            holder.festivalNames.setText(festival);
        }
    }

    @Override
    public int getItemCount() {
        return festivalList.size();
    }

    // Method to update the dataset
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<String> newFestivalList) {
        festivalList.clear();
        festivalList.addAll(newFestivalList);
        Log.d(TAG_NAME,"festivalList in recylerview :"+festivalList);
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView festivalDatesVaaram,festivalNames;
        public ViewHolder(View view) {
            super(view);
            festivalDatesVaaram = view.findViewById(R.id.festivalDates);
            festivalNames = view.findViewById(R.id.festivalNames);
        }
    }
}
