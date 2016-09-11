package com.hyc.eyepetizer.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public class FragmentAdapter<T extends Fragment> extends FragmentStatePagerAdapter {
    private List<T> mFragments;


    public FragmentAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        mFragments=fragments;
    }

    @Override
    public T getItem(int position) {
        return mFragments.get(position);
    }
    @Override
    public int getCount() {
        return mFragments==null?0:mFragments.size();
    }
}
