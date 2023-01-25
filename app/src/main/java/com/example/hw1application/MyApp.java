package com.example.hw1application;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MySPV.init(this);
        MySignal.init(this);


    }
}
