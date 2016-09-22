package com.hyc.eyepetizer.model;

import android.util.Log;
import android.util.SparseArray;

import com.hyc.eyepetizer.model.beans.ViewData;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class SectionModel implements VideoListInterface {
    private static SectionModel sModel;
    private SparseArray<List<ViewData>> mSection;
    public static SectionModel getInstance() {

        if (sModel == null) {
            synchronized (SectionModel.class) {
                if (sModel == null) {
                    sModel = new SectionModel();
                }
            }
        }
        return sModel;
    }
    private SectionModel(){
        mSection=new SparseArray<>();
    }
    public void putList(int key,List<ViewData> values){
        Log.e("datgetTypea",key+"-----"+values.size());
        mSection.put(key,values);
    }

    @Override
    public List<ViewData> getVideoList(int videoID, int parentIndex, SparseArray<Integer> array) {
        if (mSection.get(parentIndex)!=null) {
            return mSection.get(parentIndex);
        }
        return null;
    }
}
