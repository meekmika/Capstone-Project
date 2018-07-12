package com.meekmika.warsart;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.meekmika.warsart.data.remote.FirebaseHandler;

import timber.log.Timber;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new MyDebugTree());
        }
    }
}
