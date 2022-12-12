package com.yanzhenjie.recyclerview.swipe;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by druid on 2019/11/19.
 */

public class PullZoomSwipeMenuRecyclerView extends SwipeMenuRecyclerView {
    private static final String TAG = PullZoomSwipeMenuRecyclerView.class.getName();

    public PullZoomSwipeMenuRecyclerView(Context context) {
        this(context, null);
    }

    public PullZoomSwipeMenuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullZoomSwipeMenuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new OnScrollListener() {
            int aa = 0;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                aa += dy;
                Log.i("DY", aa + "");
            }
        });

    }

    private View zoomView;

    public void setZoomView(View view) {
        this.zoomView = view;
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}