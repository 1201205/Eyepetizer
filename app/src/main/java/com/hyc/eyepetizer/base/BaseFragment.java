package com.hyc.eyepetizer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by ray on 16/8/31.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    protected T mPresenter;


    protected abstract void initPresenter();


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }
}
