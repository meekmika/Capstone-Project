package com.meekmika.warsart.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.meekmika.warsart.R;
import com.meekmika.warsart.data.model.StreetArt;

import java.util.List;

import timber.log.Timber;

public class StreetArtAdapter extends RecyclerView.Adapter<StreetArtAdapter.StreetArtViewHolder> {

    private List<StreetArt> streetArtData;
    private StreetArtAdapterOnClickHandler clickHandler;
    private FirebaseStorage storage;

    public StreetArtAdapter() {
        storage = FirebaseStorage.getInstance();
    }


    @NonNull
    @Override
    public StreetArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.street_art_list_item, parent, false);
        return new StreetArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreetArtViewHolder holder, int position) {
        StreetArt streetArt = streetArtData.get(position);
        String title = streetArt.getTitle();
        String address = streetArt.getAddress();

        holder.streetArtTitle.setText(title);
        holder.streetArtAddress.setText(address);

        StorageReference storageReference;
        try {
            String imageUrl = streetArt.getImages().get(0);
            storageReference = storage.getReferenceFromUrl(imageUrl);
            Glide.with(holder.streetArtPreview.getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(holder.streetArtPreview);
        } catch (Exception e) {
            Timber.e("Could not load image for street art list item.");
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (streetArtData == null) return 0;
        return streetArtData.size();
    }

    public void setStreetArtData(List<StreetArt> newStreetArtData) {
        streetArtData = newStreetArtData;
        notifyDataSetChanged();
    }

    public void setOnClickHandler(StreetArtAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public interface StreetArtAdapterOnClickHandler {
        void onClick(int streetArtIndex);
    }

    class StreetArtViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView streetArtPreview;
        final TextView streetArtTitle;
        final TextView streetArtAddress;

        StreetArtViewHolder(View view) {
            super(view);
            streetArtPreview = view.findViewById(R.id.iv_street_art);
            streetArtTitle = view.findViewById(R.id.tv_street_art_title);
            streetArtAddress = view.findViewById(R.id.tv_street_art_address);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickHandler != null) clickHandler.onClick(getAdapterPosition());
        }
    }
}
