package com.meekmika.warsart.ui;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meekmika.warsart.data.model.StreetArt;
import com.meekmika.warsart.data.remote.FirebaseHandler;
import com.meekmika.warsart.utils.GeoUtil;

import java.util.List;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, FirebaseHandler.OnDataReadyCallback {

    private static final LatLng WARSAW = new LatLng(52.237, 21.018);
    private List<StreetArt> streetArtList;
    private GoogleMap googleMap;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.getMapAsync(this);
        FirebaseHandler.getStreetArtListAsync(this);
        setRetainInstance(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        putMarkersOnMap();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(WARSAW));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WARSAW, 12f));
    }

    private void putMarkersOnMap() {
        if (streetArtList != null && googleMap != null) {
            for (StreetArt streetArt : streetArtList) {
                LatLng position = GeoUtil.getCoordinates(getContext(), streetArt.getAddress());
                if (position != null) {
                    googleMap.addMarker(new MarkerOptions().position(position));
                }
            }
        }
    }

    @Override
    public void onDataReady(List<StreetArt> streetArtList) {
        this.streetArtList = streetArtList;
        putMarkersOnMap();
    }

    @Override
    public void onError() {

    }
}
