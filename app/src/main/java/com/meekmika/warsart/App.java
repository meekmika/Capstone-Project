package com.meekmika.warsart;

import android.app.Application;

import timber.log.Timber;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /* Cache data offline */
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        if (BuildConfig.DEBUG) {
            Timber.plant(new MyDebugTree());
        }
    }
}
