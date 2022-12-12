package com.scwang.smartrefresh.layout.header;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;
import com.scwang.smartrefresh.layout.util.SmartUtil;

public class AnimationHeader extends InternalAbstract implements RefreshHeader {

    public AnimationHeader(Context context) {
        this(context, null);
    }

    public AnimationHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        initView(context);
    }

    private TextView mTitleText;
    private ImageView mProgressView;

    protected String mTextPulling;//"下拉可以刷新";
    protected String mTextRefreshing;//"正在刷新...";
    protected String mTextLoading;//"正在加载...";
    protected String mTextRelease;//"释放立即刷新";
    protected String mTextFinish;//"刷新完成";
    protected String mTextFailed;//"刷新失败";

    private void initView(Context context) {
        View.inflate(context, R.layout.srl_anim_header, this);
        final View thisView = this;
        thisView.setMinimumHeight(SmartUtil.dp2px(70));
        mProgressView = thisView.findViewById(R.id.srl_classics_progress);
        mProgressView.setImageResource(R.drawable.anim_header);
        mTitleText = thisView.findViewById(R.id.srl_classics_title);
        //
        mTextPulling = context.getString(R.string.srl_header_pulling);
        mTextRefreshing = context.getString(R.string.srl_header_refreshing);
        mTextLoading = context.getString(R.string.srl_header_loading);
        mTextRelease = context.getString(R.string.srl_header_release);
        mTextFinish = context.getString(R.string.srl_header_finish);
        mTextFailed = context.getString(R.string.srl_header_failed);
    }

    public void initView(Context context, int color) {
        initView(context);
        mTitleText.setTextColor(color);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        super.onStartAnimator(refreshLayout, height, maxDragHeight);
    }

    @Override
    public void onReleased(@NonNull final RefreshLayout refreshLayout, int height, int maxDragHeight) {
        AnimationDrawable animationDrawable = (AnimationDrawable) mProgressView.getDrawable();
        animationDrawable.start();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (success) {
            mTitleText.setText(mTextFinish);
        } else {
            mTitleText.setText(mTextFailed);
        }
        AnimationDrawable animationDrawable = (AnimationDrawable) mProgressView.getDrawable();
        animationDrawable.stop();
        return super.onFinish(layout, success);//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout,
                               @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mTitleText.setText(mTextPulling);
                break;
            case Refreshing:
            case RefreshReleased:
                mTitleText.setText(mTextRefreshing);
                break;
            case ReleaseToRefresh:
                mTitleText.setText(mTextRelease);
                break;
            case ReleaseToTwoLevel:
                break;
            case Loading:
                mTitleText.setText(mTextLoading);
                break;
        }
    }

}
