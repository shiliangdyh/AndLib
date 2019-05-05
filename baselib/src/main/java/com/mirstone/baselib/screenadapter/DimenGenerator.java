package com.mirstone.baselib.screenadapter;

import android.content.Context;

/**
 * @package: com.mirstone.baselib.util
 * @fileName: DimenGenerator
 * @data: 2018/8/1 9:04
 * @author: ShiLiang
 * @describe:
 */
public class DimenGenerator {
    /**
     * 设计稿尺寸(根据自己设计师的设计稿的宽度填入)
     */
    private static final int DESIGN_WIDTH = 375;

    /**
     * 设计稿高度  没用到
     */
    private static final int DESIGN_HEIGHT = 667;

    public static void generator(Context context) {

        DimenTypes[] values = DimenTypes.values();
        for (DimenTypes value : values) {
            MakeUtils.makeAll(DESIGN_WIDTH, value, context.getExternalCacheDir()+"/androidui/adapter");
        }

    }
}
