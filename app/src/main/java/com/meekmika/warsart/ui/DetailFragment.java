package com.meekmika.warsart.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meekmika.warsart.R;
import com.meekmika.warsart.adapters.ImagePagerAdapter;
import com.meekmika.warsart.data.model.StreetArt;

import static com.meekmika.warsart.ui.DetailActivity.STREET_ART;

public class DetailFragment extends Fragment {

    private StreetArt streetArt;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ViewPager viewPager = rootView.findViewById(R.id.image_view_pager);
        TextView addressTextView = rootView.findViewById(R.id.tv_street_art_address);
        TextView artistTextView = rootView.findViewById(R.id.tv_street_art_artist);
        TextView descriptionTextView = rootView.findViewById(R.id.tv_street_art_description);

        if (streetArt != null) {
            viewPager.setAdapter(new ImagePagerAdapter(streetArt.getImages()));
            addressTextView.setText(streetArt.getAddress());
            artistTextView.setText(streetArt.getArtist());
            descriptionTextView.setText(streetArt.getDescription());
        }

        return rootView;
    }
}
