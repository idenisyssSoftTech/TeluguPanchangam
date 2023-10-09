package com.soumya.telugupanchangam.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.adapters.Notificationadapter;
import com.soumya.telugupanchangam.databases.callbacksinterfaces.DeleteNotiftyItemCallback;
import com.soumya.telugupanchangam.databases.dbtables.NotificationsTable;
import com.soumya.telugupanchangam.databases.livedatamodel.NotifyViewModel;

import java.util.List;

public class NotificationsFragment extends Fragment implements DeleteNotiftyItemCallback {

    RecyclerView notification_recycler_view;
    LinearLayoutManager linearLayoutManager;

    NotifyViewModel notifyViewModel;
    Notificationadapter notificationadapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        notification_recycler_view = root.findViewById(R.id.notification_recycler_view);
        notification_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        notification_recycler_view.setHasFixedSize(true);

        notificationadapter = new Notificationadapter(getActivity(), notifyViewModel,this);
        notification_recycler_view.setAdapter(notificationadapter);


        notifyViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(NotifyViewModel.class);
        if (notifyViewModel != null) {
            notifyViewModel.getAllData().observe(getActivity(), new Observer<List<NotificationsTable>>() {
                @Override
                public void onChanged(List<NotificationsTable> entities) {
                    notificationadapter.setDataList(entities);
                }
            });
        }
//        insertFourItems();
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