package com.hyc.eyepetizer.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ray on 16/8/31.
 */
public class HorizontalDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;


    public HorizontalDecoration(int space) {
        this.mSpace = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //super.getItemOffsets(outRect, view, parent, state);
        int itemPosition =
            ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        int totalItemCount = parent.getAdapter().getItemCount();
        if (itemPosition == totalItemCount - 1) {
            outRect.set(mSpace, mSpace, mSpace, mSpace);
        } else {
            outRect.set(mSpace, mSpace, 0, mSpace);
        }

    }

}
