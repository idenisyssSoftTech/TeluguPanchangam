package com.telugu.panchangam.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.telugu.panchangam.R;
import com.telugu.panchangam.adapters.Notificationadapter;
import com.telugu.panchangam.databases.callbacksinterfaces.DeleteNotiftyItemCallback;
import com.telugu.panchangam.databases.dbtables.NotificationsTable;
import com.telugu.panchangam.databases.livedatamodel.NotifyViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment implements DeleteNotiftyItemCallback {

    private final String TAG_NAME = "NotificationsFragment";
    RecyclerView notification_recycler_view;
    LinearLayout linearLayout;

    NotifyViewModel notifyViewModel;
    Notificationadapter notificationadapter;
    List<NotificationsTable> notificationsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notifyViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(NotifyViewModel.class);
        notificationadapter = new Notificationadapter(getActivity(), notifyViewModel,this);
        notificationsList = new ArrayList<>();
    }

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        notification_recycler_view = root.findViewById(R.id.notification_recycler_view);
        linearLayout = root.findViewById(R.id.linear_notify_no_data);

        notification_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        notification_recycler_view.setHasFixedSize(true);
        notification_recycler_view.setAdapter(notificationadapter);

        notifyViewModel.getAllData().observe(getViewLifecycleOwner(), notifications ->{
            notificationsList = notifications;
            notificationadapter.setDataList(notifications);
            Log.d(TAG_NAME, "notification count : "+notifications.size());
            if(notifications.size() > 0){
                notification_recycler_view.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }else {
                notification_recycler_view.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDeleteItem(NotificationsTable entity) {
        // Delete the item by its ID
        notifyViewModel.delete(entity);
    }
}