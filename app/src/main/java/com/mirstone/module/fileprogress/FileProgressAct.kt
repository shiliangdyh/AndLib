package com.mirstone.module.fileprogress

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import com.mirstone.BuildConfig
import com.mirstone.R
import com.mirstone.baselib.base.BaseSwipeBackActivity
import com.mirstone.baselib.util.ToastUtil
import com.mirstone.utils.RxUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_file_progress.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @package: com.mirstone.module.fileprogress
 * @fileName: FileProgressAct
 * @data: 2018/8/8 9:13
 * @author: ShiLiang
 * @describe:
 */
class FileProgressAct : BaseSwipeBackActivity() {
    private val TAG = "FileProgressAct"

    private val path = "/storage/emulated/0/Android/data/b.zip"
    private val TIME_OUT: Long = 5//超时时间
    private val TIME_UNIT = TimeUnit.SECONDS//超时单位

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_progress)

        btnDownload.setOnClickListener { download() }
        btnUpload.setOnClickListener {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe({ granted ->
                        if (granted) { // Always true pre-M
                            upload()
                        } else {
                            ToastUtil.show(this,"没有读写权限")
                        }
                    })
        }
    }

    private fun upload() {
        Log.d(TAG, "upload")
        progressView.visibility = View.VISIBLE
        progressView.percent = 0f
        val okhttpClientbuilder = OkHttpClient.Builder()
        //不要添加拦截器，否则RequestBody.writeTo（）会调用多次
//        if (BuildConfig.DEBUG) {
//            val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            okhttpClientbuilder.addInterceptor(httpLoggingInterceptor)
//        }
        okhttpClientbuilder.connectTimeout(TIME_OUT, TIME_UNIT)
                .writeTimeout(TIME_OUT, TIME_UNIT)
                .readTimeout(TIME_OUT, TIME_UNIT)

        val retrofit = Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL2)
                .client(okhttpClientbuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val apiService = retrofit.create(ApiService::class.java)

        val file = File(path)
        val requestBody = ProgressRequestBody(file, "multipart/form-data", ProgressListener { progress, total, done ->
            progressView.percent = (progress * 100 / total).toFloat()
            if (done){
                progressView.visibility = View.GONE
            }
        })
        val part = MultipartBody.Part.createFormData("OTHER", "b.zip", requestBody)
        val subscribe = apiService.upload(part)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe({
                    Log.d(TAG, "success $it")
                }, {
                    Log.d(TAG, "failed $it.message")
                })
    }

    private fun download() {
        progressView.visibility = View.VISIBLE
        progressView.percent = 0f
        val okhttpClientbuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okhttpClientbuilder.addInterceptor(httpLoggingInterceptor)
        }
        okhttpClientbuilder.addNetworkInterceptor { chain ->
            val orginalResponse = chain.proceed(chain.request())
            orginalResponse.newBuilder()
                    .body(ProgressResponseBody(orginalResponse.body(), ProgressListener { progress, total, done ->
                        Log.e(TAG, Thread.currentThread().name)
                        Log.d(TAG, "onProgress: total ----> $total ,progress ----> $progress , done ---> $done")
                        progressView.percent = (progress * 100 / total).toFloat();
                        if (done) {
                            progressView.visibility = View.GONE
                        }
                    }))
                    .build()
        }
        okhttpClientbuilder.connectTimeout(TIME_OUT, TIME_UNIT)
                .writeTimeout(TIME_OUT, TIME_UNIT)
                .readTimeout(TIME_OUT, TIME_UNIT)

        val retrofit = Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(okhttpClientbuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val apiService = retrofit.create<ApiService>(ApiService::class.java)
        val subscribe = apiService.download()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe({
                    Log.d(TAG, "success")
                }, {
                    Log.d(TAG, "failed ${it.message}")
                })
    }
}