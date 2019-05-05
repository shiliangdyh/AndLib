package com.mirstone

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.mirstone.baselib.base.BaseSwipeBackActivity
import com.mirstone.module.fileprogress.FileProgressAct
import com.mirstone.module.flexbox.FlexboxLayoutActivity
import com.mirstone.module.glide.GlideActivity
import com.mirstone.module.pictures.PicturesActivity
import com.mirstone.module.screenadapter.ScreenAdapterAct
import com.mirstone.module.selectfile.SelectFileAct
import com.mirstone.module.transition.GestureViewsAct
import com.mirstone.module.video.VideoSelectAct
import com.mirstone.module.xrecyclerview.InputActivity
import com.mirstone.module.xrecyclerview.XRecyclerViewActivity
import com.mirstone.module.zxing.ZXingAct
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init();
    }

    private fun init() {
        btnGlide.setOnClickListener { startActivity(Intent(this, GlideActivity::class.java)) }
        btnProgress.setOnClickListener { startActivity(Intent(this, FileProgressAct::class.java)) }
        btnSelectFile.setOnClickListener { startActivity(Intent(this, SelectFileAct::class.java)) }
        btnAdapter.setOnClickListener { startActivity(Intent(this, ScreenAdapterAct::class.java)) }
        btnZxing.setOnClickListener { startActivity(Intent(this, ZXingAct::class.java)) }
        btnGestureViews.setOnClickListener { startActivity(Intent(this, GestureViewsAct::class.java)) }
        btnVideo.setOnClickListener { startActivity(Intent(this, VideoSelectAct::class.java)) }
        btnPictures.setOnClickListener { startActivity(Intent(this, PicturesActivity::class.java)) }
        btnXRecyclerView.setOnClickListener { startActivity(Intent(this, XRecyclerViewActivity::class.java)) }
        btnImageVideo.setOnClickListener { startActivity(Intent(this, InputActivity::class.java)) }
        btnFlexboxLayoutManager.setOnClickListener { startActivity(Intent(this, FlexboxLayoutActivity::class.java)) }
    }

    override fun isEnableGesture(): Boolean {
        return false
    }

    override fun getStateBarColor(): Int {
        return Color.BLACK
    }
}
