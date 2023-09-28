package com.soumya.telugupanchangam.customviews.panchangcalenderview;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CalenderItem implements Parcelable {
    private String daynumber;
    private int iday;
    private int year;
    private int month;
    private String event;

    public CalenderItem(int iday, int year, int month, String daynum, String event) {
        this.daynumber = daynum;
        this.iday = iday;
        this.year = year;
        this.month = month;
        this.event = event;
    }

    public String getDaynumber() {
        return daynumber;
    }

    public void setDaynumber(String daynumber) {
        this.daynumber = daynumber;
    }

    public int getIday() {
        return iday;
    }

    public void setIday(int iday) {
        this.iday = iday;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    protected CalenderItem(Parcel in) {
        daynumber = in.readString();
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
        dest.writeString(daynumber);
        dest.writeString(event);
    }
}
