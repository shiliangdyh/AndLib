package com.mirstone.module.transition;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.views.GestureImageView;

import java.util.List;

class PagerAdapter extends RecyclePagerAdapter<PagerAdapter.ViewHolder> {

    private final ViewPager viewPager;
    private final OnPagerItemClickListener onPagerItemClickListener;
    private List<Integer> list;

    public PagerAdapter(ViewPager viewPager, List<Integer> list, OnPagerItemClickListener onPagerItemClickListener) {
        this.viewPager = viewPager;
        this.list = list;
        this.onPagerItemClickListener = onPagerItemClickListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        ViewHolder holder = new ViewHolder(container);
        holder.image.getController().enableScrollInViewPager(viewPager);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setOnClickListener(v -> {
            if (onPagerItemClickListener != null) {
                onPagerItemClickListener.onPagerItemClick(position);
            }
        });
        GlideHelper.loadFull(holder.image, list.get(position), list.get(position));
    }

    static GestureImageView getImageView(RecyclePagerAdapter.ViewHolder holder) {
        return ((ViewHolder) holder).image;
    }


    static class ViewHolder extends RecyclePagerAdapter.ViewHolder {
        final GestureImageView image;

        ViewHolder(ViewGroup container) {
            super(new GestureImageView(container.getContext()));
            image = (GestureImageView) itemView;
        }
    }

    interface OnPagerItemClickListener {
        void onPagerItemClick(int position);
    }
}
