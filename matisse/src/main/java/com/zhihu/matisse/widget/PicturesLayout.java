package com.zhihu.matisse.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mirstone.baselib.R;
import com.mirstone.baselib.util.CollectionUtil;
import com.mirstone.baselib.util.ScreenUtil;
import com.mirstone.baselib.util.ToastUtil;

import java.util.ArrayList;

import byc.imagewatcher.ImageWatcher;


public class PicturesLayout extends FrameLayout /*implements ImageWatcher.OnPictureLongPressListener*/ {
    private static final String TAG = "PicturesLayout";
    public static final int MAX_DISPLAY_COUNT = 9;
    private CallBack onAddClickListener;
    private boolean showMode;

    private final FrameLayout.LayoutParams lpChildImage = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private int space;
    private int deleteSize;
    private ImageView ivAdd;

    private ArrayList<ImageView> imageGroupList = new ArrayList<>();
    private ArrayList<String> picturePaths = new ArrayList<>();
    private int imageSize;

    private ImageWatcher vImageWatcher;
    private Bitmap currentBitmap;

    public void setShowMode(boolean showMode) {
        this.showMode = showMode;
    }

    public void setOnAddClickListener(CallBack onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public PicturesLayout(@NonNull Context context) {
        this(context, null);
    }

    public PicturesLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicturesLayout(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        post(new Runnable() {
            @Override
            public void run() {
                init(context, attrs, defStyleAttr);
                initImageWatcher();
            }
        });
    }

    private void initImageWatcher() {
        vImageWatcher = ImageWatcher.Helper.with((Activity) getContext()) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setTranslucentStatus(0) // 如果是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
//                .setErrorImageRes(R.mipmap.error_picture) // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
                .setHintMode(ImageWatcher.POINT)//设置指示器（默认小白点）
                .setHintColor(getResources().getColor(android.R.color.holo_red_light), getResources().getColor(android.R.color.white))//设置指示器颜色
                .setOnPictureLongPressListener(new ImageWatcher.OnPictureLongPressListener() {
                    @Override
                    public void onPictureLongPress(ImageView v, String url, int pos) {
                        ToastUtil.INSTANCE.show(getContext(), "long click");
                    }
                }) // 长按图片的回调，你可以显示一个框继续提供一些复制，发送等功能
                .setLoader(new ImageWatcher.Loader() {//调用show方法前，请先调用setLoader 给ImageWatcher提供加载图片的实现
                    @Override
                    public void load(Context context, String url, final ImageWatcher.LoadCallback lc) {
                        int width = ScreenUtil.INSTANCE.getDeviceWidth(getContext());
                        int height = ScreenUtil.INSTANCE.getDeviceHeight(getContext());
                        Glide.with(context)
                                .asBitmap()
                                .load(url)
                                .apply(new RequestOptions()
                                        .override(width, height)
                                        .priority(Priority.HIGH)
                                        .fitCenter())
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        lc.onResourceReady(resource);
                                    }

                                    @Override
                                    public void onLoadStarted(Drawable placeholder) {
                                        lc.onLoadStarted(new BitmapDrawable(currentBitmap));
                                        lc.onResourceReady(currentBitmap);
                                    }

                                    @Override
                                    public void onLoadFailed(Drawable errorDrawable) {
                                        lc.onLoadFailed(errorDrawable);
                                    }
                                });
                    }
                })
                .create();
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Log.d(TAG, "init" + getWidth());
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        space = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mDisplayMetrics) + 0.5f);
        deleteSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, mDisplayMetrics) + 0.5f);
        imageSize = (getWidth() - space * 2) / 3;
        lpChildImage.width = imageSize;
        if (!this.showMode) {
            initAddButton(context);
        }
    }

    private void initAddButton(Context context) {
        ivAdd = new SquareImageView(context);
        ivAdd.setBackgroundResource(R.drawable.default_picture);
        ivAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddClickListener != null) {
                    onAddClickListener.addPicture();
                }
            }
        });
        ivAdd.setImageResource(R.drawable.ic_add);
        ivAdd.setScaleType(ImageView.ScaleType.CENTER);
        ivAdd.setLayoutParams(lpChildImage);
        ivAdd.setTranslationX(0);
        ivAdd.setTranslationX(0);
        addView(ivAdd);
    }

    public void onSelectPictures(ArrayList<String> list) {
        removeAllViews();
        addView(ivAdd);
        imageGroupList.clear();
        picturePaths.clear();
        if (CollectionUtil.isNotEmpty(list)) {
            picturePaths.addAll(list);
        }
        notifyDataChanged();
    }

    public ArrayList<String> getSelectPictures(){
        return picturePaths;
    }

    private void notifyDataChanged() {
        final ArrayList<String> pictureUrls = this.picturePaths;
        if (CollectionUtil.isNotEmpty(pictureUrls)) {
            int size = pictureUrls.size();
            int totalSize = showMode ? size : size + 1;
            int row = 0;
            if (totalSize > 6) {
                row = 3;
            } else if (totalSize > 3) {
                row = 2;
            } else if (totalSize > 0) {
                row = 1;
            }
            for (int i = 0; i < size; i++) {
                String url = pictureUrls.get(i);
                final ImageView imageView = new SquareImageView(getContext());
                imageGroupList.add(imageView);
                imageView.setLayoutParams(lpChildImage);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(imageView).asBitmap().load(url).apply(new RequestOptions().override(imageSize, imageSize)).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                    }
                });
                imageView.setBackgroundResource(R.drawable.default_picture);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView iv = (ImageView) v;
                        currentBitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                        vImageWatcher.show(iv, imageGroupList, picturePaths);
                    }
                });
                imageView.setTranslationX((i % 3) * (imageSize + space));
                imageView.setTranslationY((i / 3) * (imageSize + space));
                addView(imageView);
                if (!showMode) {
                    ImageView ivDelete = new ImageView(getContext());
                    ivDelete.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    ivDelete.setPadding(10, 10, 10, 10);
                    ivDelete.setLayoutParams(new ViewGroup.LayoutParams(deleteSize, deleteSize));
                    ivDelete.setImageResource(R.drawable.ic_delete);
                    final int finalI = i;
                    ivDelete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pictureUrls.remove(finalI);
                            ArrayList<String> list = new ArrayList<>(pictureUrls);
                            onSelectPictures(list);
                            if (onAddClickListener != null) {
                                onAddClickListener.onDelete(finalI);
                            }
                        }
                    });
                    ivDelete.setTranslationX((i % 3) * (imageSize + space) + imageSize - deleteSize);
                    ivDelete.setTranslationY((i / 3) * (imageSize + space));
                    addView(ivDelete);
                }
            }
            if (!showMode) {
                if (size >= 9) {
                    ivAdd.setVisibility(GONE);
                } else {
                    ivAdd.setVisibility(VISIBLE);
                    ivAdd.setTranslationX((size % 3) * (imageSize + space));
                    ivAdd.setTranslationY((size / 3) * (imageSize + space));
                }
            }
            getLayoutParams().height = imageSize * row + space * (row - 1);
        }
    }

    public boolean handleBackPressed() {
        return vImageWatcher != null && vImageWatcher.handleBackPressed();
    }

//    @Override
//    public void onPictureLongPress(ImageView v, String url, int pos) {
//
//    }

    public interface CallBack {
        void addPicture();
        void onDelete(int position);
    }
}
