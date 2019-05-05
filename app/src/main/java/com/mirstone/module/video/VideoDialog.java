package com.mirstone.module.video;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.mirstone.App;
import com.mirstone.R;
import com.mirstone.baselib.dialog.BaseDialogFragment;
import com.mirstone.baselib.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @package: com.mirstone.module.video
 * @fileName: VideoDialog
 * @data: 2018/8/13 21:31
 * @author: ShiLiang
 * @describe:
 */
public class VideoDialog extends BaseDialogFragment {
    private static final String TAG = "VideoDialog";
    private static final int RESQUEST_TAKE_VIDEO = 1000;
    private static final int RESQUEST_SELECT_VIDEO = 1001;
    private Uri capturedUri;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_add_video;
    }

    @Override
    public void bindView(View view) {
        view.findViewById(R.id.btnRecording).setOnClickListener(this::onClick);
        view.findViewById(R.id.btnSelect).setOnClickListener(this::onClick);
        view.findViewById(R.id.btnCancel).setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRecording:
                requestPermission(1);
                break;
            case R.id.btnSelect:
                requestPermission(2);
                break;
            case R.id.btnCancel:
                break;
        }
    }

    private void requestPermission(int type) {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        Observable<Boolean> observable = null;
        if (type == 1) {
            observable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        } else {
            observable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        Disposable subscribe = observable
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        if (type == 1) {
                            dispatchTakeVideoIntent();
                        } else {
                            dispatchSelectVideoIntent();
                        }
                    } else {
                        ToastUtil.INSTANCE.show(App.app, "无权限");
                    }
                    dismiss();
                });
    }

    private void dispatchSelectVideoIntent() {
        Intent intent = new Intent();
        intent.setType("video/*");  //选择视频 （mp4 3gp 是android支持的视频格式）
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(intent, RESQUEST_SELECT_VIDEO);
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(App.app.getPackageManager()) != null) {
            try {

                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 100);
                capturedUri = Uri.fromFile(createMediaFile());
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);
                getActivity().startActivityForResult(takeVideoIntent, RESQUEST_TAKE_VIDEO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File createMediaFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        String fileName = "VID_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

        return File.createTempFile(
                fileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == RESQUEST_TAKE_VIDEO || requestCode == RESQUEST_SELECT_VIDEO) && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                String inputPath = null;
                try {
                    inputPath = Util.getFilePath(App.app, data.getData());
                    if (callBack != null) {
                        callBack.onSuccess(inputPath);
                    }
                    if (requestCode == RESQUEST_TAKE_VIDEO) {
//                        updateVideo(inputPath);
                    }
                    Log.d(TAG, "video path: " + inputPath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   public interface CallBack{
        void onSuccess(String videoPath);
        void onFailed(String error);
    }

    /**
     * 将视频插入图库
     *
     * @param url 视频路径地址
     */
    public void updateVideo(String url) {
        File file = new File(url);
        //获取ContentResolve对象，来操作插入视频
        ContentResolver localContentResolver = App.app.getContentResolver();
        //ContentValues：用于储存一些基本类型的键值对
        ContentValues localContentValues = getVideoContentValues(getActivity(), file, System.currentTimeMillis());
        //insert语句负责插入一条新的纪录，如果插入成功则会返回这条记录的id，如果插入失败会返回-1。
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
    }

    //再往数据库中插入数据的时候将，将要插入的值都放到一个ContentValues的实例当中
    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/3gp");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }
}
