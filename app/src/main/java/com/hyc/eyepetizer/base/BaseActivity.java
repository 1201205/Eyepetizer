package com.hyc.eyepetizer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hyc on 2016/5/13.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity
    implements BaseView {
    protected T mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        if (getIntent() != null) {
            handleIntent();
        }
    }


    protected abstract void handleIntent();

    protected abstract int getLayoutID();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
