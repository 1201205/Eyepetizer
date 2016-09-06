package com.hyc.eyepetizer.base;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hyc on 2016/7/7.
 */
public class ErrorCheckerTransformer<T>
    implements Observable.Transformer<T, T> {

    @Override
    public Observable<T> call(Observable<T> observable) {
        return observable.map(new Func1<T, T>() {
            @Override
            public T call(T t) {
                return t;
            }
        });
    }
}
