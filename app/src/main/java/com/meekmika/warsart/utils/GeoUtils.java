package com.meekmika.warsart.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import timber.log.Timber;

public class GeoUtils {

    public static LatLng getCoordinates(Context context, String addressString) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocationName(addressString, 5);
            Address location = addresses.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            Timber.e("Could not get coordinates for address %s", addressString);
            e.printStackTrace();
            return null;
        }
    }
}
