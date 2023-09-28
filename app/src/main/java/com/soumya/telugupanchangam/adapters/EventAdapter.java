package com.soumya.telugupanchangam.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.databases.dbtables.Eventdata;

public class EventAdapter extends ListAdapter<Eventdata, EventAdapter.MyEventView> {

    public EventAdapter() {
        super(new DiffUtil.ItemCallback<Eventdata>() {
            @Override
            public boolean areItemsTheSame(@NonNull Eventdata oldItem, @NonNull Eventdata newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Eventdata oldItem, @NonNull Eventdata newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public MyEventView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list, parent, false);
        return new MyEventView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventView holder, int position) {
        Eventdata event = getItem(position);
        holder.eventName.setText(event.getName());
        holder.eventTime.setText(event.getTime());
        holder.eventType.setText(event.getEventType());
        holder.eventDesc.setText(event.getDescription());
    }

    public static class MyEventView extends RecyclerView.ViewHolder {
        TextView eventName, eventTime, eventType, eventDesc;

        public MyEventView(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventTime = itemView.findViewById(R.id.event_time);
            eventType = itemView.findViewById(R.id.event_type);
            eventDesc = itemView.findViewById(R.id.event_desc);
        }
    }
}