package com.hyc.eyepetizer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyc.eyepetizer.beans.Selection;
import com.hyc.eyepetizer.beans.ViewData;
import com.hyc.eyepetizer.net.Requests;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/30.
 */
public class TestFragment extends Fragment{
    @BindView(R.id.target)
    RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_main,container,false);
        ButterKnife.bind(this,root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        Requests.getApi().getSelection().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Selection>() {
            @Override
            public void call(Selection selection) {
                Log.e("selection",System.currentTimeMillis()+"");
                List<ViewData> datas=new ArrayList<ViewData>();
                int i=selection.getSectionList().size();
                for (int j=0;j<i;j++) {
                    datas.addAll(selection.getSectionList().get(j).getItemList());
                }
                mRecyclerView.setLayoutManager(manager);
                mRecyclerView.setAdapter(new TestAdapter(getContext(),datas));
                Log.e("selection",System.currentTimeMillis()+"");
            }
        });
    }
}
