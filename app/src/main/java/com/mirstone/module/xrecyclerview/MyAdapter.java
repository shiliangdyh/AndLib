package com.mirstone.module.xrecyclerview;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirstone.R;

/**
 * @package: com.mirstone.module.xrecyclerview
 * @fileName: MyAdapter
 * @data: 2018/9/3 15:50
 * @author: ShiLiang
 * @describe:
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyAdapter";
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder " + position);
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @SuppressLint("CutPasteId")
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
