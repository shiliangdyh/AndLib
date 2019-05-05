package com.mirstone.module.transition

import android.content.Intent
import android.os.Bundle
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import kotlinx.android.synthetic.main.activity_gesture_views.*

/**
 * @package: com.mirstone.module.transition
 * @fileName: GestureViewsAct
 * @data: 2018/8/9 21:39
 * @author: ShiLiang
 * @describe:
 */
class GestureViewsAct: BaseSwipeBackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_views)

        btnGestureImageView.setOnClickListener { startActivity(Intent(this, GestureImageViewAct::class.java)) }
        btnImageViewAnim.setOnClickListener { startActivity(Intent(this, ImageViewAnimAct::class.java)) }
        btnRecyclerView.setOnClickListener { startActivity(Intent(this, RecyclerToPagerAct::class.java)) }
    }
}