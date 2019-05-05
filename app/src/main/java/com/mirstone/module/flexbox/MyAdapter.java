package com.mirstone.module.flexbox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mirstone.R;

/**
 * @package: com.mirstone.module.flexbox
 * @fileName: MyAdapter
 * @data: 2018/9/6 21:37
 * @author: ShiLiang
 * @describe:
 */
public class MyAdapter extends RecyclerView.Adapter{

    private String[] titles = {
            "看的撒娇覅das",
            "看的撒娇覅fsdadf",
            "看的撒asdlkgfl;kjflsadj娇覅",
            "看的撒vkja娇覅",
            "看覅",
            "看a;l的撒娇覅",
            "看f娇覅",
            "看的pasdofgjdskahjlsajofujiwjhjhfkldsa撒娇覅",
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flexbox,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.textView.setText(titles[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public static class MyHolder extends RecyclerView.ViewHolder{

        private final TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            textView = ((TextView) itemView);
        }
    }
}
