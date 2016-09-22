package com.hyc.eyepetizer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseFragment;
import com.hyc.eyepetizer.contract.DiscoveryContract;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.DiscoveryPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/9/22.
 */
public class DiscoveryFragment extends BaseFragment<DiscoveryPresenter> implements DiscoveryContract.View {
    @BindView(R.id.rv_discovery)
    RecyclerView mRvDiscovery;
    private Unbinder mUnbinder;

    @Override
    protected void initPresenter() {
        mPresenter = new DiscoveryPresenter(this);
        mPresenter.getDiscovery();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void showDiscovery(List<ViewData> viewDatas) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mUnbinder.unbind();
    }
}
