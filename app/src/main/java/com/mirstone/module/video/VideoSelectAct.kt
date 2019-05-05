package com.mirstone.module.video

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import com.github.tcking.giraffecompressor.GiraffeCompressor
import com.mirstone.App
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import com.mirstone.baselib.util.ToastUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_video.*
import java.io.File


/**
 * @package: com.mirstone.module.video
 * @fileName: VideoSelectAct
 * @data: 2018/8/13 8:54
 * @author: ShiLiang
 * @describe:
 */
class VideoSelectAct : BaseSwipeBackActivity() {

    private val REQUEST_FOR_VIDEO_FILE = 1000
    private var videoDialog: VideoDialog? = null
    private var videoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            builder.detectFileUriExposure()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        btnAdd.setOnClickListener {
            if (videoDialog == null) {
                videoDialog = VideoDialog()
                videoDialog?.setCallBack(object : VideoDialog.CallBack {

                    override fun onSuccess(videoPath: String) {
                        compress(videoPath)
                    }

                    override fun onFailed(error: String) {

                    }
                })
            }
            videoDialog?.show(supportFragmentManager)
        }

        btnPlay.setOnClickListener {
            if (TextUtils.isEmpty(videoPath)) {
                ToastUtil.show(this, "请选择一个视频")
                return@setOnClickListener
            }
            val intent = Intent(this, VideoPlayAct::class.java)
            intent.putExtra("url", videoPath)
            startActivity(intent)
        }

    }

    private fun compress(videoPath: String) {
        ToastUtil.show(this, "处理中")
        GiraffeCompressor.create() //two implementations: mediacodec and ffmpeg,default is mediacodec
                .input(videoPath) //set video to be compressed
                .output(Environment.getExternalStorageDirectory().absolutePath + File.separator + System.currentTimeMillis() + ".mp4") //set compressed video output
                .bitRate(3000000)//set bitrate 码率
                .resizeFactor(1.0f)//set video resize factor 分辨率缩放,默认保持原分辨率
                .ready()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    ToastUtil.show(App.app, "处理完成")
                    Log.d("onNext", "output--> ${it.output}")
                    this@VideoSelectAct.videoPath = it.output
                    videoUrl.text = it.output.substring(it.output.lastIndexOf(File.separatorChar) + 1, it.output.length)
                    videoThumb.setImageBitmap(getVideoThumbnail(it.output))
                })
    }

    fun getVideoThumbnail(videoPath: String): Bitmap {
        val media = MediaMetadataRetriever()
        media.setDataSource(videoPath)
        return media.frameAtTime
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        videoDialog?.onActivityResult(requestCode, resultCode, data)
    }

}
