package com.mirstone.module.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @package: com.mirstone.module
 * @fileName: GlideUtil
 * @data: 2018/8/7 16:51
 * @author: ShiLiang
 * @describe:
 */
public class GlideUtil {
    public static void loadImage(String url, ImageView imageView){
        GlideApp.with(imageView)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
}
