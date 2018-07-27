package com.meekmika.warsart;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        if (BuildConfig.DEBUG) {
            Timber.plant(new MyDebugTree());
        }
    }
}
