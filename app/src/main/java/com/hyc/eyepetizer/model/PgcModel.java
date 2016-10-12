package com.hyc.eyepetizer.model;

import android.util.SparseArray;
import com.hyc.eyepetizer.model.beans.ViewData;
import java.util.List;

/**
 * Created by hyc on 2016/10/09.
 */
public class PgcModel implements VideoListInterface {
    private static PgcModel sModel;
    private SparseArray<List<ViewData>> mSection;


    private PgcModel() {
        mSection = new SparseArray<>();
    }


    public static PgcModel getInstance() {

        if (sModel == null) {
            synchronized (PgcModel.class) {
                if (sModel == null) {
                    sModel = new PgcModel();
                }
            }
        }
        return sModel;
    }


    public void putList(int key, List<ViewData> values) {
        mSection.put(key, values);
    }


    @Override
    public List<ViewData> getVideoList(int videoID, int parentIndex) {
        if (mSection.get(parentIndex) != null) {
            return mSection.get(parentIndex);
        }
        return null;
    }


    public void clear() {
        mSection.clear();
        sModel = null;
    }
}
