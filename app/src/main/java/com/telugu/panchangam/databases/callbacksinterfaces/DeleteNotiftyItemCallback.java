package com.telugu.panchangam.databases.callbacksinterfaces;

import com.telugu.panchangam.databases.dbtables.NotificationsTable;

public interface DeleteNotiftyItemCallback {
    void onDeleteItem(NotificationsTable entity);
}
