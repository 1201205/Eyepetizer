package com.hyc.eyepetizer.net;

import com.hyc.eyepetizer.model.beans.Selection;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ray on 16/8/25.
 */
public interface Api {
    /**
     * 首页：
     *精选：
     * http://baobab.wandoujia.com/api/v3/tabs/selected?udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_xiaomi_market&last_channel=eyepetizer_xiaomi_market&system_version_code=22
     *
     * http://baobab.wandoujia.com/api/v3/tabs/selected?pagination=1&needFilter=true&udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_xiaomi_market&last_channel=eyepetizer_xiaomi_market&system_version_code=22
     * 往期：
     * http://baobab.wandoujia.com/api/v2/feed?num=2&udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_xiaomi_market&last_channel=eyepetizer_xiaomi_market&system_version_code=22
     *热门作者：
     * http://baobab.wandoujia.com/api/v3/tabs/pgcs?udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_xiaomi_market&last_channel=eyepetizer_xiaomi_market&system_version_code=22
     * 发现：
     * http://baobab.wandoujia.com/api/v3/discovery?udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_xiaomi_market&last_channel=eyepetizer_xiaomi_market&system_version_code=22
     *
     */
    @GET("api/v3/tabs/selected")
    Observable<Selection> getSelection();
    //http://baobab.wandoujia.com/api/v3/tabs/selected?pagination=1&needFilter=true"
    @GET("api/v3/tabs/selected") Observable<Selection> getMoreSelection(
        @Query("pagination") int index, @Query("needFilter") boolean needFilter);

}
