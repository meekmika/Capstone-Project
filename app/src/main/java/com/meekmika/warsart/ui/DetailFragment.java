package com.meekmika.warsart.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meekmika.warsart.R;
import com.meekmika.warsart.adapters.ImagePagerAdapter;
import com.meekmika.warsart.data.model.StreetArt;
import com.meekmika.warsart.utils.NetworkUtils;

import java.net.URL;

import static android.content.Context.LOCATION_SERVICE;
import static com.meekmika.warsart.ui.DetailActivity.STREET_ART;

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    private static final int PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE = 818;
    private static final float ZOOM_LEVEL = 14f;
    private static final long LOCATION_REFRESH_TIME = 1200000; //20 min
    private static final float LOCATION_REFRESH_DISTANCE = 100; //100 meters
    private StreetArt streetArt;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            LatLng streetArtLocation = new LatLng(streetArt.getLat(), streetArt.getLng());
            LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
            new GetDistanceTask().execute(new Pair<>(origin, streetArtLocation));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private TextView distanceTextView;

    public static DetailFragment newInstance(StreetArt streetArt) {

        Bundle args = new Bundle();
        args.putParcelable(STREET_ART, streetArt);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        streetArt = args.getParcelable(STREET_ART);

        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ViewPager viewPager = rootView.findViewById(R.id.image_view_pager);
        FrameLayout pageIndicatorContainer = rootView.findViewById(R.id.page_indicator_container);
        TextView titleTextView = rootView.findViewById(R.id.tv_street_art_title);
        TextView addressTextView = rootView.findViewById(R.id.tv_street_art_address);
        TextView artistTextView = rootView.findViewById(R.id.tv_street_art_artist);
        TextView descriptionTextView = rootView.findViewById(R.id.tv_street_art_description);
        distanceTextView = rootView.findViewById(R.id.tv_street_art_distance);
        FixedAspectRatioFrameLayout mapContainer = rootView.findViewById(R.id.map_container);

        if (streetArt != null) {
            viewPager.setAdapter(new ImagePagerAdapter(streetArt.getImages()));
            viewPager.setContentDescription(getString(R.string.a11y_images));

            String title = streetArt.getTitle();
            if (title == null || title.isEmpty()) title = getString(R.string.untitled);
            titleTextView.setText(title);
            titleTextView.setContentDescription(getString(R.string.a11y_title, title));

            String artist = streetArt.getArtist();
            if (artist == null || artist.isEmpty()) artist = getString(R.string.unknown);
            artistTextView.setText(getString(R.string.created_by, artist));
            artistTextView.setContentDescription(getString(R.string.a11y_created_by, artist));

            String description = streetArt.getDescription();
            if (description == null || description.isEmpty())
                descriptionTextView.setVisibility(View.GONE);
            descriptionTextView.setText(description);
            descriptionTextView.setContentDescription(getString(R.string.a11y_about, description));

            String address = streetArt.getAddress();
            if (streetArt.getAddress() == null || address.isEmpty())
                address = getString(R.string.unknown_location);
            addressTextView.setText(address);
            addressTextView.setContentDescription(getString(R.string.a11y_address, address));

            if (streetArt.getImages().size() < 2) {
                pageIndicatorContainer.setVisibility(View.GONE);
            }
            mapContainer.setAspectRatio(9 / 16f);
        }

        ImageButton openMapsButton = rootView.findViewById(R.id.btn_open_maps);
        openMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri location = Uri.parse("geo:0,0?q=" + streetArt.getAddress());
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        location);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setupPermissions();
        LatLng position = new LatLng(streetArt.getLat(), streetArt.getLng());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_LEVEL));
        googleMap.addMarker(new MarkerOptions().position(position));
    }

    private void setupPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(locationPermission, PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE);
            }
        } else {
            googleMap.setMyLocationEnabled(true);
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                            LOCATION_REFRESH_DISTANCE, locationListener);
                }
            }
        }
    }

    public class GetDistanceTask extends AsyncTask<Pair, Void, String[]> {
        @Override
        protected String[] doInBackground(Pair... params) {
            if (params.length == 0) {
                return null;
            }

            LatLng origin = (LatLng) params[0].first;
            LatLng destination = (LatLng) params[0].second;

            if (origin == null || destination == null) {
                return null;
            }

            try {
                URL destinationUrl = NetworkUtils.buildUrl(origin, destination);
                return NetworkUtils.getDestinationStringsFromJson(NetworkUtils.getResponseFromHttpUrl(destinationUrl));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] response) {
            if (response != null && getContext() != null) {
                distanceTextView.setText(getString(R.string.distance, response[0], response[1]));
                distanceTextView.setVisibility(View.VISIBLE);
                distanceTextView.setContentDescription(getString(R.string.a11y_distance, response[0], response[1]));
            }
        }
    }
}
