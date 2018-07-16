package com.meekmika.warsart.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.meekmika.warsart.R;

import static com.meekmika.warsart.ui.BottomNavigation.SHOW_MAP;

public class MainActivity extends AppCompatActivity implements
        BottomNavigation.BottomNavigationOnClickListener {

    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setUpView();
    }

    private void setUpView() {
        BottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setBottomNavigationOnClickListener(this);

        String fragmentToShow = sharedPreferences.getString(getString(R.string.pref_fragment_to_show), SHOW_MAP);
        showFragment(fragmentToShow);
    }

    private void showFragment(String fragmentToShow) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentToShow);
        if (fragment == null) {
            fragment = fragmentToShow.equals(SHOW_MAP) ? new MapFragment() : new ListFragment();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, fragmentToShow)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onButtonClicked(String fragmentToShow) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_fragment_to_show), fragmentToShow);
        editor.apply();
        showFragment(fragmentToShow);
    }

}
