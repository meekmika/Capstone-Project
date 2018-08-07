package com.meekmika.warsart.adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.meekmika.warsart.R;

import java.util.List;

import timber.log.Timber;

public class ImagePagerAdapter extends PagerAdapter {

    private FirebaseStorage storage;
    private List<String> images;

    public ImagePagerAdapter(List<String> images) {
        this.images = images;
        storage = FirebaseStorage.getInstance();
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View itemView = inflater.inflate(R.layout.image_pager_item, container, false);

        ImageView image = itemView.findViewById(R.id.image);
        StorageReference storageReference;
        try {
            String imageUrl = images.get(position);
            storageReference = storage.getReferenceFromUrl(imageUrl);
            Glide.with(itemView.getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(image);
        } catch (Exception e) {
            Timber.e("Could not load image for ViewPager.");
            e.printStackTrace();
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
