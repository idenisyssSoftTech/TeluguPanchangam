package com.telugu.panchangam.sqliteDB.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.telugu.panchangam.utils.AppConstants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SqliteDBHelper extends SQLiteOpenHelper {
    private final String TAG_NAME = "SqliteDBHelper";
    private final Context context;

    public SqliteDBHelper(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create FestivalsTe table
        db.execSQL("CREATE TABLE IF NOT EXISTS FestivalsTe (FDate TEXT, FestivalDate TEXT, " +
                "Vaaram TEXT, FestivalName TEXT, FestivalMonth TEXT, FestivalYear INTEGER, color TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS PanchTe (PDate TEXT, Tithi1 TEXT, " +
                "Tithi2 TEXT, Nakshatram1 TEXT, Nakshatram2 TEXT, Yogam1 TEXT, Yogam2 TEXT, Karan1 TEXT, Karan2 TEXT, " +
                "Karan3 TEXT, Vaaram TEXT, Sunrise TEXT, Sunset TEXT, YearName TEXT, MonthName TEXT, Paksha TEXT, " +
                "GulikaKalam TEXT, Yamagandam TEXT, Durmuhurtham1 TEXT, Durmuhurtham2 TEXT, Varjam1 TEXT, Varjam2 TEXT," +
                "RahuKalam TEXT, AbhijitMuhurt TEXT, AmritKalam1 TEXT, AmritKalam2 TEXT, Ayanam TEXT, RuthuvuName TEXT," +
                "DateName TEXT, FestivalName TEXT, BrahmaMuhurt TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    // Copy the database from assets to internal storage on the first run
    public void copyDatabaseFromAssets() {
        try {
            InputStream inputStream = context.getAssets().open(AppConstants.DATABASE_NAME);
            String outFileName = context.getDatabasePath(AppConstants.DATABASE_NAME).getPath();
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG_NAME, "Error copying database", e);
        }
    }

    public List<String> getFestivalData() {
        List<String> festivalDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define your SQL query here
        String query = "SELECT * FROM FestivalsTe";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                // Assuming there's a column named "column_name" in your table
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex("FestivalName"));
                festivalDataList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return festivalDataList;
    }

    public List<String> getFestivalDataForMonth(String month, int year) {
        List<String> festivalDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define your SQL query to filter data based on month and year
        String query = "SELECT * FROM FestivalsTe WHERE FestivalMonth = ? AND FestivalYear = ?";
        Log.d(TAG_NAME, "SQL Query: " + query);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{month, String.valueOf(year)});
            Log.d(TAG_NAME, "month : " + month);
            Log.d(TAG_NAME, "year : " + year);
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String festivalDate = cursor.getString(cursor.getColumnIndex("FestivalDate"));
                    @SuppressLint("Range") String vaaram = cursor.getString(cursor.getColumnIndex("Vaaram"));
                    @SuppressLint("Range") String festivalName = cursor.getString(cursor.getColumnIndex("FestivalName"));
                    String formattedFestivalData = festivalDate + " - " + vaaram + " - " + festivalName;
                    festivalDataList.add(formattedFestivalData);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG_NAME, "Error querying database: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            Log.d(TAG_NAME, "festivalDataList : " + festivalDataList);
        }
        return festivalDataList;
    }

    @SuppressLint("Range")
    public List<String> getPanchTeDataForDate(String Vaaram, String DateName) {
        List<String> panchTeDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define your SQL query to filter data based on the selected date
        String query = "SELECT * FROM PanchTe WHERE Vaaram = ? AND DateName = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{Vaaram, DateName});
            Log.d(TAG_NAME, "Vaaram : " + Vaaram);
            Log.d(TAG_NAME, "DateName : " + DateName);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String c0 = cursor.getString(cursor.getColumnIndex("Vaaram"));
                    String c1 = cursor.getString(cursor.getColumnIndex("DateName"));
                    String c2 = cursor.getString(cursor.getColumnIndex("YearName"));
                    String c3 = cursor.getString(cursor.getColumnIndex("Ayanam"));
                    String c4 = cursor.getString(cursor.getColumnIndex("RuthuvuName"));
                    String c5 = cursor.getString(cursor.getColumnIndex("MonthName"));
                    String c6 = cursor.getString(cursor.getColumnIndex("Paksha"));
                    String c7 = cursor.getString(cursor.getColumnIndex("Tithi1"));
                    String c8 = cursor.getString(cursor.getColumnIndex("Nakshatram1"));
                    String c9 = cursor.getString(cursor.getColumnIndex("Yogam1"));
                    String c10 = cursor.getString(cursor.getColumnIndex("Karan1"));
                    String c11 = cursor.getString(cursor.getColumnIndex("Karan2"));
                    String c12 = cursor.getString(cursor.getColumnIndex("Sunrise"));
                    String c13 = cursor.getString(cursor.getColumnIndex("Sunset"));
                    String c14 = cursor.getString(cursor.getColumnIndex("BrahmaMuhurt"));
                    String c15 = cursor.getString(cursor.getColumnIndex("Yamagandam"));
                    String c16 = cursor.getString(cursor.getColumnIndex("RahuKalam"));
                    String c17 = cursor.getString(cursor.getColumnIndex("Durmuhurtham1"));
                    String c18 = cursor.getString(cursor.getColumnIndex("Durmuhurtham2"));
                    String c19 = cursor.getString(cursor.getColumnIndex("Varjam1"));
                    String c20 = cursor.getString(cursor.getColumnIndex("AmritKalam1"));
                    String c21 = cursor.getString(cursor.getColumnIndex("AmritKalam2"));
                    String c22 = cursor.getString(cursor.getColumnIndex("AbhijitMuhurt"));
                    String c23 = cursor.getString(cursor.getColumnIndex("GulikaKalam"));
                    String c24 = cursor.getString(cursor.getColumnIndex("FestivalName"));


                    String formattedData = (c0 + " - " + c1 + " - " +c2 + " - " +c3 + " - " +c4 + " - " + c5 + " - " + c6 + " - "+
                            c7 + " - " + c8 + " - " + c9+ " - " + c10+ " - " + c11+ " - " + c12+ " - " + c13+ " - " + c14+ " - " + c15+ " - "+
                            c16 + " - " + c17 + " - " + c18 + " - " + c19 + " - " + c20 + " - " + c21 + " - " + c22 + " - " + c23 + " - " + c24);


                    panchTeDataList.add(formattedData);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG_NAME, "Error querying database: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            Log.d(TAG_NAME, "panchTeDataList : " + panchTeDataList);
        }
        return panchTeDataList;
    }
}

