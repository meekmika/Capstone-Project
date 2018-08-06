package com.meekmika.warsart.data;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

/**
 * https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 */

public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {

    private static final int DELAY_MILLIS = 2000;
    private final Query query;
    private final MyValueEventListener listener = new MyValueEventListener();
    private boolean listenerRemovePending = false;
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(listener);
            listenerRemovePending = false;
        }
    };
    private Handler handler = new Handler();

    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }

    public FirebaseQueryLiveData(DatabaseReference ref) {
        this.query = ref;
    }

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        } else {
            query.addValueEventListener(listener);
        }

        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        handler.postDelayed(removeListener, DELAY_MILLIS);
        listenerRemovePending = true;
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Timber.e("Can't listen to query %s", query);
            Timber.e(databaseError.toException());
        }
    }
}
