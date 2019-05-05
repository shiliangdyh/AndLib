package com.mirstone.module.fileprogress;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @package: com.zhuku.test
 * @fileName: TestService
 * @data: 2018/8/6 21:04
 * @author: ShiLiang
 * @describe:
 */
public interface ApiService {
    String BASE_URL = "http://yun.it7090.com/";
    String BASE_URL2 = "https://testapi.zhuku.co/";

    @POST("video/XHLaunchAd/video03.mp4")
    Flowable<ResponseBody> download();

    @Multipart
    @POST("common/file/fileoption/uploadFIleToIO")
    Flowable<ResponseBody> upload(@Part MultipartBody.Part part);
}
