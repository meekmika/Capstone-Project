package com.meekmika.warsart.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meekmika.warsart.R;
import com.meekmika.warsart.data.StreetArtViewModel;
import com.meekmika.warsart.data.model.StreetArt;
import com.meekmika.warsart.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The configuration screen for the {@link StreetArtWidget StreetArtWidget} AppWidget.
 */
public class StreetArtWidgetConfigureActivity extends AppCompatActivity implements StreetArtWidgetAdapter.StreetArtWidgetAdapterOnClickHandler {

    private static final String PREFS_NAME = "com.meekmika.warsart.widget.StreetArtWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private HashMap<String, StreetArt> streetArtHashMap;
    private StreetArtWidgetAdapter adapter;

    public StreetArtWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveStreetArtPref(Context context, int appWidgetId, StreetArt streetArt) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();

        Gson gson = new Gson();
        String streetArtJson = gson.toJson(streetArt);

        prefs.putString(PREF_PREFIX_KEY + appWidgetId, streetArtJson);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static StreetArt loadStreetArtPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String streetArtJson = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (streetArtJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(streetArtJson, StreetArt.class);
        } else {
            return null;
        }
    }

    static void deleteStreetArtPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.street_art_widget_configure);

        final ImageView placeholder = findViewById(R.id.placeholder);

        if (!NetworkUtils.isOnline(this)) {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }

        RecyclerView recyclerView = findViewById(R.id.rv_street_art_list);
        adapter = new StreetArtWidgetAdapter();
        adapter.setOnClickHandler(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        StreetArtViewModel viewModel = ViewModelProviders.of(this).get(StreetArtViewModel.class);
        LiveData<HashMap<String, StreetArt>> liveData = viewModel.getStreetArtLiveData();
        liveData.observe(this, new Observer<HashMap<String, StreetArt>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, StreetArt> streetArtHashMap) {
                if (streetArtHashMap != null) {
                    StreetArtWidgetConfigureActivity.this.streetArtHashMap = streetArtHashMap;
                    adapter.setStreetArtData(new ArrayList<>(streetArtHashMap.values()));
                    if (adapter.getItemCount() != 0) placeholder.setVisibility(View.GONE);
                    else placeholder.setVisibility(View.VISIBLE);
                }
            }
        });

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    @Override
    public void onClick(String streetArtId) {
        final Context context = StreetArtWidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
        StreetArt streetArt = streetArtHashMap.get(streetArtId);
        saveStreetArtPref(context, appWidgetId, streetArt);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        StreetArtWidget.updateAppWidget(context, appWidgetManager, appWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

