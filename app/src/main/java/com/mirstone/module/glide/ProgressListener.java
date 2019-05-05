package com.mirstone.module.glide;

/**
 * @package: com.zhuku.model.http.progress
 * @fileName: ProgressListener
 * @data: 2018/8/6 20:57
 * @author: ShiLiang
 * @describe:
 */
public interface ProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
