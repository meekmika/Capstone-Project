package com.meekmika.warsart.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meekmika.warsart.R;
import com.meekmika.warsart.data.model.StreetArt;

import java.util.List;

public class StreetArtAdapter extends RecyclerView.Adapter<StreetArtAdapter.StreetArtViewHolder> {

    private List<StreetArt> streetArtData;

    @NonNull
    @Override
    public StreetArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.street_art_list_item, parent, false);
        return new StreetArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreetArtViewHolder holder, int position) {
        String title = streetArtData.get(position).getTitle();
        holder.streetArtTitle.setText(title);
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

    class StreetArtViewHolder extends RecyclerView.ViewHolder {

        final TextView streetArtTitle;
        final TextView streetArtAddress;

        StreetArtViewHolder(View view) {
            super(view);
            streetArtTitle = view.findViewById(R.id.tv_street_art_title);
            streetArtAddress = view.findViewById(R.id.tv_street_art_address);
        }
    }
}
