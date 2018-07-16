package com.meekmika.warsart.data;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.meekmika.warsart.data.model.StreetArt;

import java.util.ArrayList;
import java.util.List;

public class StreetArtViewModel extends ViewModel {
    private static final DatabaseReference DB_REF = FirebaseDatabase.getInstance().getReference();

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(DB_REF);

    private final LiveData<List<StreetArt>> streetArtLiveData =
            Transformations.map(liveData, new Deserializer());

    private class Deserializer implements Function<DataSnapshot, List<StreetArt>> {
        @Override
        public List<StreetArt> apply(DataSnapshot input) {
            List<StreetArt> streetArtList = new ArrayList<>();
            for (DataSnapshot child : input.getChildren()) {
                StreetArt streetArt = child.getValue(StreetArt.class);
                streetArtList.add(streetArt);
            }
            return streetArtList;
        }
    }

    @NonNull
    public LiveData<List<StreetArt>> getStreetArtLiveData() {
        return streetArtLiveData;
    }
}
