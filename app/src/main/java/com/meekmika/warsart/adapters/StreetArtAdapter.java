package com.meekmika.warsart.adapters;

import android.content.Context;
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
import com.meekmika.warsart.ui.FavoriteButton;
import com.meekmika.warsart.utils.SharedPrefsUtils;

import java.util.List;

import timber.log.Timber;

public class StreetArtAdapter extends RecyclerView.Adapter<StreetArtAdapter.StreetArtViewHolder> {

    private List<StreetArt> streetArtData;
    private StreetArtAdapterOnClickHandler clickHandler;
    private FirebaseStorage storage;
    private Context context;

    public StreetArtAdapter() {
        storage = FirebaseStorage.getInstance();
    }


    @NonNull
    @Override
    public StreetArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.street_art_list_item, parent, false);
        return new StreetArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreetArtViewHolder holder, int position) {
        StreetArt streetArt = streetArtData.get(position);
        String id = streetArt.getId();

        String title = streetArt.getTitle();
        if (title == null || title.isEmpty()) title = context.getString(R.string.untitled);
        holder.streetArtTitle.setText(title);
        holder.streetArtTitle.setContentDescription(context.getString(R.string.a11y_title, title));

        String address = streetArt.getAddress();
        if (address == null || address.isEmpty())
            address = context.getString(R.string.unknown_location);
        holder.streetArtAddress.setText(address);
        holder.streetArtAddress.setContentDescription(context.getString(R.string.a11y_address, address));

        holder.favoriteButton.setStreetArtId(id);
        holder.favoriteButton.setChecked(SharedPrefsUtils.isFavorite(holder.itemView.getContext(), id));
        holder.favoriteButton.setContentDescription(context.getString(R.string.a11y_toggle_favorite));

        StorageReference storageReference;
        try {
            String imageUrl = streetArt.getImages().get(0);
            storageReference = storage.getReferenceFromUrl(imageUrl);
            Glide.with(holder.streetArtPreview.getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
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
        void onClick(String streetArtId);
    }

    class StreetArtViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final FavoriteButton favoriteButton;
        final ImageView streetArtPreview;
        final TextView streetArtTitle;
        final TextView streetArtAddress;

        StreetArtViewHolder(View view) {
            super(view);
            favoriteButton = view.findViewById(R.id.btn_favorite);
            streetArtPreview = view.findViewById(R.id.iv_street_art);
            streetArtTitle = view.findViewById(R.id.tv_street_art_title);
            streetArtAddress = view.findViewById(R.id.tv_street_art_address);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickHandler != null)
                clickHandler.onClick(streetArtData.get(getAdapterPosition()).getId());
        }
    }
}
