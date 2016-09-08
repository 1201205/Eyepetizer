package com.hyc.eyepetizer.model;

import android.util.SparseArray;
import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.List;

/**
 * Created by ray on 16/9/6.
 */
public class VideoRelateModel implements VideoListInterface {
    private static VideoRelateModel sModel;
    private SparseArray<List<ViewData>> mViewDatas;
    private SparseArray<SparseArray<List<ViewData>>> mRelate;


    private VideoRelateModel() {
        mViewDatas = new SparseArray<>();
        mRelate=new SparseArray<>();
    }


    public void addData(int index, List<ViewData> datas) {
        mViewDatas.put(index, datas);
    }


    public void clear() {
        mViewDatas.clear();
    }
    public void addRelate(int id, SparseArray<List<ViewData>> data) {
        mRelate.put(id, data);
    }

    @Override public List<ViewData> getVideoListByIndex(int index) {
        return mViewDatas.get(index);
    }
    public static VideoRelateModel getInstance() {

        if (sModel == null) {
            synchronized (VideoRelateModel.class) {
                if (sModel == null) {
                    sModel = new VideoRelateModel();
                }
            }
        }
        return sModel;
    }
}
