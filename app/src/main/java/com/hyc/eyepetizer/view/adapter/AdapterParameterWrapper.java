package com.hyc.eyepetizer.view.adapter;

import android.util.Log;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.AppUtil;

/**
 * Created by Administrator on 2016/10/14.
 */
public class AdapterParameterWrapper {
    private int color = AppUtil.getColor(R.color.title_black);
    private int type;
    private ViewAdapter.HorizontalItemClickListener listener;
    private boolean fromRank;

    public boolean isFromRank() {
        return fromRank;
    }

    public void setFromRank(boolean fromRank) {
        this.fromRank = fromRank;
    }

    public ViewAdapter.HorizontalItemClickListener getListener() {
        return listener;
    }

    public void setListener(ViewAdapter.HorizontalItemClickListener listener) {
        this.listener = listener;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
