package com.soumya.telugupanchangam.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.adapter.Notificationadapter;
import com.soumya.telugupanchangam.database2.callbacksinterfaces.DeleteNotiftyItemCallback;
import com.soumya.telugupanchangam.database2.entities.NotificationsTable;
import com.soumya.telugupanchangam.database2.viewmodels.NotifyViewModel;
import com.soumya.telugupanchangam.databases.AppDatabase;
import com.soumya.telugupanchangam.databases.daos.EventDao;
import com.soumya.telugupanchangam.databases.dbtables.Eventdata;
import com.soumya.telugupanchangam.databinding.FragmentNotificationsBinding;

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

        insertFourItems();
        return root;
    }

    // Insert four items into the database
    private void insertFourItems() {
        NotificationsTable item1 = new NotificationsTable();
        item1.setNotify_event_id(16);
        item1.setNotify_date("27-09-2023");
        item1.setNotify_time("17:58");
        item1.setNotify_description("Item 1h hello hi ");
        item1.setNotify_event_name("Item 1");

        NotificationsTable item2 = new NotificationsTable();
        item2.setNotify_event_id(17);
        item2.setNotify_date("27-09-2023");
        item2.setNotify_time("18:18");
        item2.setNotify_description("Item 2h hello hi ");
        item2.setNotify_event_name("Item 2");

        NotificationsTable item3 = new NotificationsTable();
        item3.setNotify_event_id(18);
        item3.setNotify_date("27-09-2023");
        item3.setNotify_time("18:48");
        item3.setNotify_description("Item 3h hello hi ");
        item3.setNotify_event_name("Item 3");

        NotificationsTable item4 = new NotificationsTable();
        item4.setNotify_event_id(19);
        item4.setNotify_date("27-09-2023");
        item4.setNotify_time("18:58");
        item4.setNotify_description("Item 4h hello hi ");
        item4.setNotify_event_name("Item 4");

        notifyViewModel.insert(item1);
        notifyViewModel.insert(item2);
        notifyViewModel.insert(item3);
        notifyViewModel.insert(item4);
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