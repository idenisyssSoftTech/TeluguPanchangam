package com.soumya.telugupanchangam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.database2.callbacksinterfaces.DeleteNotiftyItemCallback;
import com.soumya.telugupanchangam.database2.entities.NotificationsTable;
import com.soumya.telugupanchangam.database2.viewmodels.NotifyViewModel;

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
    public void setDataList(List<NotificationsTable> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyNotificationAdapter extends RecyclerView.ViewHolder {

        private final TextView dataTextView;
        private final ImageButton editButton;

        public MyNotificationAdapter(@NonNull View itemView) {
            super(itemView);

            dataTextView = itemView.findViewById(R.id.text_view_data);
            ImageButton deleteButton = itemView.findViewById(R.id.button_delete);
            editButton = itemView.findViewById(R.id.button_edit);

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    NotificationsTable entity = dataList.get(position);
                    //viewModel.delete(entity);
                    deleteNotiftyItemCallback.onDeleteItem(entity);
                }
            });

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Handle edit functionality here
                }
            });
        }

        void bind(NotificationsTable entity) {
            dataTextView.setText(entity.getNotify_description());
        }

    }
}
