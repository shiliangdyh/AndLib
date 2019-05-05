package com.mirstone.baselib.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mirstone.baselib.util.InputUtil
import com.mirstone.baselib.util.StatusBarUtil
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.app.SwipeBackActivity

/**
 * @package: com.mirstone.baselib
 * @fileName: SLActivity
 * @data: 2018/7/23 15:22
 * @author: ShiLiang
 * @describe:
 */
open class BaseSwipeBackActivity : SwipeBackActivity() {
    protected var activity: Activity? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        activity = this
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        activity = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSwipeConfig()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initStateBar()
    }

    private fun initStateBar() {
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000)
        }
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initStateBar()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        initStateBar()
    }

    private fun initSwipeConfig() {
        val swipeBackLayout = swipeBackLayout
        swipeBackLayout.setEnableGesture(isEnableGesture())
        swipeBackLayout.setEdgeTrackingEnabled(getEdgeTrackingEnabled())
        swipeBackLayout.addSwipeListener(object : SwipeBackLayout.SwipeListener {
            override fun onScrollStateChange(state: Int, scrollPercent: Float) {

            }

            override fun onEdgeTouch(edgeFlag: Int) {
//                vibrate(VIBRATE_DURATION.toLong())
            }

            override fun onScrollOverThreshold() {
//                vibrate(VIBRATE_DURATION.toLong())
            }
        })
    }

    override fun onPause() {
        super.onPause()
        InputUtil.closeInput(this)
    }

    companion object {
        private const val VIBRATE_DURATION = 10
    }

    //是否滑动退出
    protected open fun isEnableGesture() = true

    protected open fun getEdgeTrackingEnabled() = SwipeBackLayout.EDGE_LEFT

    protected open fun getStateBarColor() = Color.parseColor("#55000000")
}
