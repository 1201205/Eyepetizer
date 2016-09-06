package com.hyc.eyepetizer.base;

import rx.Observable;

/**
 * Created by hyc on 2016/7/7.
 */
public class DefaultTransformer<T>
    implements Observable.Transformer<T, T> {

    public DefaultTransformer() {
    }


    @Override
    public Observable<T> call(Observable<T> observable) {
        return observable
            .compose(new SchedulerTransformer<T>());
    }
}
