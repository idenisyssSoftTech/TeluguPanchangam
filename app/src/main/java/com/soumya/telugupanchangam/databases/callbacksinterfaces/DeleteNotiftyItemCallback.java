package com.soumya.telugupanchangam.databases.callbacksinterfaces;

import com.soumya.telugupanchangam.databases.dbtables.NotificationsTable;

public interface DeleteNotiftyItemCallback {
    void onDeleteItem(NotificationsTable entity);
}
