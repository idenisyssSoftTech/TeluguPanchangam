package com.soumya.telugupanchangam.database2.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class NotificationsTable {
    @PrimaryKey(autoGenerate = true)
    private  long notify_id;

    @ColumnInfo(name = "event_id")
    private long notify_event_id;
    @ColumnInfo(name = "eventName")
    private String notify_event_name;
    @ColumnInfo(name = "date")
    private String notify_date;
    @ColumnInfo(name = "time")
    private String notify_time;

    @ColumnInfo(name =  "description")
    private  String notify_description;

    @ColumnInfo(name = "eventType")
    private String notify_eventtype;


    public long getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(long notify_id) {
        this.notify_id = notify_id;
    }

    public long getNotify_event_id() {
        return notify_event_id;
    }

    public void setNotify_event_id(long notify_event_id) {
        this.notify_event_id = notify_event_id;
    }

    public String getNotify_event_name() {
        return notify_event_name;
    }

    public void setNotify_event_name(String notify_event_name) {
        this.notify_event_name = notify_event_name;
    }

    public String getNotify_date() {
        return notify_date;
    }

    public void setNotify_date(String notify_date) {
        this.notify_date = notify_date;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getNotify_description() {
        return notify_description;
    }

    public void setNotify_description(String notify_description) {
        this.notify_description = notify_description;
    }

    public String getNotify_eventtype() {
        return notify_eventtype;
    }

    public void setNotify_eventtype(String notify_eventtype) {
        this.notify_eventtype = notify_eventtype;
    }
}
