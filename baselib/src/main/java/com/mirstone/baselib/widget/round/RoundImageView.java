package com.mirstone.baselib.widget.round;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * @package: com.mirstone.baselib.widget.round
 * @fileName: RoundImageView
 * @data: 2018/8/14 14:52
 * @author: ShiLiang
 * @describe:
 */
public class RoundImageView extends AppCompatImageView {
    private BaseRoundView baseFilletView;

    public RoundImageView(Context context) {
        super(context);
        init(null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        baseFilletView = new BaseRoundView(this, attrs);
    }

    @Override
    public void draw(Canvas canvas) {
        baseFilletView.draw(canvas);
        super.draw(canvas);
        baseFilletView.drawOff(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        baseFilletView.onSizeChanged(w, h);
    }
}
