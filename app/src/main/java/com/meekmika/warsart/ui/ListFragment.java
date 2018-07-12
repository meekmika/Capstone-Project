package com.meekmika.warsart.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meekmika.warsart.R;
import com.meekmika.warsart.adapters.StreetArtAdapter;
import com.meekmika.warsart.data.model.StreetArt;
import com.meekmika.warsart.data.remote.FirebaseHandler;

import java.util.List;

public class ListFragment extends Fragment implements FirebaseHandler.OnDataReadyCallback {

    private StreetArtAdapter adapter;

    public ListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new StreetArtAdapter();
        FirebaseHandler.getStreetArtListAsync(this);
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
    public void onDataReady(List<StreetArt> streetArtList) {
        adapter.setStreetArtData(streetArtList);
    }

    @Override
    public void onError() {

    }
}
