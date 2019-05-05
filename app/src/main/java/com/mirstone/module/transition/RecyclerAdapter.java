package com.mirstone.module.transition;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirstone.R;

import java.util.List;


class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Integer> list;
    private final OnPaintingClickListener listener;

    public RecyclerAdapter(List<Integer> list, OnPaintingClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(parent);
        holder.itemView.setOnClickListener(this::onItemClick);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Storing item position for click handler
        holder.itemView.setTag(R.id.tag_item, position);

        GlideHelper.loadThumb(holder.image, list.get(position));
//        GlideApp.with(holder.image).load(painting.thumbId).into(holder.image);

//        CharSequence text = new SpannableBuilder(holder.title.getContext())
//                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
//                .append(painting.author).append("\n")
//                .clearStyle()
//                .append(painting.title)
//                .build();
        holder.title.setText("");
    }

    private void onItemClick(@NonNull View view) {
        int pos = (Integer) view.getTag(R.id.tag_item);
        listener.onPaintingClick(pos);
    }

    static ImageView getImageView(RecyclerView.ViewHolder holder) {
        return ((ViewHolder) holder).image;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView title;

        ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_item, parent, false));
            image = itemView.findViewById(R.id.list_image);
            title = itemView.findViewById(R.id.list_image_title);
        }
    }

    interface OnPaintingClickListener {
        void onPaintingClick(int position);
    }

}
