package com.hyc.eyepetizer.presenter;

import android.text.TextUtils;
import android.util.SparseArray;
import com.hyc.eyepetizer.base.BasePresenter;
import com.hyc.eyepetizer.base.DefaultTransformer;
import com.hyc.eyepetizer.base.ExceptionAction;
import com.hyc.eyepetizer.contract.VideoRelateContract;
import com.hyc.eyepetizer.model.FeedModel;
import com.hyc.eyepetizer.model.VideoRelateModel;
import com.hyc.eyepetizer.model.beans.VideoRelated;
import com.hyc.eyepetizer.model.beans.ViewData;
import com.hyc.eyepetizer.net.Requests;
import com.hyc.eyepetizer.utils.WidgetHelper;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by ray on 16/9/6.
 */
public class VideoRelatePresenter extends BasePresenter<VideoRelateContract.View>
    implements VideoRelateContract.Presenter {
    private VideoRelateModel mModel;
    private int mID;


    public VideoRelatePresenter(VideoRelateContract.View view) {
        super(view);
        mModel = VideoRelateModel.getInstance();
    }


    @Override public void getRelate(int id) {
        mID = id;
        Requests.getApi()
            .getRelated(id)
            .compose(new DefaultTransformer<VideoRelated>())
            .map(new Func1<VideoRelated, List<ViewData>>() {

                @Override public List<ViewData> call(VideoRelated videoRelated) {
                    if (videoRelated != null) {
                        int count = videoRelated.getItemList().size();
                        SparseArray<List<ViewData>> data = new SparseArray<List<ViewData>>();
                        for (int i = 0; i < count; i++) {
                            List<ViewData> datas = videoRelated.getItemList()
                                .get(i)
                                .getData()
                                .getItemList();
                            data.put(i, datas);
                        }
                        mModel.addRelate(mID, data);
                        if (TextUtils.isEmpty(videoRelated.getNextPageUrl())) {
                            List<ViewData> datas = new ArrayList<ViewData>();
                            datas.addAll(videoRelated.getItemList());
                            datas.add(new ViewData(null, WidgetHelper.Type.NO_MORE));
                            return datas;
                        }
                        return videoRelated.getItemList();
                    }
                    return null;
                }
            })
            .subscribe(
                new Action1<List<ViewData>>() {
                    @Override public void call(List<ViewData> videoRelated) {
                        mView.showView(videoRelated);
                    }
                }, new ExceptionAction());
    }


    @Override public void detachView() {
        super.detachView();
        mModel.clear();
        FeedModel.getInstance().clearRelateByID(mID);
    }
}
