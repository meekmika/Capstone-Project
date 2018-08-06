package com.meekmika.warsart.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
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
import android.widget.ImageView;

import com.meekmika.warsart.R;
import com.meekmika.warsart.adapters.StreetArtAdapter;
import com.meekmika.warsart.data.model.StreetArt;

import java.util.List;

import static com.meekmika.warsart.ui.DetailActivity.EXTRA_STREET_ART_ID;

public class ListFragment extends Fragment implements StreetArtAdapter.StreetArtAdapterOnClickHandler {

    private StreetArtAdapter adapter;
    private ImageView placeholder;

    public ListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new StreetArtAdapter();
        adapter.setOnClickHandler(this);
        MainActivity mainActivity = (MainActivity) getActivity();
        MutableLiveData<List<StreetArt>> liveData = mainActivity.getStreetArtList();
        liveData.observe(this, new Observer<List<StreetArt>>() {
            @Override
            public void onChanged(@Nullable List<StreetArt> streetArtList) {
                if (streetArtList != null) {
                    adapter.setStreetArtData(streetArtList);
                    if (adapter.getItemCount() != 0) placeholder.setVisibility(View.GONE);
                    else placeholder.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        placeholder = rootView.findViewById(R.id.placeholder);

        RecyclerView recyclerView = rootView.findViewById(R.id.street_art_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(String streetArtId) {
        Intent intentToStartDetailActivity = new Intent(getContext(), DetailActivity.class);
        intentToStartDetailActivity.putExtra(EXTRA_STREET_ART_ID, streetArtId);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
