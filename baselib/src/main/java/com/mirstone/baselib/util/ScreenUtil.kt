package com.mirstone.baselib.util

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * @package: com.zhuku.utils
 * @fileName: ScreenUtil
 * @data: 2018/4/13 10:12
 * @author: ShiLiang
 * @describe: 与屏幕相关操作
 */

object ScreenUtil {

    /**
     * 获取屏幕DisplayMetrics
     *
     * @param context 上下文
     * @return DisplayMetrics
     */
    private fun getMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    /**
     * 获取屏幕宽（PX）
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    fun getDeviceWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }

    /**
     * 获取屏幕高（PX）
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    fun getDeviceHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.y
    }

    /**
     * 获取屏幕密度
     *
     * @param context 上下文
     * @return 屏幕密度
     */
    fun getDeviceDensity(context: Context): Float {
        return getMetrics(context).density
    }

    /**
     * 获取屏幕dpi
     *
     * @param context 上下文
     * @return 屏幕dpi
     */
    fun getDeviceDpi(context: Context): Int {
        return getMetrics(context).densityDpi
    }

    /**
     * 获取手机状态栏高度(PX)
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            val dimension = context.resources.getDimension(resourceId).toDouble()
            result = Math.ceil(dimension).toInt()
        }
        return result
    }

    fun getActionBarSize(context: Context): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val actionbarSize = typedArray.getDimensionPixelOffset(0, 0)
        typedArray.recycle()
        return actionbarSize
    }
}
