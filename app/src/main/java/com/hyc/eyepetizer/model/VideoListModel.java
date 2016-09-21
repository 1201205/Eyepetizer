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
    private Observer mObserver;

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


    public void setObserver(VideoListModel.Observer observer){
        mObserver=observer;
    }


    public void clear(int type) {
        if (mList.get(type) != null) {
            mList.get(type).clear();
        }
    }


    public void addVideoList(int id, List<ViewData> data) {
        mList.put(id, data);
    }
    public void addMore(int id, List<ViewData> data) {
        if (mList.get(id)!=null) {
            mList.get(id).addAll(data);
            if (mObserver!=null) {
                mObserver.notifyDataChanged();
            }
        }
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
    public interface Observer{
        void notifyDataChanged();
    }
}
