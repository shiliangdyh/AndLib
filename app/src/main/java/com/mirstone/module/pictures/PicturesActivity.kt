package com.mirstone.module.pictures

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import com.mirstone.baselib.util.ToastUtil
import com.mirstone.ext.logD
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.Item
import com.zhihu.matisse.widget.PicturesLayout
import kotlinx.android.synthetic.main.activity_pictures.*
import java.util.*


/**
 * @package: com.mirstone.module.pictures
 * @fileName: PicturesActivity
 * @data: 2018/8/27 16:12
 * @author: ShiLiang
 * @describe:
 */
class PicturesActivity : BaseSwipeBackActivity() {
    private val REQUEST_CODE_CHOOSE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pictures)

        pictureLayout.setShowMode(false)
        pictureLayout.setOnAddClickListener(object: PicturesLayout.CallBack{
            override fun addPicture() {
                val rxPermissions = RxPermissions(this@PicturesActivity)
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe({ granted ->
                            if (granted) { // Always true pre-M
                                selectPhoto()
                            } else {
                                ToastUtil.show(this@PicturesActivity, "没有读写权限")
                            }
                        })
            }

            override fun onDelete(position: Int) {
                itemsResult?.removeAt(position)
            }

        })
    }

    private fun selectPhoto() {
        Matisse.from(this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
//                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .gridExpectedSize(200)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .selectedItems(itemsResult)
                .imageEngine(Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE)
    }

    var itemsResult: ArrayList<Item>? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            val list = Matisse.obtainPathResult(data)
            itemsResult = Matisse.obtainItemsResult(data)
            logD("list--> " + Arrays.toString(list.toTypedArray()))
            pictureLayout.onSelectPictures(list as ArrayList<String>?)
        }
    }

    override fun onBackPressed() {
        if (!pictureLayout.handleBackPressed()) {
            super.onBackPressed()
        }
    }
}