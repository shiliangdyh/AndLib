package com.mirstone.utils;


import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
 * File: RxUtil.java
 * Author: shiliang
 * Create: 2018/3/26 11:00
 * Changes (from 2018/3/26)
 * -----------------------------------------------------------------
 * 2018/3/26 : Create RxUtil.java (shiliang);
 * -----------------------------------------------------------------
 * Description: 线程切换，结果统一处理
 */
public final class RxUtil {
    private static final String TAG = "RxUtil";

    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {
        return flowable -> flowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
