package com.hyc.eyepetizer.view.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.base.BaseFragment;
import com.hyc.eyepetizer.contract.DiscoveryContract;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.presenter.DiscoveryPresenter;
import com.hyc.eyepetizer.utils.AppUtil;
import com.hyc.eyepetizer.utils.WidgetHelper;
import com.hyc.eyepetizer.view.adapter.ViewAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class DiscoveryFragment extends BaseFragment<DiscoveryPresenter> implements DiscoveryContract.View {
    @BindView(R.id.rv_discovery)
    RecyclerView mRvDiscovery;
    private Unbinder mUnbinder;
    private SparseArray<Integer> mPositionSparseArray;


    @Override
    protected void initPresenter() {
        mPresenter = new DiscoveryPresenter(this);
        mPresenter.attachView();
        mPresenter.getDiscovery();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }
    private ViewAdapter mViewAdapter;

    @Override
    public void showDiscovery(final List<ViewData> viewDatas) {
        mPositionSparseArray=new SparseArray<>();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (WidgetHelper.Type.SQUARE_CARD.equals(viewDatas.get(position).getType())) {
                    return  1;
                }
                if (mPositionSparseArray.indexOfValue(position) == -1) {
                    mPositionSparseArray.put(mPositionSparseArray.size(), position);
                }
                return 2;
            }
        });
        mViewAdapter=new ViewAdapter.Builder(getContext(),viewDatas).build();
        mRvDiscovery.setLayoutManager(layoutManager);
        mRvDiscovery.setAdapter(mViewAdapter);
        mRvDiscovery.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int itemPosition =
                        ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
                if (itemPosition!=0) {
                    outRect.top= (int) AppUtil.dip2px(2);
                }
                if (WidgetHelper.Type.SQUARE_CARD.equals(viewDatas.get(itemPosition).getType())) {
                    if (isTheOdd(itemPosition)) {
                        outRect.right = (int) AppUtil.dip2px(1);
                    } else {
                        outRect.left = (int) AppUtil.dip2px(1);
                    }
                }
            }
        });
    }

    /**
     * 根据位置判断当前position是否处于奇数
     * 也就是当前position减去前一条一排只有一个的位置
     * @param itemPosition
     * @return
     */
    private boolean isTheOdd(int itemPosition) {
        int last=0;
        for (int i = mPositionSparseArray.size() - 1; i >= 0; i--) {
            if (mPositionSparseArray.get(i) < itemPosition) {
                last = mPositionSparseArray.get(i);
                break;
            }
        }
        return (itemPosition - last) % 2 == 1;
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
