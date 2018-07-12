package com.meekmika.warsart.data.remote;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meekmika.warsart.data.model.StreetArt;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHandler {

    private static final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static List<StreetArt> streetArtList;

    public static void getStreetArtListAsync(final onDataReadyCallback callback) {

        if (streetArtList == null) {
            streetArtList = new ArrayList<>();
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        StreetArt streetArt = child.getValue(StreetArt.class);
                        streetArtList.add(streetArt);
                    }
                    callback.onData(streetArtList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    callback.onError();
                }
            });
        } else {
            callback.onData(streetArtList);
        }
    }

    public interface onDataReadyCallback {
        void onData(List<StreetArt> streetArtList);

        void onError();
    }

}
