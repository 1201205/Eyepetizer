package com.hyc.eyepetizer.model;

import android.util.SparseArray;
import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.List;

/**
 * Created by ray on 16/9/6.
 */
public class VideoRelateModel implements VideoListInterface {
    private SparseArray<List<ViewData>> mViewDatas;


    public VideoRelateModel() {
        mViewDatas = new SparseArray<>();
    }


    public void addData(int index, List<ViewData> datas) {
        mViewDatas.put(index, datas);
    }


    public void clear() {
        mViewDatas.clear();
    }


    @Override public List<ViewData> getVideoListByIndex(int index) {
        return mViewDatas.get(index);
    }
}
