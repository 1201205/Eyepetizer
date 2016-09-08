package com.hyc.eyepetizer.model;

import android.util.SparseArray;
import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ray on 16/9/6.
 */
public class DailySelectionModel implements VideoListInterface {
    private static DailySelectionModel sModel;
    private List<ViewData> mDatas;
    private List<ViewData> mVideoDatas;
    private SparseArray<Integer> mMap;


    private DailySelectionModel() {
        mDatas = new ArrayList<>();
        mVideoDatas = new ArrayList<>();
        mMap = new SparseArray<>();
    }


    public static DailySelectionModel getInstance() {

        if (sModel == null) {
            synchronized (DailySelectionModel.class) {
                if (sModel == null) {
                    sModel = new DailySelectionModel();
                }
            }
        }
        return sModel;
    }


    public void addData(List<ViewData> datas) {
        mDatas.addAll(datas);
    }


    public int getSize() {
        return mDatas.size();
    }


    public SparseArray<Integer> getMap() {
        return mMap;
    }


    public void clear() {
        mMap.clear();
        mDatas.clear();
        mVideoDatas.clear();
        sModel = null;
    }


    public void addVideoData(ViewData data) {
        mVideoDatas.add(data);
    }


    @Override
    public List<ViewData> getVideoList(int videoID, int parentIndex, SparseArray<Integer> array) {
        array = mMap;
        return mVideoDatas;
    }
}
