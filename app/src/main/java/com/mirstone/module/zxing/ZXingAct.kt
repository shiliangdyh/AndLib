package com.mirstone.module.zxing

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import com.mirstone.baselib.util.ToastUtil
import com.mirstone.zxinglib.android.CaptureActivity
import com.mirstone.zxinglib.common.Constant
import com.mirstone.zxinglib.encode.QRCodeUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_zxing.*


/**
 * @package: com.mirstone.module.zxing
 * @fileName: ZXingAct
 * @data: 2018/8/8 16:11
 * @author: ShiLiang
 * @describe:
 */
class ZXingAct : BaseSwipeBackActivity() {

    private val REQUEST_CODE_SCAN: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zxing)

        btnScan.setOnClickListener {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe({ granted ->
                        if (granted) { // Always true pre-M
                            scan()
                        } else {
                            ToastUtil.show(this, "没有权限")
                        }
                    })
        }

        btnCreate.setOnClickListener {
            val bitmap = QRCodeUtil.createQRCode(content.text.toString(), 500)

            imageView.setImageBitmap(bitmap)
        }

        btnCreateLogo.setOnClickListener {
            val bitmap = QRCodeUtil.createQRCodeWithLogo(content.text.toString(), 500,
                    BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))

            imageViewLogo.setImageBitmap(bitmap)
        }
    }

    private fun scan() {
        val intent = Intent(this, CaptureActivity::class.java)
        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */

        //ZxingConfig config = new ZxingConfig();
        //config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
        //config.setPlayBeep(true);//是否播放提示音
        //config.setShake(true);//是否震动
        //config.setShowAlbum(true);//是否显示相册
        //config.setShowFlashLight(true);//是否显示闪光灯
        //intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                val content = data.getStringExtra(Constant.CODED_CONTENT)
                scanResult.text = "扫描结果为：$content"
            }
        }
    }
}