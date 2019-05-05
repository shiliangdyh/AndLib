package com.mirstone.module.video

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import cn.jzvd.JZUserAction
import cn.jzvd.JZUserActionStandard
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.mirstone.R
import kotlinx.android.synthetic.main.activity_video_play.*

/**
 * @package: com.mirstone.module.video
 * @fileName: VideoPlayAct
 * @data: 2018/8/13 8:54
 * @author: ShiLiang
 * @describe:
 */
class VideoPlayAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

        val url = intent.getStringExtra("url")

        videoPlayer.setUp(url,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, url.substring(url.lastIndexOf('/') + 1, url.length))
//        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(videoPlayer.thumbImageView)
        videoPlayer.thumbImageView.setImageBitmap(getVideoThumbnail(url))
        JZVideoPlayer.setJzUserAction(MyUserActionStandard())
//        videoPlayer.onClick(videoPlayer.startButton)
    }

    fun getVideoThumbnail(videoPath: String): Bitmap {
        val media = MediaMetadataRetriever()
        media.setDataSource(videoPath)
        return media.frameAtTime
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

    override fun onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return
        }
        super.onBackPressed()
    }

    /**
     * 这只是给埋点统计用户数据用的，不能写和播放相关的逻辑，监听事件请参考MyJZVideoPlayerStandard，复写函数取得相应事件
     */
    internal inner class MyUserActionStandard : JZUserActionStandard {

        override fun onEvent(type: Int, url: Any, screen: Int, vararg objects: Any) {
            when (type) {
                JZUserAction.ON_CLICK_START_ICON -> Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_CLICK_START_ERROR -> Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_CLICK_START_AUTO_COMPLETE -> Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_CLICK_PAUSE -> Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_CLICK_RESUME -> Log.i("USER_EVENT", "ON_CLICK_RESUME" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_SEEK_POSITION -> Log.i("USER_EVENT", "ON_SEEK_POSITION" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_AUTO_COMPLETE -> Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_ENTER_FULLSCREEN -> Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_QUIT_FULLSCREEN -> Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_ENTER_TINYSCREEN -> Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_QUIT_TINYSCREEN -> Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME -> Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserAction.ON_TOUCH_SCREEN_SEEK_POSITION -> Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)

                JZUserActionStandard.ON_CLICK_START_THUMB -> Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                JZUserActionStandard.ON_CLICK_BLANK -> Log.i("USER_EVENT", "ON_CLICK_BLANK" + " title is : " + (if (objects.size == 0) "" else objects[0]) + " url is : " + url + " screen is : " + screen)
                else -> Log.i("USER_EVENT", "unknow")
            }
        }
    }

}
