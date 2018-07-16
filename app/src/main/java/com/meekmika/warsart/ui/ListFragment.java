package com.meekmika.warsart.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.meekmika.warsart.R;
import com.meekmika.warsart.adapters.StreetArtAdapter;
import com.meekmika.warsart.data.StreetArtViewModel;
import com.meekmika.warsart.data.model.StreetArt;

import java.util.ArrayList;
import java.util.List;

import static com.meekmika.warsart.ui.DetailActivity.EXTRA_STREET_ART_ID;

public class ListFragment extends Fragment implements StreetArtAdapter.StreetArtAdapterOnClickHandler {

    private StreetArtAdapter adapter;

    public ListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new StreetArtAdapter();
        adapter.setOnClickHandler(this);
        StreetArtViewModel viewModel = ViewModelProviders.of(this).get(StreetArtViewModel.class);
        LiveData<List<StreetArt>> liveData = viewModel.getStreetArtLiveData();
        liveData.observe(this, new Observer<List<StreetArt>>() {
            @Override
            public void onChanged(@Nullable List<StreetArt> streetArtList) {
                if (streetArtList != null) {
                    adapter.setStreetArtData(streetArtList);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.street_art_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(int streetArtIndex) {
        Intent intentToStartDetailActivity = new Intent(getContext(), DetailActivity.class);
        intentToStartDetailActivity.putExtra(EXTRA_STREET_ART_ID, streetArtIndex);
        startActivity(intentToStartDetailActivity);
    }
}
