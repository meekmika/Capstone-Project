package com.meekmika.warsart.data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.meekmika.warsart.data.model.StreetArt;
import com.meekmika.warsart.utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.meekmika.warsart.utils.SharedPrefsUtils.FAVORITES_KEY;

/**
 * https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 */

public class StreetArtViewModel extends AndroidViewModel {
    private static final DatabaseReference DB_REF = FirebaseDatabase.getInstance().getReference();
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(DB_REF);
    private final LiveData<HashMap<String, StreetArt>> streetArtLiveData = Transformations.map(liveData, new Deserializer());
    private HashSet<String> favoriteIds;
    private MutableLiveData<List<StreetArt>> streetArtFavoritesLiveData = new MutableLiveData<>();

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            switch (key) {
                case FAVORITES_KEY:
                    updateFavorites();
                    break;
                default:
                    break;
            }
        }
    };

    public StreetArtViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        favoriteIds = SharedPrefsUtils.loadFavoritesPref(getApplication());
        updateFavorites();
    }

    @NonNull
    public LiveData<HashMap<String, StreetArt>> getStreetArtLiveData() {
        return streetArtLiveData;
    }

    @NonNull
    public MutableLiveData<List<StreetArt>> getStreetArtFavoritesLiveData() {
        return streetArtFavoritesLiveData;
    }

    public void updateFavorites() {
        favoriteIds = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(getApplication()).getStringSet(FAVORITES_KEY, favoriteIds);
        if (streetArtLiveData.getValue() != null) {
            ArrayList<StreetArt> favorites = new ArrayList<>();
            for (String id : favoriteIds) {
                favorites.add(streetArtLiveData.getValue().get(id));
            }
            streetArtFavoritesLiveData.setValue(favorites);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        PreferenceManager.getDefaultSharedPreferences(getApplication()).unregisterOnSharedPreferenceChangeListener(listener);
    }


    private class Deserializer implements Function<DataSnapshot, HashMap<String, StreetArt>> {
        @Override
        public HashMap<String, StreetArt> apply(DataSnapshot input) {
            HashMap<String, StreetArt> streetArtList = new HashMap<>();
            for (DataSnapshot child : input.getChildren()) {
                StreetArt streetArt = child.getValue(StreetArt.class);
                streetArtList.put(streetArt.getId(), streetArt);
            }
            return streetArtList;
        }
    }
}
