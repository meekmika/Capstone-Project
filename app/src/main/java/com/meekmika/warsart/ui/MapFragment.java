package com.meekmika.warsart.ui;

import android.Manifest;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meekmika.warsart.data.model.StreetArt;

import java.util.ArrayList;
import java.util.List;

import static com.meekmika.warsart.ui.DetailActivity.EXTRA_STREET_ART_ID;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final int PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE = 718;
    private static final LatLng WARSAW = new LatLng(52.237, 21.018);
    private static final float ZOOM_LEVEL = 12f;

    private ArrayList<StreetArt> streetArtList;
    private GoogleMap googleMap;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.getMapAsync(this);
        MainActivity mainActivity = (MainActivity) getActivity();
        MutableLiveData<List<StreetArt>> liveData = mainActivity.getStreetArtList();
        liveData.observe(this, new Observer<List<StreetArt>>() {
            @Override
            public void onChanged(@Nullable List<StreetArt> streetArtList) {
                if (streetArtList != null) {
                    MapFragment.this.streetArtList = (ArrayList<StreetArt>) streetArtList;
                    putMarkersOnMap();
                }
            }
        });
        setRetainInstance(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setupPermissions();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String streetArtId = (String) marker.getTag();
                Intent intentToStartDetailActivity = new Intent(getContext(), DetailActivity.class);
                intentToStartDetailActivity.putExtra(EXTRA_STREET_ART_ID, streetArtId);
                startActivity(intentToStartDetailActivity);
                return true;
            }
        });
        putMarkersOnMap();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(WARSAW));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WARSAW, ZOOM_LEVEL));
    }

    private void putMarkersOnMap() {
        if (streetArtList != null && googleMap != null) {
            googleMap.clear();
            for (int i = 0; i < streetArtList.size(); i++) {
                StreetArt streetArt = streetArtList.get(i);
                LatLng position = new LatLng(streetArt.getLat(), streetArt.getLng());
                googleMap.addMarker(new MarkerOptions().position(position)).setTag(streetArt.getId());
            }
        }
    }

    private void setupPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(locationPermission, PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE);
            }
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }
            }
        }
    }
}
