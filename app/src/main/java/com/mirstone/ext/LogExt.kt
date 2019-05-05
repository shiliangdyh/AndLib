package com.mirstone.ext

import android.text.TextUtils
import android.util.Log
import com.mirstone.baselib.BuildConfig

/**
 * @package: com.mirstone.baselib.ext
 * @fileName: LogExt
 * @data: 2018/8/1 11:05
 * @author: ShiLiang
 * @describe:
 */
fun Any.logD(msg: String?, tag: String = "LOG") {
    if (BuildConfig.DEBUG && !TextUtils.isEmpty(msg)) {
        Log.d(tag, msg)
    }
}