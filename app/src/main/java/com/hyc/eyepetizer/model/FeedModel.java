package com.hyc.eyepetizer.model;

import android.util.SparseArray;

import com.hyc.eyepetizer.model.beans.SectionList;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.utils.WidgetHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * 想法：
 * 对外提供Feed的数据
 * 让presenter直接取用
 * Activity之间跳转传递引用，而不是序列化的对象
 * Created by Administrator on 2016/9/2.
 */
public class FeedModel {

    private static FeedModel sModel;
    private List<SectionList> mSectionLists;
    private SparseArray<List<ViewData>> mViewDatas;


    private FeedModel() {
        mSectionLists = new ArrayList<>();
        mViewDatas=new SparseArray<>();
    }


    public static FeedModel getInstance() {

        if (sModel == null) {
            synchronized (FeedModel.class) {
                if (sModel == null) {
                    sModel = new FeedModel();
                }
            }
        }
        return sModel;
    }


    public void addSection(List<SectionList> datas) {
        mSectionLists.addAll(datas);
    }

    public void addSection(SectionList data) {
        mSectionLists.add(data);
    }

    public void clear() {
        mSectionLists.clear();
    }

    public List<ViewData> getVideoListByIndex(int index, SparseArray<Integer> array) {
        if (mSectionLists.size() < index) {
            return null;
        }
        List<ViewData> datas = new ArrayList<>();
        int count=mSectionLists.get(index-1).getItemList().size();
        int j=0;
        for (int i=0;i<count;i++) {
            ViewData data=mSectionLists.get(index-1).getItemList().get(i);
            if (WidgetHelper.Type.VIDEO.equals(data.getType())) {
                datas.add(data);
                array.put(j,i);
                j++;
            }
        }
        if (mViewDatas.get(index)!=null) {
            mViewDatas.get(index).clear();
        }
        mViewDatas.put(index,datas);
        return datas;
    }
    public List<ViewData> getVideoListByIndex(int index){
        if (mViewDatas.get(index)==null) {
            return getVideoListByIndex(index,new SparseArray<Integer>());
        }
        return mViewDatas.get(index);
    }

}
