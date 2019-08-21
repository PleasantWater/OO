package com.blogofyb.oo.util.extensions

import android.util.Log
import io.reactivex.Observable

/**
 * Create by yuanbing
 * on 2019/8/15
 */
fun  <T> Observable<T>.safeSubscribeBy(subscribe: (T) -> Unit) = subscribe(subscribe, {
    toast("网络异常")
    Log.d("safeSubscribeBy", it.message ?: "Exception")
})