package com.hyc.eyepetizer.model;

import android.util.SparseArray;
import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.List;

/**
 * Created by ray on 16/9/6.
 */
public class VideoRelateModel implements VideoListInterface {
    private static VideoRelateModel sModel;
    private SparseArray<SparseArray<List<ViewData>>> mRelate;


    private VideoRelateModel() {
        mRelate=new SparseArray<>();
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


    public void clear() {
        mRelate.clear();
    }


    public void addRelate(int id, SparseArray<List<ViewData>> data) {
        mRelate.put(id, data);
    }


    @Override
    public List<ViewData> getVideoList(int videoID, int parentIndex, SparseArray<Integer> array) {
        if (mRelate.get(videoID) != null) {
            return mRelate.get(videoID).get(parentIndex);
        }
        return null;
    }
}
