package com.scwang.smartrefresh.layout.footer;

import android.content.Context;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;
import com.scwang.smartrefresh.layout.util.SmartUtil;

public class ProgressFooter extends InternalAbstract implements RefreshFooter {

    protected boolean mNoMoreData = false;

    public ProgressFooter(Context context) {
        this(context, null);
    }

    private TextView mTitleText;
    private ProgressBar mProgressView;

    public ProgressFooter(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        View.inflate(context, R.layout.srl_progress_footer, this);
        final View thisView = this;
        thisView.setMinimumHeight(SmartUtil.dp2px(40));
        mProgressView = thisView.findViewById(R.id.srl_classics_progress);
        mTitleText = thisView.findViewById(R.id.srl_classics_title);
        if (!thisView.isInEditMode()) {
            mProgressView.setVisibility(INVISIBLE);
        }
    }

    private String no_data = "";

    public ProgressFooter setNoDataTips(String no_data) {
        this.no_data = no_data;
        return this;
    }

    public ProgressFooter setProgressDrawable(Drawable drawable){
        mProgressView.setIndeterminateDrawable(drawable);
        return this;
    }

    private String end_data="";
    public ProgressFooter setEndDataTips(String end_data){
        this.end_data=end_data;
        return this;
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (!mNoMoreData) {
            if (mProgressView.getVisibility() != VISIBLE) {
                mProgressView.setVisibility(VISIBLE);
            }
            super.onStartAnimator(refreshLayout, height, maxDragHeight);
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (!mNoMoreData) {
            mTitleText.setText(success ? getResources().getString(R.string.srl_footer_finish) :
                    getResources().getString(R.string.srl_footer_failed));
            mProgressView.setVisibility(GONE);
            return super.onFinish(layout, success);
        }
        return 0;
    }

    @Override
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
        if (mSpinnerStyle == SpinnerStyle.FixedBehind) {
            super.setPrimaryColors(colors);
        }
    }

    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     */
    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData;
            if (noMoreData) {
                if (no_data.isEmpty()) {
                    mTitleText.setText(getResources().getString(R.string.srl_footer_nothing));
                } else {
                    mTitleText.setText(no_data);
                }
            } else {
                mTitleText.setText(getResources().getString(R.string.srl_footer_loading));
            }
        }
        return true;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState,
                               @NonNull RefreshState newState) {
        if (!mNoMoreData) {
            switch (newState) {
                case None:
                case PullUpToLoad:
                    mTitleText.setText(getResources().getString(R.string.srl_footer_loading));
                    break;
                case Loading:
                case LoadReleased:
                    mTitleText.setText(getResources().getString(R.string.srl_footer_loading));
                    break;
                case ReleaseToLoad:
                    mTitleText.setText(getResources().getString(R.string.srl_footer_loading));
                    break;
                case Refreshing:
                    mTitleText.setText(getResources().getString(R.string.srl_footer_loading));
                    break;
            }
        }
    }

}