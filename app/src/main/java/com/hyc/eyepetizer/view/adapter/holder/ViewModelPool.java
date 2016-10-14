package com.hyc.eyepetizer.view.adapter.holder;

import android.util.SparseArray;

import com.hyc.eyepetizer.view.adapter.ItemViewProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/14.
 */
public class ViewModelPool {
    private Map<String, ItemViewProvider> mTypeMap;
    private List<ItemViewProvider> mProviders;
    private static ViewModelPool sViewModelPool;

    private ViewModelPool() {
        mProviders = new ArrayList<>();
        mTypeMap = new HashMap<>();
    }

    private void putInPool(String type, ItemViewProvider provider) {
        mTypeMap.put(type, provider);
        mProviders.add(provider);
    }

    public int getType(String type){
      return   mProviders.indexOf(mTypeMap.get(type));
    }
    public ItemViewProvider getProviderByType(String type){
     return mTypeMap.get(type);
    }
    public ItemViewProvider getProvider(int index){
        return mProviders.get(index);
    }

    public static final ViewModelPool getInstance() {
        synchronized (ViewModelPool.class) {
            if (sViewModelPool == null) {
                sViewModelPool = new ViewModelPool();
            }
            return sViewModelPool;
        }
    }

    public void init(){
        putInPool("video",new VideoViewProvider());
        putInPool("forwardFooter",new ForwardViewProvider());
        putInPool("videoCollectionWithCover",new CoverVideoViewProvider());
        putInPool("blankFooter",new BlankViewProvider());
        putInPool("textHeader",new TextHeaderViewProvider());
        putInPool("videoCollectionWithBrief",new BriefVideoViewProvider());
        putInPool("videoCollectionWithTitle",new TitleVideoViewProvider());
        putInPool("campaign",new CampaignViewProvider());
        putInPool("briefCard",new BriefCardViewProvider());
        putInPool("banner2",new RectangleCardViewProvider());
        putInPool("video",new VideoViewProvider());
        putInPool("leftAlignTextHeader",new LeftTextHeaderViewProvider());
        putInPool("blankCard",new BlankCardViewProvider());
        putInPool("noMore",new NoMoreViewProvider());
        putInPool("horizontalScrollCard",new BannerViewProvider());
        putInPool("squareCard",new SquareCardViewProvider());
        putInPool("rectangleCard",new DiscoveryCardViewProvider());
    }

}
