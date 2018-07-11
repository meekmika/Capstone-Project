package com.meekmika.warsart.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meekmika.warsart.R;

public class BottomNavigation extends LinearLayout {
    public static final String SHOW_MAP = "show-map";
    public static final String SHOW_LIST = "show-list";

    private BottomNavigationOnClickListener mBottomNavigationOnClickListener;

    public BottomNavigation(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.bottom_navigation, this, true);
        TextView showMapButton = view.findViewById(R.id.btn_show_map);
        TextView showListButton = view.findViewById(R.id.btn_show_list);

        showMapButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomNavigationOnClickListener != null)
                    mBottomNavigationOnClickListener.onButtonClicked(SHOW_MAP);
            }
        });
        showListButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomNavigationOnClickListener != null)
                    mBottomNavigationOnClickListener.onButtonClicked(SHOW_LIST);
            }
        });
    }

    public void setBottomNavigationOnClickListener(BottomNavigationOnClickListener mBottomNavigationOnClickListener) {
        this.mBottomNavigationOnClickListener = mBottomNavigationOnClickListener;
    }

    public interface BottomNavigationOnClickListener {
        void onButtonClicked(String buttonClicked);
    }
}
