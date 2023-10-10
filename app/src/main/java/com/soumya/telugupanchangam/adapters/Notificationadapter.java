package com.soumya.telugupanchangam.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.databases.callbacksinterfaces.DeleteNotiftyItemCallback;
import com.soumya.telugupanchangam.databases.dbtables.NotificationsTable;
import com.soumya.telugupanchangam.databases.livedatamodel.NotifyViewModel;

import java.util.ArrayList;
import java.util.List;

public class Notificationadapter extends RecyclerView.Adapter<Notificationadapter.MyNotificationAdapter> {
    private List<NotificationsTable> dataList = new ArrayList<>();
    private final Context context;
    private NotifyViewModel viewModel;
    private final DeleteNotiftyItemCallback deleteNotiftyItemCallback;

    public Notificationadapter(Context context, NotifyViewModel viewModel,DeleteNotiftyItemCallback deleteNotiftyItemCallback) {
        this.context = context;
        this.viewModel = viewModel;
        this.deleteNotiftyItemCallback = deleteNotiftyItemCallback;
    }

    @NonNull
    @Override
    public Notificationadapter.MyNotificationAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list, parent, false);
        return new MyNotificationAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Notificationadapter.MyNotificationAdapter holder, int position) {
        NotificationsTable entity = dataList.get(position);
        holder.bind(entity);

    }
    @SuppressLint("NotifyDataSetChanged")
    public void setDataList(List<NotificationsTable> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyNotificationAdapter extends RecyclerView.ViewHolder {

        private final TextView event_name, event_desc, event_date, event_time, event_type;

        public MyNotificationAdapter(@NonNull View itemView) {
            super(itemView);

            event_type = itemView.findViewById(R.id.Notify_event_type);
            event_date = itemView.findViewById(R.id.Notify_event_date);
            event_time = itemView.findViewById(R.id.Notify_event_time);
            event_name = itemView.findViewById(R.id.Notify_event_name);
            event_desc = itemView.findViewById(R.id.Notify_event_desc);
            ImageButton deleteButton = itemView.findViewById(R.id.button_delete);

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    NotificationsTable entity = dataList.get(position);
                    //viewModel.delete(entity);
                    deleteNotiftyItemCallback.onDeleteItem(entity);
                }
            });

        }

        void bind(NotificationsTable entity) {
            event_type.setText(entity.getNotify_eventtype());
            event_date.setText(entity.getNotify_date());
            event_time.setText(entity.getNotify_time());
            event_name.setText(entity.getNotify_event_name());
            event_desc.setText(entity.getNotify_description());
        }

    }
}
