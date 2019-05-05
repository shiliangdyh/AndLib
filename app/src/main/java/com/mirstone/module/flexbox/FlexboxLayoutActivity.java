package com.mirstone.module.flexbox;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.mirstone.R;
import com.mirstone.baselib.base.BaseSwipeBackActivity;

import org.jetbrains.annotations.Nullable;

/**
 * @package: com.mirstone.module.flexbox
 * @fileName: FlexboxLayoutActivity
 * @data: 2018/9/6 21:35
 * @author: ShiLiang
 * @describe:
 */
public class FlexboxLayoutActivity extends BaseSwipeBackActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexbox);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setAlignItems(AlignItems.STRETCH);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new MyAdapter());
    }

    @Override
    protected int getStateBarColor() {
        return Color.GREEN;
    }
}
