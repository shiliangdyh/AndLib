package com.mirstone.selectfilelib.bean

import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import com.mirstone.selectfilelib.R

/**
 * @package: com.mirstone.selectfilelib.bean
 * @fileName: Config
 * @data: 2018/7/27 9:56
 * @author: ShiLiang
 * @describe:
 */
data class Config(var titleId: Int = R.string.filelib_select_file) : Parcelable {
    val ROOT_PATH = Environment.getExternalStorageDirectory().absolutePath
    var requestCode: Int = 0
    //返回图标
    @IdRes
    var navigationIcon: Int = -1
    @StringRes
    var buttonTextId: Int = R.string.filelib_finish
    @StyleRes
    var themeId: Int = R.style.SLSelectFileStyle
    // 多选模式
    var mutiMode: Boolean = true
    //最多文件数，多选下生效
    var maxNum: Int = 9
    var types: Array<String>? = null
    var startPath: String = ROOT_PATH

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        requestCode = parcel.readInt()
        navigationIcon = parcel.readInt()
        buttonTextId = parcel.readInt()
        themeId = parcel.readInt()
        mutiMode = parcel.readByte() != 0.toByte()
        maxNum = parcel.readInt()
        types = parcel.createStringArray()
        startPath = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(titleId)
        parcel.writeInt(requestCode)
        parcel.writeInt(navigationIcon)
        parcel.writeInt(buttonTextId)
        parcel.writeInt(themeId)
        parcel.writeByte(if (mutiMode) 1 else 0)
        parcel.writeInt(maxNum)
        parcel.writeStringArray(types)
        parcel.writeString(startPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Config> {
        override fun createFromParcel(parcel: Parcel): Config {
            return Config(parcel)
        }

        override fun newArray(size: Int): Array<Config?> {
            return arrayOfNulls(size)
        }
    }


}
