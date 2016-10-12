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
public class FeedModel implements VideoListInterface {

    private static FeedModel sModel;
    private SparseArray<SectionList> mSectionListSparseArray;
    private SparseArray<List<ViewData>> mViewDatas;
    private SparseArray<SparseArray<List<ViewData>>> mRelate;

    private FeedModel() {
        mViewDatas=new SparseArray<>();
        mRelate = new SparseArray<>();
        mSectionListSparseArray=new SparseArray<>();
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


    public void addViewDatas(int key, List<ViewData> datas) {
        mViewDatas.put(key, datas);
    }

    public void addSection(SectionList data) {
        mSectionListSparseArray.put(data.getId(),data);
    }
    public void clear() {
        mSectionListSparseArray.clear();
    }

    public List<ViewData> getVideoListByIndex(int index, SparseArray<Integer> array) {
        SectionList sectionList=mSectionListSparseArray.get(index);
        if (sectionList==null) {
            return null;
        }
        List<ViewData> datas = new ArrayList<>();

        int count=sectionList.getItemList().size();
        int j=0;
        for (int i=0;i<count;i++) {
            ViewData data=sectionList.getItemList().get(i);
            switch (data.getType()){
                case WidgetHelper.Type.VIDEO:
                    datas.add(data);
                    array.put(j,i);
                    j++;
                    break;
                case  WidgetHelper.Type.VIDEO_COLLECTION_WITH_BRIEF:
                case  WidgetHelper.Type.VIDEO_COLLECTION_WITH_TITLE:
                case  WidgetHelper.Type.VIDEO_COLLECTION_WITH_COVER:
                    SectionModel.getInstance().putList(data.getData().getHeader().getId(),data.getData().getItemList());
                    break;
            }
        }
        if (mViewDatas.get(index)!=null) {
            mViewDatas.get(index).clear();
        }
        mViewDatas.put(index,datas);
        return datas;
    }

    public boolean isTheLastSelection(int index){
        return mSectionListSparseArray.indexOfKey(index)==mSectionListSparseArray.size()-1;
    }

    public List<ViewData> getVideoListByIndex(int index){
        if (mViewDatas.get(index)==null) {
            return getVideoListByIndex(index,new SparseArray<Integer>());
        }
        return mViewDatas.get(index);
    }


    public void addRelate(int id, SparseArray<List<ViewData>> data) {
        mRelate.put(id, data);
    }

    /**
     * 获取一个ViewData中有很多个ItemList的情况
     * @param id  对应的video id  或者是通过什么id来获取到的
     * @param index  第几行
     * @return
     */
    public List<ViewData> getRelate(int id, int index) {
        if (mRelate.get(id) != null) {
            return mRelate.get(id).get(index);
        }
        return null;
    }


    public void clearRelateByID(int id) {
        if (mRelate.get(id) != null) {
            mRelate.get(id).clear();
            mRelate.delete(id);
        }
    }

    @Override
    public List<ViewData> getVideoList(int videoID, int parentIndex) {
        if (mViewDatas.get(parentIndex) != null) {
            return mViewDatas.get(parentIndex);
        }
        return null;
    }
}
