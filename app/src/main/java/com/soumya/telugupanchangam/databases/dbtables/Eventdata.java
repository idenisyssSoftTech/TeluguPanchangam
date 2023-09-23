package com.soumya.telugupanchangam.databases.dbtables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "eventsdata")
public class Eventdata {
        @PrimaryKey(autoGenerate = true)
        private long id;

       @ColumnInfo(name = "eventName")
        private String name;
       @ColumnInfo(name = "date")
        private String date;
        @ColumnInfo(name = "time")
        private String time;
         @ColumnInfo(name = "description")
        private String description;
        @ColumnInfo(name = "eventType")
        private String eventType;

        // Constructors, getters, and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
