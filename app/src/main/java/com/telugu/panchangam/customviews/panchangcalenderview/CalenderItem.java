package com.telugu.panchangam.customviews.panchangcalenderview;

import android.os.Parcel;

public class CalenderItem  {
    private String day;
    private String event;
    private boolean isSelected;


    public CalenderItem(String day, String event) {
        this.day = day;
        this.event = event;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    protected CalenderItem(Parcel in) {
        day = in.readString();
        event = in.readString();
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
