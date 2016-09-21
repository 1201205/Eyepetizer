package com.hyc.eyepetizer.net;

import com.hyc.eyepetizer.model.beans.DailySelection;
import com.hyc.eyepetizer.model.beans.Reply;
import com.hyc.eyepetizer.model.beans.Selection;
import com.hyc.eyepetizer.model.beans.TagVideoList;
import com.hyc.eyepetizer.model.beans.VideoRelated;
import com.hyc.eyepetizer.model.beans.Videos;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
     * http://baobab.wandoujia.com/api/v3/video/9194/detail/related?udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_yingyongbao_market&last_channel=eyepetizer_yingyongbao_market&system_version_code=22
     *   排行
     *   monthly  weekly
     *  http://baobab.wandoujia.com/api/v3/ranklist?num=10&strategy=historical&udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_yingyongbao_market&last_channel=eyepetizer_yingyongbao_market&system_version_code=22
     *
     *
     * 轻主题
     * http://baobab.wandoujia.com/api/v3/lightTopics/42?udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_yingyongbao_market&last_channel=eyepetizer_yingyongbao_market&system_version_code=22
     *
     * category
     * date  shareCount
     * http://baobab.wandoujia.com/api/v3/videos?categoryId=30&strategy=date&udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_yingyongbao_market&last_channel=eyepetizer_yingyongbao_market&system_version_code=22
     *
     * 标签：
     * http://baobab.wandoujia.com/api/v3/tag/videos?tagId=80&strategy=date&udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_yingyongbao_market&last_channel=eyepetizer_yingyongbao_market&system_version_code=22
     *
     * 个人
     * date  shareCount
     * http://baobab.wandoujia.com/api/v3/pgc/videos?pgcId=170&strategy=date&udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_yingyongbao_market&last_channel=eyepetizer_yingyongbao_market&system_version_code=22
     *
     *  视频评论:
     *  http://baobab.wandoujia.com/api/v1/replies/video?id=9034&udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_yingyongbao_market&last_channel=eyepetizer_yingyongbao_market&system_version_code=22
     *  每日精选
     * http://baobab.wandoujia.com/api/v2/feed?num=2&udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_yingyongbao_market&last_channel=eyepetizer_yingyongbao_market&system_version_code=22
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
    @GET("/api/v3/video/{id}/detail/related") Observable<VideoRelated> getRelated(
        @Path("id") int id);

    @GET("/api/v1/replies/video") Observable<Reply> getReply(
            @Query("id") int id);

    //    id=9034&lastId=9&num=10
    @GET("/api/v1/replies/video") Observable<Reply> getMoreReply(
            @Query("id") int id,@Query("lastId") int lastId,@Query("num") int num);

    /**
     * 获取今天的精选
     * @param num
     * @return
     */
    @GET("/api/v2/feed") Observable<DailySelection> getDailySelection(
            @Query("num") int num);

    /**
     * 获取指定的精选
     * @param num
     * @param date
     * @return
     */
    @GET("/api/v2/feed") Observable<DailySelection> getDailySelection(
            @Query("num") int num,@Query("date") long date);
    ///api/v3/pgc/videos?pgcId=170&strategy=date
    /**
     * 获取排行榜视频
     * @param num  10
     * @param strategy  monthly  weekly  historical
     * @return
     */
    @GET("/api/v3/ranklist?vc=126") Observable<Videos> getRankByStrategy(
            @Query("num") int num,@Query("strategy") String strategy);
    /**
     * 获取pgc视频
     *
     * @param pgcId 10
     * @param strategy monthly  weekly  historical
     */
    @GET("/api/v3/pgc/videos") Observable<Videos> getPgcByStrategy(
        @Query("pgcId") int pgcId, @Query("strategy") String strategy);

    @GET("/api/v3/pgc/videos") Observable<Videos> getMorePgcByStrategy(@Query("start") int start,@Query("num") int num,
            @Query("pgcId") int pgcId, @Query("strategy") String strategy);

    //http://baobab.wandoujia.com/api/v3/tag/videos?tagId=458&strategy=date&udid=8954dd2dac7e41d68d967d5cc8115ced8b7af94c&vc=126&vn=2.4.1&deviceModel=Redmi%20Note%203&first_channel=eyepetizer_yingyongbao_market&last_channel=eyepetizer_yingyongbao_market&system_version_code=22
    @GET("/api/v3/tag/videos") Observable<TagVideoList> getTagVideoByStragtegy(
            @Query("tagId") int tagId, @Query("strategy") String strategy);
    @GET("/api/v3/tag/videos") Observable<TagVideoList> getMoreTagVideoByStragtegy(
        @Query("start") int start, @Query("num") int num,
        @Query("tagId") int tagId, @Query("strategy") String strategy);
    //http://baobab.wandoujia.com/api/v3/videos?start=10&num=10&categoryId=38&strategy=date"
    @GET("/api/v3/videos") Observable<Videos> getCategoryByStrategy(
            @Query("categoryId") int categoryId, @Query("strategy") String strategy);

    @GET("/api/v3/videos") Observable<Videos> getMoreCategoryByStrategy(@Query("start") int start,@Query("num") int num,
                                                                       @Query("categoryId") int categoryId, @Query("strategy") String strategy);
}
