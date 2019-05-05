package com.mirstone.module.imagevideo

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity





/**
 * @package: com.mirstone.module.imagevideo
 * @fileName: ImageVideoAct
 * @data: 2018/8/14 20:14
 * @author: ShiLiang
 * @describe:
 */
class ImageVideoAct:BaseSwipeBackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_video)

        getVideo()
    }

    /**
     * 获取视频列表
     */
    fun getVideo() {
        val projection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA)
        val orderBy = MediaStore.Video.Media.DISPLAY_NAME
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        getContentProvider(uri, projection, orderBy)
    }

    /**
     * 获取ContentProvider
     * @param projection
     * @param orderBy
     */
    fun getContentProvider(uri: Uri, projection: Array<String>, orderBy: String) {
        val listImage = ArrayList<HashMap<String, String>>()
        val cursor = contentResolver.query(uri, projection, null, null, orderBy) ?: return
        while (cursor.moveToNext()) {
            val map = HashMap<String, String>()
            for (i in projection.indices) {
                map[projection[i]] = cursor.getString(i)
                println(projection[i] + ":" + cursor.getString(i))
            }
            listImage.add(map)
        }
    }
}