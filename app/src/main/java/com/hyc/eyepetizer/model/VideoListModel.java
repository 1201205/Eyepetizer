package com.hyc.eyepetizer.model;

import android.util.SparseArray;
import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.List;

/**
 * Created by ray on 16/9/6.
 */
public class VideoListModel implements VideoListInterface {
    private static VideoListModel sModel;
    private SparseArray<List<ViewData>> mList;


    private VideoListModel() {
        mList = new SparseArray<>();
    }


    public static VideoListModel getInstance() {

        if (sModel == null) {
            synchronized (VideoListModel.class) {
                if (sModel == null) {
                    sModel = new VideoListModel();
                }
            }
        }
        return sModel;
    }


    public void clear() {
        mList.clear();
    }


    public void addVideoList(int id, List<ViewData> data) {
        mList.put(id, data);
    }


    @Override
    public List<ViewData> getVideoList(int videoID, int parentIndex, SparseArray<Integer> array) {
        if (mList.get(videoID) != null) {
            return mList.get(videoID);
        }
        return null;
    }


    public ViewData getVideo(int videoID, int position) {
        if (mList.get(videoID) != null) {
            return mList.get(videoID).get(position);
        }
        return null;
    }
}
