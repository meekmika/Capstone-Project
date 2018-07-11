package com.meekmika.warsart.ui;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import timber.log.Timber;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.getMapAsync(this);
        setRetainInstance(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Timber.d("Map Ready");
        LatLng warsaw = new LatLng(52.237, 21.018);
        googleMap.addMarker(new MarkerOptions().position(warsaw)
                .title("Marker in Warsaw"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(warsaw));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw, 12f));
    }
}
