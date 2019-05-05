package com.jcodecraeer.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * @package: com.zhuku.widget
 * @fileName: ListenerXRecyclerView
 * @data: 2018/9/5 20:27
 * @author: ShiLiang
 * @describe: 实现了item的点击事件、长按事件
 */
public class ListenerXRecyclerView extends XRecyclerView implements RecyclerView.OnChildAttachStateChangeListener {
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private OnRecyclerItemLongClickListener onRecyclerItemLongClickListener;

    public OnRecyclerItemClickListener getOnRecyclerItemClickListener() {
        return onRecyclerItemClickListener;
    }

    public OnRecyclerItemLongClickListener getOnRecyclerItemLongClickListener() {
        return onRecyclerItemLongClickListener;
    }

    public void setOnRecyclerItemLongClickListener(OnRecyclerItemLongClickListener onRecyclerItemLongClickListener) {
        this.onRecyclerItemLongClickListener = onRecyclerItemLongClickListener;
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public ListenerXRecyclerView(Context context) {
        super(context);
        init();
    }

    public ListenerXRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListenerXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnChildAttachStateChangeListener(this);
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        if (onRecyclerItemClickListener == null && onRecyclerItemLongClickListener == null) {
            return;
        }
        final int position = getChildAdapterPosition(view) - getHeaders_includingRefreshCount();
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerItemClickListener != null) {
                    onRecyclerItemClickListener.onRecyclerItemClick(position);
                }
            }
        });
        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerItemLongClickListener != null) {
                    onRecyclerItemLongClickListener.onRecyclerItemLongClick(position);
                }
                return true;
            }
        });
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        if (onRecyclerItemClickListener == null && onRecyclerItemLongClickListener == null) {
            return;
        }
        view.setOnClickListener(null);
        view.setOnLongClickListener(null);
    }

    public interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(int position);
    }

    public interface OnRecyclerItemLongClickListener {
        void onRecyclerItemLongClick(int position);
    }
}
