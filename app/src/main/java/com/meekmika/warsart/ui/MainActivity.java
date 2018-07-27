package com.meekmika.warsart.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.meekmika.warsart.R;
import com.meekmika.warsart.data.StreetArtViewModel;
import com.meekmika.warsart.data.model.StreetArt;
import com.meekmika.warsart.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.meekmika.warsart.ui.BottomNavigation.SHOW_MAP;
import static com.meekmika.warsart.utils.SharedPrefsUtil.SHOW_FAVORITES_KEY;

public class MainActivity extends AppCompatActivity implements
        BottomNavigation.BottomNavigationOnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private boolean showFavorites;

    private StreetArtViewModel viewModel;
    private List<StreetArt> streetArtAll = new ArrayList<>();
    private List<StreetArt> streetArtFavorites = new ArrayList<>();
    private MutableLiveData<List<StreetArt>> streetArtList = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        showFavorites = sharedPreferences.getBoolean(SHOW_FAVORITES_KEY, false);
        setUpView();

        viewModel = ViewModelProviders.of(this).get(StreetArtViewModel.class);
        LiveData<HashMap<String, StreetArt>> streetArtLiveData = viewModel.getStreetArtLiveData();
        streetArtLiveData.observe(this, new Observer<HashMap<String, StreetArt>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, StreetArt> streetArtHashMap) {
                if (streetArtHashMap != null) {
                    viewModel.updateFavorites();
                    streetArtAll = new ArrayList<>(streetArtHashMap.values());
                    setStreetArtListData();
                }
            }
        });
        LiveData<List<StreetArt>> streetArtFavoritesLiveData = viewModel.getStreetArtFavoritesLiveData();
        streetArtFavoritesLiveData.observe(this, new Observer<List<StreetArt>>() {
            @Override
            public void onChanged(@Nullable List<StreetArt> streetArtList) {
                if (streetArtList != null) {
                    streetArtFavorites = streetArtList;
                    setStreetArtListData();
                }
            }
        });

        setStreetArtListData();
    }

    private void setUpView() {
        BottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setBottomNavigationOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

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

    public MutableLiveData<List<StreetArt>> getStreetArtList() {
        return streetArtList;
    }

    @Override
    public void onButtonClicked(String fragmentToShow) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_fragment_to_show), fragmentToShow);
        editor.apply();
        showFragment(fragmentToShow);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.getItem(0).setChecked(SharedPrefsUtil.getShowFavoritesPref(this));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_favorites:
                item.setChecked(!item.isChecked());
                SharedPrefsUtil.setShowFavoritesPref(this, item.isChecked());
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void setStreetArtListData() {
        if (showFavorites) {
            streetArtList.setValue(streetArtFavorites);
        } else {
            streetArtList.setValue(streetArtAll);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case SHOW_FAVORITES_KEY:
                showFavorites = sharedPreferences.getBoolean(key, false);
                setStreetArtListData();
                break;
            default:
                break;
        }
    }
}
