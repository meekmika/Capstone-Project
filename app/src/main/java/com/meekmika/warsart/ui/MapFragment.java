package com.meekmika.warsart.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meekmika.warsart.data.model.StreetArt;
import com.meekmika.warsart.data.remote.FirebaseHandler;
import com.meekmika.warsart.utils.GeoUtil;

import java.util.List;

import static com.meekmika.warsart.ui.DetailActivity.EXTRA_STREET_ART_ID;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, FirebaseHandler.OnDataReadyCallback {

    private static final int PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE = 718;
    private static final LatLng WARSAW = new LatLng(52.237, 21.018);
    private static final float ZOOM_LEVEL = 12f;

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
        setupPermissions();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int streetArtIndex = (int) marker.getTag();
                Intent intentToStartDetailActivity = new Intent(getContext(), DetailActivity.class);
                intentToStartDetailActivity.putExtra(EXTRA_STREET_ART_ID, streetArtIndex);
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
            for (int i = 0; i < streetArtList.size(); i++) {
                LatLng position = GeoUtil.getCoordinates(getContext(), streetArtList.get(i).getAddress());
                if (position != null) {
                    googleMap.addMarker(new MarkerOptions().position(position)).setTag(i);
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
