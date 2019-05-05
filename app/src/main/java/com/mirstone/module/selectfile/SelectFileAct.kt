package com.mirstone.module.selectfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import com.mirstone.baselib.util.ToastUtil
import com.mirstone.selectfilelib.SLFilePicker
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_select_file.*
import java.util.*

/**
 * @package: com.mirstone.module.selectfile
 * @fileName: SelectFileAct
 * @data: 2018/8/8 14:40
 * @author: ShiLiang
 * @describe:
 */
class SelectFileAct : BaseSwipeBackActivity() {
    private val  TAG = "SelectFileAct"
    private val REQUEST_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_file)

        btnSelect.setOnClickListener {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe({ granted ->
                        if (granted) { // Always true pre-M
                            selectFile()
                        } else {
                            ToastUtil.show(this, "没有读写权限")
                        }
                    })
        }
    }

    private fun selectFile() {
        SLFilePicker().withActivity(this)
                .withRequestCode(REQUEST_CODE)
                .withMutiMode(false)
                .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE) {
                val list = data.getStringArrayListExtra("paths")
                Log.d(TAG,"files: ${Arrays.toString(list.toArray())}" )
            }
        }
    }
}