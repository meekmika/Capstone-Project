package com.meekmika.warsart.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.meekmika.warsart.R;
import com.meekmika.warsart.data.model.StreetArt;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link StreetArtWidgetConfigureActivity StreetArtWidgetConfigureActivity}
 */
public class StreetArtWidget extends AppWidgetProvider {

    private static FirebaseStorage storage = FirebaseStorage.getInstance();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        StreetArt streetArt = StreetArtWidgetConfigureActivity.loadStreetArtPref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.street_art_widget);
        if (streetArt != null) {
            views.setTextViewText(R.id.tv_street_art_title, streetArt.getTitle());
            views.setTextViewText(R.id.tv_street_art_address, streetArt.getAddress());

            AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, views, R.id.iv_street_art, appWidgetId);

            StorageReference storageReference;
            try {
                String imageUrl = streetArt.getImages().get(0);
                storageReference = storage.getReferenceFromUrl(imageUrl);
                Glide.with(context.getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .asBitmap()
                        .into(appWidgetTarget);
            } catch (Exception e) {
                Timber.e("Could not load image for street art list item.");
                e.printStackTrace();
            }
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            StreetArtWidgetConfigureActivity.deleteStreetArtPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

