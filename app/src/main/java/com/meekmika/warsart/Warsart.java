package com.meekmika.warsart;

import android.app.Application;

import timber.log.Timber;

public class Warsart extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
