package com.mirstone.module.fileprogress;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * @package: com.zhuku.model.http.progress
 * @fileName: ProgressRequestBody
 * @data: 2018/8/7 11:39
 * @author: ShiLiang
 * @describe:
 */
public class ProgressRequestBody extends RequestBody {
    private static final String TAG = "ProgressRequestBody";
    private static final int SEGMENT_SIZE = 2 * 1024; // okio.Segment.SIZE

    protected File file;
    private ProgressListener listener;
    private String contentType;
    private Handler mainHandler;

    public ProgressRequestBody(File file, String contentType, ProgressListener listener) {
        this.file = file;
        this.contentType = contentType;
        this.listener = listener;
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        long contentLength = contentLength();
        try {
            source = Okio.source(file);
            long total = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                long totalWrite = total;
                sink.flush();
                mainHandler.post(() -> listener.onProgress(totalWrite, contentLength, totalWrite == contentLength));

            }
        } finally {
            Util.closeQuietly(source);
        }
    }
}
