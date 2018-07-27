package com.meekmika.warsart.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.meekmika.warsart.R;
import com.meekmika.warsart.data.StreetArtViewModel;
import com.meekmika.warsart.data.model.StreetArt;
import com.meekmika.warsart.utils.SharedPrefsUtil;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_STREET_ART_ID = "extra_street_art_id";
    public static final String STREET_ART = "extra_street_art";

    private String streetArtId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        streetArtId = getIntent().getStringExtra(EXTRA_STREET_ART_ID);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FavoriteButton favoriteButton = findViewById(R.id.btn_favorite);
        favoriteButton.setStreetArtId(streetArtId);
        favoriteButton.setChecked(SharedPrefsUtil.isFavorite(this, streetArtId));

        StreetArtViewModel viewModel = ViewModelProviders.of(this).get(StreetArtViewModel.class);
        LiveData<HashMap<String, StreetArt>> liveData = viewModel.getStreetArtLiveData();
        liveData.observe(this, new Observer<HashMap<String, StreetArt>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, StreetArt> streetArtHashMap) {
                if (streetArtHashMap != null) {
                    StreetArt streetArt = streetArtHashMap.get(streetArtId);
                    getSupportActionBar().setTitle(streetArt.getTitle());
                    DetailFragment detailFragment = DetailFragment.newInstance(streetArt);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, detailFragment)
                            .commit();
                }
            }
        });

    }
}
