package com.hyc.eyepetizer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;

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
        ButterKnife.bind(this);
        if (getIntent() != null) {
            handleIntent();
        }
        initPresenterAndData();
        if (mPresenter!=null) {
            mPresenter.attachView();
        }
        initView();
    }


    protected abstract void handleIntent();

    protected abstract int getLayoutID();

    protected abstract void initPresenterAndData();

    protected abstract void initView();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

//    protected void setShareElementTransition(){
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setSharedElementEnterTransition(
//                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
//                    ScalingUtils.ScaleType.CENTER_CROP));
//            getWindow().setSharedElementReturnTransition(
//                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
//                    ScalingUtils.ScaleType.CENTER_CROP));
//        }
//    }
}
