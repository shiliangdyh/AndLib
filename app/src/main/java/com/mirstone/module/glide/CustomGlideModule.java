package com.mirstone.module.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;
import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * @package: com.mirstone.module
 * @fileName: CustomGlideModule
 * @data: 2018/8/7 16:48
 * @author: ShiLiang
 * @describe:
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule{
    private static final String GLIDE_CACHE_DIR = "glide";

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        // 设置磁盘缓存为100M，缓存在内部缓存目录
        int cacheSizeBytes = 1024 * 1024 * 100;
        File cacheFile = new File(context.getCacheDir(), GLIDE_CACHE_DIR);
        builder.setDiskCache(new DiskLruCacheFactory(cacheFile.getAbsolutePath(), cacheSizeBytes));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
