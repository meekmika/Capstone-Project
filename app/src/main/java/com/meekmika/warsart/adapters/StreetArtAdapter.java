package com.meekmika.warsart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meekmika.warsart.R;

import java.util.ArrayList;

public class StreetArtAdapter extends RecyclerView.Adapter<StreetArtAdapter.StreetArtViewHolder> {

    private Context mContext;
    private ArrayList mStreetArtData;

    public StreetArtAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public StreetArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.street_art_list_item, parent, false);
        return new StreetArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreetArtViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mStreetArtData == null) return 0;
        return mStreetArtData.size();
    }

    public void setStreetArtData(ArrayList newStreetArtData) {
        mStreetArtData = newStreetArtData;
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
