package com.soumya.telugupanchangam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class MainActiivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // CsvReader.readCsvFile(this); // 'this' refers to the context of your activity or fragment

    }
}