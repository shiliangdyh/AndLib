package com.mirstone.module.screenadapter

import android.os.Bundle
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity

/**
 * @package: com.mirstone.module.screenadapter
 * @fileName: ScreenAdapterAct
 * @data: 2018/8/8 15:27
 * @author: ShiLiang
 * @describe:
 */
class ScreenAdapterAct :BaseSwipeBackActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_adapter)
    }
}