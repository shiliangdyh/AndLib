package com.mirstone.selectfilelib.filter

import java.io.File
import java.io.FileFilter

/**
 * @package: com.mirstone.selectfilelib.filter
 * @fileName: SLFileFilter
 * @data: 2018/7/28 15:03
 * @author: ShiLiang
 * @describe:
 */
class SLFileFilter(private val mTypes: Array<String>?, private val showHideFile: Boolean) : FileFilter {

    override fun accept(file: File): Boolean {
        if (file.isDirectory) {
            return showHideFile || !file.name.startsWith(".")
        }
        if (mTypes != null && mTypes.size > 0) {
            for (mType in mTypes) {
                if (file.name.endsWith(mType.toLowerCase()) || file.name.endsWith(mType.toUpperCase())) {
                    return true
                }
            }
        } else {
            return true
        }
        return false
    }
}
