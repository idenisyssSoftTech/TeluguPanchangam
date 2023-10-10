package com.soumya.telugupanchangam.databases.callbacksinterfaces;

import com.soumya.telugupanchangam.databases.dbtables.Eventdata;

public interface DeleteEventItemCallback {
    void onDeleteItem(Eventdata entity);
}
