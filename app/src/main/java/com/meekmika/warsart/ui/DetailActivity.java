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

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_STREET_ART_ID = "extra_street_art_id";
    public static final String STREET_ART = "extra_street_art";

    private int streetArtIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        streetArtIndex = getIntent().getIntExtra(EXTRA_STREET_ART_ID, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StreetArtViewModel viewModel = ViewModelProviders.of(this).get(StreetArtViewModel.class);
        LiveData<List<StreetArt>> liveData = viewModel.getStreetArtLiveData();
        liveData.observe(this, new Observer<List<StreetArt>>() {
            @Override
            public void onChanged(@Nullable List<StreetArt> streetArtList) {
                if (streetArtList != null) {
                    StreetArt streetArt = streetArtList.get(streetArtIndex);
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
