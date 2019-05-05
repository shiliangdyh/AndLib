package com.mirstone.module.transition;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker;
import com.mirstone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This example demonstrates images animation from {@link RecyclerView} into {@link ViewPager}.
 */
public class RecyclerToPagerAct extends AppCompatActivity implements PagerAdapter.OnPagerItemClickListener {

    private RecyclerView list;
    private ViewPager pager;
    private View background;

    private PagerAdapter pagerAdapter;
    private ViewsTransitionAnimator<Integer> animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_recycler_screen);

        List<Integer> data = new ArrayList<>();
        data.add(R.drawable.animtor_001);
        data.add(R.drawable.animtor_002);
        data.add(R.drawable.animtor_003);
        data.add(R.drawable.animtor_004);
        data.add(R.drawable.animtor_005);
        data.add(R.drawable.animtor_006);

        // Initializing ListView
        list = findViewById(R.id.recycler_list);
        list.setLayoutManager(new GridLayoutManager(this,3));
        list.setAdapter(new RecyclerAdapter(data, this::onPaintingClick));

        // Initializing ViewPager
        pager = findViewById(R.id.recycler_pager);
        pagerAdapter = new PagerAdapter(pager, data, this);
        pager.setAdapter(pagerAdapter);
        pager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.view_pager_margin));

        // Initializing images animator. It requires us to provide FromTracker and IntoTracker items
        // that are used to find images views for particular item IDs in the list and in the pager
        // to keep them in sync.
        // In this example we will use SimpleTracker which will track images by their positions,
        // if you have a more complicated case see further examples.
        final SimpleTracker listTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int position) {
                RecyclerView.ViewHolder holder = list.findViewHolderForLayoutPosition(position);
                return holder == null ? null : RecyclerAdapter.getImageView(holder);
            }
        };

        final SimpleTracker pagerTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int position) {
                RecyclePagerAdapter.ViewHolder holder = pagerAdapter.getViewHolder(position);
                return holder == null ? null : PagerAdapter.getImageView(holder);
            }
        };

        animator = GestureTransitions.from(list, listTracker).into(pager, pagerTracker);

        // Setting up background animation during image transition
        background = findViewById(R.id.recycler_full_background);
        animator.addPositionUpdateListener((pos, isLeaving) -> applyImageAnimationState(pos));
    }

    @Override
    public void onBackPressed() {
        // We should leave full image mode instead of closing the screen
        if (!animator.isLeaving()) {
            animator.exit(true);
        } else {
            super.onBackPressed();
        }
    }

    private void onPaintingClick(int position) {
        // Animating image transition from given list position into pager
        animator.enter(position, true);
    }

    private void applyImageAnimationState(float position) {
        background.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
        background.setAlpha(position);
    }

    @Override
    public void onPagerItemClick(int position) {
        if (!animator.isLeaving()) {
            animator.exit(true);
        }
    }
}
