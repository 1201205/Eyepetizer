package com.hyc.eyepetizer.widget;

import android.support.v4.view.ViewPager;
import android.view.View;


/**
 * Created by Administrator on 2016/9/6.
 */
public class ScrollPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) {
            view.scrollTo(0, 0);
        } else if (position > 1) {
            view.scrollTo(0, 0);
        } else {
            view.scrollTo((int) (0.6f * position * pageWidth), 0);
        }
    }

}
