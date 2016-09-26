package com.asha.vrlib.plugins;

import android.content.Context;

/**
 * Created by hzqiujiadi on 16/8/7.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class MDAbsLinePipe {
    private boolean mIsInit;


    abstract public void takeOver(int totalWidth, int totalHeight, int size);

    abstract public void commit(int totalWidth, int totalHeight, int size);

    abstract protected void init(Context context);

    // MDPosition position = MDPosition.sOriginalPosition;


    public final void setup(Context context) {
        if (!mIsInit) {
            init(context);
            mIsInit = true;
        }
    }

}
