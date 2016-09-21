package com.hyc.eyepetizer.presenter;

import android.text.TextUtils;
import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.VideoListContract;
import com.hyc.eyepetizer.model.VideoListModel;
import com.hyc.eyepetizer.model.beans.TagVideoList;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.net.Requests;
import com.hyc.eyepetizer.utils.WidgetHelper;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/20.
 */
public class TagVideoPresenter extends VideoListPresenter {
    private int mID;
    private String mStragtegy;
    private VideoListModel mModel;
    private int mTypeID;
    private int mCount;

    public TagVideoPresenter(int typeID,int id,VideoListContract.View view,String stragtegy) {
        super(view);
        mID=id;
        mTypeID = typeID;
        mStragtegy=stragtegy;
        mModel = VideoListModel.getInstance();
    }

    @Override
    public void getVideoList() {
        mCompositeSubscription.add(
                Requests.getApi().getTagVideoByStragtegy(mID,mStragtegy).compose(new DefaultTransformer<TagVideoList>()).subscribe(new Action1<TagVideoList>() {
                    @Override
                    public void call(TagVideoList tagVideoList) {
                        List<ViewData> list=new ArrayList<ViewData>();
                        list.addAll(tagVideoList.getItemList());
                        mModel.addVideoList(mTypeID, tagVideoList.getItemList());
                        if (TextUtils.isEmpty(tagVideoList.getNextPageUrl())) {
                            list.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
                            mView.noMore();
                        } else {
                            mCount = list.size();
                        }
                        mView.showList(list);
                    }
                },new ExceptionAction(){
                    @Override
                    protected void onNoNetWork() {
                        mView.showError();
                    }
                })
        );
    }

    @Override
    public void getMore() {
        mCompositeSubscription.add(
            Requests.getApi()
                .getMoreTagVideoByStragtegy(mCount, 10, mID, mStragtegy)
                .compose(new DefaultTransformer<TagVideoList>())
                .subscribe(new Action1<TagVideoList>() {
                    @Override
                    public void call(TagVideoList videos) {
                        mModel.addMore(mTypeID, videos.getItemList());
                        List<ViewData> list = new ArrayList<ViewData>();
                        list.addAll(videos.getItemList());
                        if (TextUtils.isEmpty(videos.getNextPageUrl())) {
                            list.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
                            mView.noMore();
                        } else {
                            mCount += list.size();
                        }
                        mView.showList(list);
                    }
                }, new ExceptionAction())
        );
    }


    @Override public void detachView() {
        super.detachView();
        mModel.clear(mTypeID);
    }
}
