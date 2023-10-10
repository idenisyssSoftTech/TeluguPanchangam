package com.telugu.panchangam.databases.dbtables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "eventsdata")
public class Eventdata implements Serializable {
        @PrimaryKey(autoGenerate = true)
        private int id;

       @ColumnInfo(name = "eventName")
        private String name;
       @ColumnInfo(name = "date")
        private String date;
        @ColumnInfo(name = "time")
        private String time;

        @ColumnInfo(name = "timemillis")
        private long selectedTimeMillis;
         @ColumnInfo(name = "description")
        private String description;
        @ColumnInfo(name = "eventType")
        private String eventType;

        private int notificationId;
        // Constructors, getters, and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getSelectedTimeMillis() {
        return selectedTimeMillis;
    }

    public void setSelectedTimeMillis(long selectedTimeMillis) {
        this.selectedTimeMillis = selectedTimeMillis;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}
