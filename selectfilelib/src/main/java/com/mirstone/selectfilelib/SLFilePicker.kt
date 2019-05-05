package com.mirstone.selectfilelib

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v4.app.Fragment
import com.mirstone.selectfilelib.bean.Config
import com.mirstone.selectfilelib.ui.SLSelectFileActivity
import java.util.ResourceBundle.getBundle

/**
 * @package: com.mirstone.selectfilelib
 * @fileName: SLFilePicker
 * @data: 2018/7/27 10:03
 * @author: ShiLiang
 * @describe:
 */
class SLFilePicker {
    val ROOT_PATH = Environment.getExternalStorageDirectory().absolutePath
    private val DEF_REQUEST_CODE = 0x000000
    var requestCode: Int = DEF_REQUEST_CODE
    var activity: Activity? = null
    var fragment: Fragment? = null
    @StyleRes
    var themeId: Int = R.style.SLSelectFileStyle
    @IdRes
    var navigationIcon: Int = -1
    @StringRes
    var buttonTextId: Int = R.string.filelib_finish
    // 多选模式
    var mutiMode: Boolean = true
    //最多文件数，多选下生效
    var maxNum: Int = 9
    var types: Array<String>? = null
    var startPath: String = ROOT_PATH


    fun withActivity(activity: Activity): SLFilePicker {
        this.activity = activity
        return this
    }

    fun withStartPath(startPath: String): SLFilePicker {
        this.startPath = startPath
        return this
    }

    fun withTypes(types: Array<String>): SLFilePicker {
        this.types = types
        return this
    }

    fun withFragment(fragment: Fragment): SLFilePicker {
        this.fragment = fragment
        return this
    }

    fun withRequestCode(requestCode: Int): SLFilePicker {
        this.requestCode = requestCode
        return this
    }

    fun withTheme(@StyleRes themeId: Int): SLFilePicker {
        this.themeId = themeId
        return this
    }

    fun withNavigationIcon(@IdRes navigationIcon: Int): SLFilePicker {
        this.navigationIcon = navigationIcon
        return this
    }

    fun withButtonTextId(@StringRes buttonTextId: Int): SLFilePicker {
        this.buttonTextId = buttonTextId
        return this
    }

    fun withMutiMode(mutiMode: Boolean): SLFilePicker {
        this.mutiMode = mutiMode
        return this
    }

    fun withMaxNum(maxNum: Int): SLFilePicker {
        this.maxNum = maxNum
        return this
    }

    fun start() {
        if (activity == null && fragment == null) {
            throw RuntimeException("You must pass Activity or Fragment by withActivity or withFragment method")
        }
        val intent = initIntent()
        val bundle = getBundle()
        intent.putExtras(bundle)

        if (activity != null) {
            activity!!.startActivityForResult(intent, requestCode)
        } else {
            this.fragment!!.startActivityForResult(intent, requestCode)
        }
    }

    private fun initIntent(): Intent {
        var intent: Intent? = null
        if (activity != null) {
            intent = Intent(activity, SLSelectFileActivity::class.java)
        } else {
            intent = Intent(fragment!!.activity, SLSelectFileActivity::class.java)
        }
        return intent
    }

    private fun getBundle(): Bundle {
        val config = Config()
        config.navigationIcon = this.navigationIcon
        config.buttonTextId = this.buttonTextId
        config.themeId = this.themeId
        config.requestCode = this.requestCode
        config.mutiMode = this.mutiMode
        config.maxNum = this.maxNum
        config.startPath = this.startPath
        config.types = this.types
        val bundle = Bundle()
        bundle.putParcelable("config", config)
        return bundle
    }
}
