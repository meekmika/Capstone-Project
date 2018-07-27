package com.meekmika.warsart.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import java.util.HashSet;

import static com.meekmika.warsart.utils.SharedPrefsUtil.FAVORITES_KEY;

public class FavoriteButton extends ToggleButton {

    private String streetArtId;

    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoriteButton(Context context) {
        super(context);
    }

    @Override
    public void toggle() {
        super.toggle();
        if (this.isChecked()) {
            if (streetArtId != null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                HashSet<String> favorites = (HashSet<String>) prefs.getStringSet(FAVORITES_KEY, new HashSet<String>());
                favorites.add(streetArtId);
                // remove preference to trigger listener
                editor.remove(FAVORITES_KEY);
                editor.commit();
                editor.putStringSet(FAVORITES_KEY, favorites);
                editor.apply();
            }
        } else {
            if (streetArtId != null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                HashSet<String> favorites = (HashSet<String>) prefs.getStringSet(FAVORITES_KEY, new HashSet<String>());
                favorites.remove(streetArtId);
                // remove preference to trigger listener
                editor.remove(FAVORITES_KEY);
                editor.commit();
                editor.putStringSet(FAVORITES_KEY, favorites);
                editor.apply();
            }
        }
    }

    public void setStreetArtId(String streetArtId) {
        this.streetArtId = streetArtId;
    }
}
