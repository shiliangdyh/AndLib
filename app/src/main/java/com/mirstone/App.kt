package com.mirstone

import android.app.Application
import android.content.Context
import com.github.tcking.giraffecompressor.GiraffeCompressor
import com.mirstone.baselib.screenadapter.Density
import com.mirstone.baselib.util.LogUtil


/**
 * @package: com.mirstone.andframe
 * @fileName: App
 * @data: 2018/8/8 15:21
 * @author: ShiLiang
 * @describe:
 */
class App : Application() {
    companion object {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()
        Density.setDensity(this, 375f);
        GiraffeCompressor.DEBUG = true
        GiraffeCompressor.init(this)
        LogUtil.DEBUG = true
        LogUtil.hasWriteFilePermission = true
        LogUtil.mContext = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        app = this;
    }
}
