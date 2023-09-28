package com.soumya.telugupanchangam.customviews.panchangcalenderview;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CalenderItem implements Parcelable {
    private String day;
    private String event;


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

    public static final Creator<CalenderItem> CREATOR = new Creator<CalenderItem>() {
        @Override
        public CalenderItem createFromParcel(Parcel in) {
            return new CalenderItem(in);
        }

        @Override
        public CalenderItem[] newArray(int size) {
            return new CalenderItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(event);
    }
}
