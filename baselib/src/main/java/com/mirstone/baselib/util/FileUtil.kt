package com.mirstone.baselib.util

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File
import java.text.DecimalFormat

/**
 * @package: com.mirstone.baselib.util
 * @fileName: FileUtil
 * @data: 2018/8/2 16:30
 * @author: ShiLiang
 * @describe:
 */
object FileUtil {

    /**
     * 格式化文件大小
     * @param size      文件字节数
     * @param pattern   返回的格式
     * @return           带单位的文件大小，如：1.2 MB
     */
    fun getReadableFileSize(size: Long, pattern:String = "#.00"): String {
        if (size <= 0) return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat(pattern).format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    /**
     * 获取Android SD卡目录
     * 10.0中 获取到的是 Android/sandbox/package_name/relativePath/   10.0沙盒机制，卸载删除
     * 10.0以下获取到的是 sd根目录/relativePath/ 卸载存在
     *
     * @param context
     * @param relativePath
     * @return
     */
    fun getExternalPublicStorageDir(context: Context, relativePath: String): File? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            if (Build.VERSION.SDK_INT >= 29) {
                //10.0
                Environment.getExternalStoragePublicDirectory(relativePath)
            } else {
                File(Environment.getExternalStorageDirectory(), relativePath)
            }
        } else getExternalFileDir(context, relativePath)
    }

    /**
     * 获取Android SD卡目录
     * Android/data/package_name/files/{@param relativePath}/
     * 应用卸载后删除
     *
     * @param context
     * @param relativePath
     * @return
     */
    fun getExternalFileDir(context: Context, relativePath: String): File? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            context.getExternalFilesDir(relativePath)
        } else File(getPrivateFileDir(context), relativePath)
    }

    /**
     * 获取Android私有files目录
     * data/data/package_name/files/
     * 应用卸载后删除
     *
     * @param context
     * @return
     */
    fun getPrivateFileDir(context: Context): File {
        return context.filesDir
    }


}
