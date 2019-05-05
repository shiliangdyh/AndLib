package com.mirstone.module.glide

import android.os.Bundle
import android.util.Log
import android.view.View
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import kotlinx.android.synthetic.main.activity_glide.*

/**
 * @package: com.mirstone.module
 * @fileName: GlideActivity
 * @data: 2018/8/7 16:33
 * @author: ShiLiang
 * @describe:
 */
class GlideActivity : BaseSwipeBackActivity() {
    private val TAG = "GlideActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)

        btnLoad.setOnClickListener { loadImage() }
    }

    private fun loadImage() {
        val url = "https://bbsfiles.vivo.com.cn/vivobbs/attachment/forum/201710/03/204241z8ta66qzq33z853d.jpg"
//        val url = "https://testapi.zhuku.co/common/file/fileoption/downloadFIleToIO?uuid=8eb11dbf-9154-4b48-820b-f8c90c517a86"
        ProgressInterceptor.addListener(url) { progress, total, done ->
            Log.d(TAG, "progress = $progress total = $total done = $done thread ${Thread.currentThread().name}")
            progressView.percent = (progress * 100 / total).toFloat();
            if (done) {
                progressView.visibility = View.GONE
                ProgressInterceptor.removeListener(url)
            }
        }
        GlideUtil.loadImage(url, imageView)
    }
}