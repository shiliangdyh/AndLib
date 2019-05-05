package com.mirstone.module.fileprogress;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @package: com.zhuku.model.http.api
 * @fileName: ProgressResponseBody
 * @data: 2018/8/6 20:55
 * @author: ShiLiang
 * @describe:
 */
public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private ProgressListener listener;
    private BufferedSource bufferedSource;
    private Handler mainHandler;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener listener) {
        this.responseBody = responseBody;
        this.listener = listener;
        mainHandler = new Handler(Looper.getMainLooper());
    }

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
        if (null == bufferedSource) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            long contentLength = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                mainHandler.post(() -> {
                    listener.onProgress(totalBytesRead, contentLength, bytesRead == -1);
                    if (byteCount == -1){
                        listener = null;
                    }
                });
                return bytesRead;
            }
        };
    }
}
