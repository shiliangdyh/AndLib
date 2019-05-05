package com.mirstone.baselib.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 * @package: com.mirstone.baselib.util
 * @fileName: InputUtil
 * @data: 2018/9/14 11:41
 * @author: ShiLiang
 * @describe: 输入法
 */
object InputUtil {

    fun closeInput(activity: Activity) {
        try {
            val view = activity.window.peekDecorView()
            view?.let {
                val inputmanger = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputmanger.hideSoftInputFromWindow(it.windowToken, 0)
            }
        } catch (e: Exception) {
            // no op
        }

    }
}
