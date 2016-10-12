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
    private SparseArray<Integer> mSection;
    private List<String> mDates;
    private Observer mObserver;


    private DailySelectionModel() {
        mDatas = new ArrayList<>();
        mVideoDatas = new ArrayList<>();
        mMap = new SparseArray<>();
        mSection=new SparseArray<>();
        mDates=new ArrayList<>();
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


    public void setObserver(Observer observer) {
        mObserver = observer;
    }


    public int getSize() {
        return mDatas.size();
    }


    public SparseArray<Integer> getMap() {
        return mMap;
    }


    public void addDate(String date){
        if (mDates.size() == 0) {
            mDates.add("Today");
        } else {
            mDates.add(date);
        }

    }


    public void clear() {
        mSection.clear();
        mMap.clear();
        mDatas.clear();
        mVideoDatas.clear();
        sModel = null;
    }


    public void addSection(int value){
        mSection.put(mSection.size(),value);
    }


    public void addVideoData(ViewData data) {
        mVideoDatas.add(data);
        if (mObserver != null) {
            mObserver.notifyDataSetAdd();
        }
    }


    public List<String> getDates() {
        return mDates;
    }


    public SparseArray<Integer> getSection(){
        return mSection;
    }


    @Override
    public List<ViewData> getVideoList(int videoID, int parentIndex) {
        return mVideoDatas;
    }


    public interface Observer {
        void notifyDataSetAdd();
    }
}
