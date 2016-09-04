package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by ray on 16/9/4.
 */
public class VideoRecyclerView extends RecyclerView {
    private int mCurrentAnimPosition;


    public VideoRecyclerView(Context context) {
        this(context, null);
    }


    public VideoRecyclerView(Context context,
                             @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public VideoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);

    }


    public void setmCurrentAnimPosition(int currentAnimPosition) {
        this.mCurrentAnimPosition = currentAnimPosition;
    }


    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        Log.e("hyc666", i + "----");
        int position = mCurrentAnimPosition -
            ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        if (position < 0 || position > childCount) {
            return i;
        }
        if (i == childCount - 1) {// 这是最后一个需要刷新的item
            return position;
        }
        if (i == position) {// 这是原本要在最后一个刷新的item
            return childCount - 1;
        }
        return i;// 正常次序的item
    }
}
