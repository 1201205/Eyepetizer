package com.hyc.eyepetizer.model;

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


    private FeedModel() {
        mSectionLists = new ArrayList<>();
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

    public List<ViewData> getVideoListByIndex(int index) {
        if (mSectionLists.size() < index) {
            return null;
        }
        List<ViewData> datas = new ArrayList<>();
        for (ViewData data : mSectionLists.get(index-1).getItemList()) {
            if (WidgetHelper.Type.VIDEO.equals(data.getType())) {
                datas.add(data);
            }
        }
        return datas;
    }
}
