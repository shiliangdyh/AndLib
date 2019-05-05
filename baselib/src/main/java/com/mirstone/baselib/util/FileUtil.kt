package com.mirstone.baselib.util

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
}
