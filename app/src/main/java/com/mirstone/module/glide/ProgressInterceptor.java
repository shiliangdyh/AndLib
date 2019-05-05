package com.mirstone.module.glide;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @package: com.zhuku.model.http.progress
 * @fileName: ProgressInterceptor
 * @data: 2018/8/7 15:54
 * @author: ShiLiang
 * @describe: 图片下载进度拦截
 */
public class ProgressInterceptor implements Interceptor {

    private static final Map<String, WeakReference<ProgressListener>> LISTENER_MAP = new HashMap<>();

    //入注册下载监听
    public static void addListener(String url, ProgressListener listener) {
        LISTENER_MAP.put(url, new WeakReference<>(listener));
    }

    public static ProgressListener getListener(String url) {
        WeakReference<ProgressListener> weakReference = LISTENER_MAP.get(url);
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    //取消注册下载监听
    public static void removeListener(String url) {
        LISTENER_MAP.remove(url);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String url = request.url().toString();
        ResponseBody body = response.body();
        return response.newBuilder().body(new GlideProgressResponseBody(url, body)).build();
    }
}
