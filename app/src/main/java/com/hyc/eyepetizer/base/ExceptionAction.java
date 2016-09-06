package com.hyc.eyepetizer.base;

import com.google.gson.JsonParseException;
import com.hyc.eyepetizer.R;
import com.hyc.eyepetizer.utils.AppUtil;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import rx.exceptions.Exceptions;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/7/7.
 */
public class ExceptionAction implements Action1<Throwable> {

    private boolean toast = true;


    public ExceptionAction(boolean toast) {
        this.toast = toast;
    }


    public ExceptionAction() {
        toast = true;
    }


    protected void onNoNetWork() {
    }


    @Override
    public void call(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof JsonParseException) {
            AppUtil.showToast(R.string.parse_error);
        } else if (throwable instanceof NullPointerException) {
            AppUtil.showToast(R.string.none_point_error);
            Exceptions.propagate(throwable);
        } else if (throwable instanceof SocketException) {
            AppUtil.showToast(R.string.net_error);
        } else if (throwable instanceof UnknownHostException) {
            if (toast) {
                AppUtil.showToast(R.string.net_error);
            }
            onNoNetWork();
        } else if (throwable instanceof SocketTimeoutException) {
            AppUtil.showToast(R.string.time_out);
        } else {
            AppUtil.showToast(R.string.net_error);
        }
    }
}
