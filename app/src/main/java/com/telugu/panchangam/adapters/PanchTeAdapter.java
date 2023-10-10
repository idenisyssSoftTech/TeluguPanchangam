package com.telugu.panchangam.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.telugu.panchangam.R;
import com.telugu.panchangam.utils.utils;

import java.util.List;

public class PanchTeAdapter extends RecyclerView.Adapter<PanchTeAdapter.MyPanchTeItemView> {
    private final String TAG_NAME = PanchTeAdapter.class.getName();
    public static List<String> panchTeList;

    private final Context context;
    private int selectedDatePosition = -1;

    public PanchTeAdapter(Context context,List<String> panchTeList) {
        this.context = context;
        PanchTeAdapter.panchTeList = panchTeList;
    }


    @NonNull
    @Override
    public MyPanchTeItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.panchangam_data,parent,false);
        return new MyPanchTeItemView(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyPanchTeItemView holder, int position) {
        String item = panchTeList.get(position);
        Log.d(TAG_NAME, "panchTeList item: " + item);

        if (position == selectedDatePosition) {
            holder.itemView.setBackgroundColor(Color.RED);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        String[] parts = item.split(" - ");
        if (parts.length >= 10) {
            holder.dateVaaram.setText(parts[0] + " - "+ parts[1]);
            holder.yearName.setText(parts[2]);
            holder.ruthuvuName.setText(parts[3]+" - "+ parts[4]);
            holder.paksham.setText(parts[5]+" - "+parts[6]);

            SpannableStringBuilder tithiString = utils.spanString( context,context.getString(R.string.tithi), "  "+parts[7]);
            holder.tithi.setText(tithiString);

            SpannableStringBuilder nakshatramString = utils.spanString( context,context.getString(R.string.nakshatram), "  "+parts[8]);
            holder.nakshatram.setText(nakshatramString);

            SpannableStringBuilder yogamString = utils.spanString( context,context.getString(R.string.yogam), "  "+parts[9]);
            holder.yogam.setText(yogamString);

            SpannableStringBuilder karanString = utils.spanString( context,context.getString(R.string.karan), "  "+parts[10] + " - " +parts[11]);
            holder.karan.setText(karanString);

            holder.sunrise.setText(parts[12]);
            holder.sunset.setText(parts[13]);
            holder.brahma_muhurt.setText(parts[14]);
            holder.yamagandam.setText(parts[15]);
            holder.rahuKalam.setText(parts[16]);
            holder.durmuhurtham.setText(parts[17] + "\n"+parts[18]);
            holder.varjam.setText(parts[19]);
            holder.amritKalam.setText(parts[20]+ "\n" +parts[21]);
            holder.abhijitMuhurt.setText(parts[22]);
            String noteText = context.getString(R.string.note);
            holder.note.setText(noteText);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void highlightDate(int position) {
        Log.d(TAG_NAME, "Highlighting date at position: " + position);
        Log.d(TAG_NAME, "panchTeList: " + panchTeList.toString());
        int previousSelectedPosition = selectedDatePosition;
        selectedDatePosition = position;
        // Notify item change for both the previous and new selections
        if (previousSelectedPosition != -1) {
            notifyItemChanged(previousSelectedPosition);
        }
        notifyItemChanged(selectedDatePosition);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return panchTeList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<String> panchTeList1) {
        panchTeList.clear();
        panchTeList.addAll(panchTeList1);
        Log.d(TAG_NAME,"panchTeAdapter in recylerview :"+panchTeList);
        notifyDataSetChanged();
    }


    public static class MyPanchTeItemView extends RecyclerView.ViewHolder {
        TextView panchTitle, dateVaaram, yearName, ruthuvuName, paksham, tithi, nakshatram, yogam,
                    karan, sunrise, sunset, brahma_muhurt, yamagandam, rahuKalam, durmuhurtham, varjam,
                    amritKalam, abhijitMuhurt, note;
        public MyPanchTeItemView(@NonNull View itemView) {
            super(itemView);
            panchTitle = itemView.findViewById(R.id.panch_title);
            dateVaaram = itemView.findViewById(R.id.Date_vaaram);
            yearName = itemView.findViewById(R.id.yearName);
            ruthuvuName = itemView.findViewById(R.id.ruthuName);
            paksham = itemView.findViewById(R.id.paksham);
            tithi = itemView.findViewById(R.id.tithi);
            nakshatram = itemView.findViewById(R.id.nakshatram);
            yogam = itemView.findViewById(R.id.yogam);
            karan = itemView.findViewById(R.id.karan);
            sunrise = itemView.findViewById(R.id.sunrise);
            sunset = itemView.findViewById(R.id.sunset);
            brahma_muhurt = itemView.findViewById(R.id.brahmaMuhurt);
            yamagandam = itemView.findViewById(R.id.yamagandam);
            rahuKalam = itemView.findViewById(R.id.rahuKalam);
            durmuhurtham = itemView.findViewById(R.id.durmuhurtham);
            varjam = itemView.findViewById(R.id.varjam);
            amritKalam = itemView.findViewById(R.id.amritKalam);
            abhijitMuhurt = itemView.findViewById(R.id.abhijitMuhurt);
            note = itemView.findViewById(R.id.note);
        }
    }
}
