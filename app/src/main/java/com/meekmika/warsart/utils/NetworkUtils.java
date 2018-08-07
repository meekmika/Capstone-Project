package com.meekmika.warsart.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.meekmika.warsart.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class NetworkUtils {
    private static final String DISTANCE_MATRIX_BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String KEY_PARAM = "key";
    private static final String ORIGINS_PARAM = "origins";
    private static final String DESTINATIONS_PARAM = "destinations";
    private static final String MODE_PARAM = "mode";
    private static final String LANGUAGE_PARAM = "language";

    private static final String key = BuildConfig.GOOGLE_API_KEY;
    private static final String mode = "walking";
    private static final String language = Locale.getDefault().getLanguage();

    public static URL buildUrl(LatLng origin, LatLng destination) {
        String originString = new StringBuilder()
                .append(String.valueOf(origin.latitude))
                .append(String.valueOf(","))
                .append(String.valueOf(origin.longitude))
                .toString();
        String destinationString = new StringBuilder()
                .append(String.valueOf(destination.latitude))
                .append(String.valueOf(","))
                .append(String.valueOf(destination.longitude))
                .toString();
        Uri builtUri = Uri.parse(DISTANCE_MATRIX_BASE_URL).buildUpon()
                .appendQueryParameter(ORIGINS_PARAM, originString)
                .appendQueryParameter(DESTINATIONS_PARAM, destinationString)
                .appendQueryParameter(KEY_PARAM, key)
                .appendQueryParameter(MODE_PARAM, mode)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     *                     <p>
     *                     Copied from the Sunshine weather app!
     *                     https://github.com/udacity/ud851-Sunshine/blob/student/S02.01-Solution-Networking/app/src/main/java/com/example/android/sunshine/utilities/NetworkUtils.java
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String response = scanner.next();
                return response;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String[] getDestinationStringsFromJson(String responseJsonString) throws JSONException {
        final String DESTINATION_ADDRESSES = "destination_addresses";
        final String ORIGIN_ADDRESSES = "origin_addresses";
        final String ROWS = "rows";
        final String ELEMENTS = "elements";
        final String DISTANCE = "distance";
        final String DURATION = "duration";
        final String TEXT = "text";
        final String STATUS = "status";

        final String STATUS_CODE_OK = "OK";

        String[] parsedDestinationData;

        JSONObject responseJson = new JSONObject(responseJsonString);

        if (responseJson.has(STATUS)) {
            String code = responseJson.getString(STATUS);

            switch (code) {
                case STATUS_CODE_OK:
                    break;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray rowsArray = responseJson.getJSONArray(ROWS);

        parsedDestinationData = new String[4];

        JSONObject destinationData = rowsArray.getJSONObject(0);

        JSONObject elements = destinationData.getJSONArray(ELEMENTS).getJSONObject(0);

        String dist = elements.getJSONObject(DISTANCE).getString(TEXT);
        String duration = elements.getJSONObject(DURATION).getString(TEXT);
        String destination = responseJson.getJSONArray(DESTINATION_ADDRESSES).getString(0);
        String origin = responseJson.getJSONArray(ORIGIN_ADDRESSES).getString(0);

        parsedDestinationData[0] = dist;
        parsedDestinationData[1] = duration;
        parsedDestinationData[2] = origin;
        parsedDestinationData[3] = destination;

        return parsedDestinationData;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
