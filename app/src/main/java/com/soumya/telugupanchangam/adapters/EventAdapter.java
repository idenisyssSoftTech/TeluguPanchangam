package com.soumya.telugupanchangam.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.databases.callbacksinterfaces.DeleteEventItemCallback;
import com.soumya.telugupanchangam.databases.dbtables.Eventdata;

public class EventAdapter extends ListAdapter<Eventdata, EventAdapter.MyEventView> {
    public DeleteEventItemCallback deleteEventItemCallback;
    private EditEventCallback editEventCallback;

    public EventAdapter(DeleteEventItemCallback listener,EditEventCallback editEventCallback) {
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
        this.deleteEventItemCallback = listener;
        this.editEventCallback = editEventCallback;
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

        holder.delete.setOnClickListener(view -> {
            int position1 = holder.getAdapterPosition();
            if (position1 != RecyclerView.NO_POSITION) {
                deleteEventItemCallback.onDeleteItem(event);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position1 = holder.getAdapterPosition();
                if (position1 != RecyclerView.NO_POSITION) {
                    // Call the editEventCallback to handle editing
                    editEventCallback.onEditEvent(event); // Call the method to handle editing
                }
            }
        });
    }


    public static class MyEventView extends RecyclerView.ViewHolder {
        TextView eventName, eventTime, eventType, eventDesc;
        ImageButton edit, delete;

        public MyEventView(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventTime = itemView.findViewById(R.id.event_time);
            eventType = itemView.findViewById(R.id.event_type);
            eventDesc = itemView.findViewById(R.id.event_desc);
            edit = itemView.findViewById(R.id.button_edit);
            delete = itemView.findViewById(R.id.button_delete);
        }
    }

    // Define a callback interface for editing events
    public interface EditEventCallback {
        void onEditEvent(Eventdata event);
    }

}