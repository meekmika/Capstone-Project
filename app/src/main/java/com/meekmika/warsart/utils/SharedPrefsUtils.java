package com.meekmika.warsart.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;

public class SharedPrefsUtils {
    public static final String FAVORITES_KEY = "favorites_key";
    public static final String SHOW_FAVORITES_KEY = "show_favorites";

    public static HashSet<String> loadFavoritesPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return (HashSet<String>) prefs.getStringSet(FAVORITES_KEY, new HashSet<String>());
    }

    public static boolean isFavorite(Context context, String streetArtId) {
        HashSet<String> favorites = loadFavoritesPref(context);
        return favorites.contains(streetArtId);
    }

    public static void setShowFavoritesPref(Context context, boolean showFavorites) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
        prefs.putBoolean(SHOW_FAVORITES_KEY, showFavorites);
        prefs.apply();
    }

    public static boolean getShowFavoritesPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(SHOW_FAVORITES_KEY, false);
    }
}
