package com.mirstone.module.transition

import android.os.Bundle
import com.alexvasilkov.gestures.Settings
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import kotlinx.android.synthetic.main.activity_gesture_image_view.*

/**
 * @package: com.mirstone.module.transition
 * @fileName: GestureImageViewAct
 * @data: 2018/8/10 10:16
 * @author: ShiLiang
 * @describe:
 */
class GestureImageViewAct : BaseSwipeBackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_image_view)

        gestureImageView.setImageResource(R.drawable.abc)
        gestureImageView.controller.settings
                .setRotationEnabled(true)
                .setRestrictRotation(true)
                .setExitEnabled(true)
                .setExitType(Settings.ExitType.ALL)
    }
}