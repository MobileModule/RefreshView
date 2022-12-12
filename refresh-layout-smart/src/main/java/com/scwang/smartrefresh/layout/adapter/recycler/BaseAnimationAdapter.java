package com.scwang.smartrefresh.layout.adapter.recycler;

import android.animation.Animator;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.adapter.animation.AlphaInAnimation;
import com.scwang.smartrefresh.layout.adapter.animation.BaseAnimation;

import java.util.ArrayList;

public abstract class BaseAnimationAdapter<K extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<K> {

    private boolean openAnimation = false;//是否打开动画
    private BaseAnimation customAnimation = null;//加载动画
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;

    public void openAnimation() {
        this.openAnimation = true;
    }

    public void setAnimation(BaseAnimation animation) {
        customAnimation = animation;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull K holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        addAnimation(holder);
    }

    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (openAnimation) {
            BaseAnimation animation = null;
            if (customAnimation != null) {
                animation = customAnimation;
            } else {
                animation = new AlphaInAnimation();
            }
            for (Animator anim : animation.getAnimators(holder.itemView)) {
                startAnim(anim, holder.getLayoutPosition());
            }
        }
    }

    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

    private FrameLayout mEmptyLayout;
    private boolean mIsUseEmpty = true;

    public void setEmptyView(int layoutResId, ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        setEmptyView(view);
    }

    public void setEmptyView(View emptyView) {
        int oldItemCount = getItemCount();
        boolean insert = false;
        if (mEmptyLayout == null) {
            mEmptyLayout = new FrameLayout(emptyView.getContext());
            final RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);
        mIsUseEmpty = true;
        if (insert && getEmptyViewCount() == 1) {
            int position = 0;
            if (getItemCount() > oldItemCount) {
                notifyItemInserted(position);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public int getEmptyViewCount() {
        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) {
            return 0;
        }
        if (!mIsUseEmpty) {
            return 0;
        }
        if (getItemCount() != 0) {
            return 0;
        }
        return 1;
    }
}
