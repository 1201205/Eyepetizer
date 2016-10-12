package com.hyc.eyepetizer.model;

import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.List;

/**
 * Created by ray on 16/9/7.
 */
public interface VideoListInterface {
    List<ViewData> getVideoList(int videoID, int parentIndex);
}
