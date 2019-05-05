package com.mirstone.module.glide;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @package: com.zhuku.model.http.progress
 * @fileName: GlideProgressResponseBody
 * @data: 2018/8/7 15:57
 * @author: ShiLiang
 * @describe:
 */
public class GlideProgressResponseBody extends ResponseBody {
    private static final String TAG = "XGlide";

    private BufferedSource bufferedSource;

    private ResponseBody responseBody;
    private ProgressListener listener;
    private Handler mainHandler;

    public GlideProgressResponseBody(String url, ResponseBody responseBody) {
        this.responseBody = responseBody;
        listener = ProgressInterceptor.getListener(url);
        mainHandler = new Handler(Looper.getMainLooper());
    }


    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(new ProgressSource(responseBody.source()));
        }
        return bufferedSource;
    }

    private class ProgressSource extends ForwardingSource {
        long totalBytesRead = 0;

        ProgressSource(Source source) {
            super(source);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            final long fullLength = responseBody.contentLength();
            if (bytesRead == -1) {
                totalBytesRead = fullLength;
            } else {
                totalBytesRead += bytesRead;
            }
//            int progress = (int) (100f * totalBytesRead / fullLength);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        //如果fullLength == -1 那么无法知道文件大小
                        listener.onProgress(totalBytesRead, fullLength, totalBytesRead == fullLength || fullLength == -1);
                    }
                    if (listener != null && (totalBytesRead == fullLength || fullLength == -1)) {
                        listener = null;
                    }
                }
            });

            return bytesRead;
        }
    }
}
