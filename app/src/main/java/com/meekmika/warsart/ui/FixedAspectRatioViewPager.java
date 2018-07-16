package com.meekmika.warsart.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class FixedAspectRatioViewPager extends ViewPager {

    private static final float ASPECT_RATIO = 3/4f;

    public FixedAspectRatioViewPager(@NonNull Context context) {
        super(context);
    }

    public FixedAspectRatioViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float height = MeasureSpec.getSize(widthMeasureSpec) * ASPECT_RATIO;
        int heightSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
