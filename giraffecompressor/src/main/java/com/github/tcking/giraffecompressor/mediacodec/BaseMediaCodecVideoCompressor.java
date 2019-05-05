package com.github.tcking.giraffecompressor.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;

import com.github.tcking.giraffecompressor.GiraffeCompressor;

import java.io.IOException;

/**
 * Created by TangChao on 2017/5/22.
 */

public abstract class BaseMediaCodecVideoCompressor extends GiraffeCompressor {
    static class SampleInfo {
        public int index;
        public MediaCodec.BufferInfo bufferInfo;

        public SampleInfo(int index, MediaCodec.BufferInfo bufferInfo) {
            this.index = index;
            this.bufferInfo = bufferInfo;
        }
    }

    protected MediaCodec encoder;
    protected MediaCodec decoder;
    protected MediaFormat outputVideoMediaFormat;
    protected InputSurface inputSurface;
    protected OutputSurface outputSurface;
    protected MediaMuxer muxer;
    protected TrackInfo trackInfo;
    protected MediaExtractor mediaExtractor;

    protected void initTrackInfo() throws IOException {
        mediaExtractor = new MediaExtractor();
        mediaExtractor.setDataSource(inputFile.getAbsolutePath());
        trackInfo = TrackInfo.from(mediaExtractor);
    }

    protected MediaFormat initOutputVideoMediaFormat(TrackInfo trackInfo) {
        int width = trackInfo.getVideoMediaFormat().getInteger(MediaFormat.KEY_WIDTH);
        int height = trackInfo.getVideoMediaFormat().getInteger(MediaFormat.KEY_HEIGHT);
        int rotation = trackInfo.getVideoMediaFormat().getInteger(MediaFormat.KEY_ROTATION);
        if (rotation == 90 || rotation == 270) {
            int temp = width;
            width = height;
            height = temp;
        }
        MediaFormat format = MediaFormat.createVideoFormat("video/avc", (int) (width * resizeFactor), (int) (height * resizeFactor));
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 29);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        return format;
    }
}
