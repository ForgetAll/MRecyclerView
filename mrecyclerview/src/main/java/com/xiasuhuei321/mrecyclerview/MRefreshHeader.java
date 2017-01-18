package com.xiasuhuei321.mrecyclerview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by xiasuhuei321 on 2017/1/3.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 * <p>
 * 一个RefreshHeader接口的实现类
 */

public class MRefreshHeader implements RefreshHeader {
    private final String TAG = getClass().getSimpleName();

    private final LinearLayout mContainer;
    private final ImageView arrowImg;
    private final TextView refreshText;
    private final int mMeasuredHeight;
    private boolean releaseToRefresh = false;
    private boolean rotateDown = true;
    private boolean rotateUp = true;
    private final ImageView refreshImg;
    private String refreshStr;
    private final View rootView;
    private boolean refreshing = false;

    public MRefreshHeader(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.item_refresh_header, new LinearLayout(context), false);
        mContainer = (LinearLayout) rootView.findViewById(R.id.ll_header_content);
        arrowImg = (ImageView) rootView.findViewById(R.id.iv_arrow);
        refreshImg = (ImageView) rootView.findViewById(R.id.iv_refresh);
        refreshText = (TextView) rootView.findViewById(R.id.tv_refresh_text);

        rootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = rootView.getMeasuredHeight();
        Log.e(TAG, "mMeasuredHeight:" + mMeasuredHeight);
        setVisibleHeight(0);
    }

    private int getVisibleHeight() {
        ViewGroup.LayoutParams lp = mContainer.getLayoutParams();
        return lp.height;
    }

    private void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        ViewGroup.LayoutParams lp = mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    @Override
    public void smoothScrollTo(int destHeight) {
        if(destHeight == 0)
            setNormalMode();
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public int getMeasuredHeight() {
        return mMeasuredHeight;
    }

    @Override
    public boolean isReleaseToRefresh() {
        return releaseToRefresh;
    }

    @Override
    public boolean isRefreshing() {
        return refreshing;
    }

    @Override
    public void onMove(float deltaY) {
        setVisibleHeight((int) deltaY + getVisibleHeight());
        if (getVisibleHeight() > mMeasuredHeight) {
            if (!releaseToRefresh) {
                releaseToRefresh = true;
                if (rotateDown) {
                    // 满足释放刷新的条件
                    rotateUp = true;
                    ObjectAnimator rotate =
                            ObjectAnimator.ofFloat(arrowImg, "rotation", 0f, 180f);
                    refreshText.setText("释放刷新");
                    rotate.setDuration(200);
                    rotate.start();
                    rotateDown = false;
                }
            } else {
                releaseToRefresh = false;
            }
        } else {
            if (rotateUp) {
                rotateDown = true;
                ObjectAnimator rotate = ObjectAnimator.ofFloat(arrowImg, "rotation", 180f, 0f);
                rotate.setDuration(200);
                rotate.start();
                rotateUp = false;
            }
        }
    }

    @Override
    public View getContainer() {
        return rootView;
    }

    @Override
    public void onRefresh() {
        refreshing = true;
        smoothScrollTo(mMeasuredHeight);
        arrowImg.setVisibility(View.GONE);
        refreshImg.setVisibility(View.VISIBLE);
        AnimationDrawable drawable =
                (AnimationDrawable) refreshImg.getDrawable();
        drawable.start();
        if (TextUtils.isEmpty(this.refreshStr)) {
            refreshText.setText("刷新中...");
        } else {
            refreshText.setText(this.refreshStr);
        }

    }

    @Override
    public void refreshComplete() {
        refreshing = false;
        if (releaseToRefresh) {
            ObjectAnimator rotate =
                    ObjectAnimator.ofFloat(arrowImg, "rotation", 180f, 0f);
            rotate.setDuration(200);
            rotate.start();
        }
        releaseToRefresh = false;
        refreshText.setText("下拉刷新");

    }

    public void setNormalMode(){
        refreshImg.setVisibility(View.GONE);
        arrowImg.setVisibility(View.VISIBLE);
        refreshText.setText("下拉刷新");
    }

    public void setRefreshText(String refreshStr) {
        this.refreshStr = refreshStr;
    }

}
