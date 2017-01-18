package com.xiasuhuei321.mrecyclerview;

import android.view.View;

/**
 * Created by xiasuhuei321 on 2017/1/3.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public interface RefreshHeader {
    /**
     * 下拉状态中
     *
     * @param deltaY 偏移量
     */
    void onMove(float deltaY);

    View getContainer();

    void refreshComplete();

    boolean isReleaseToRefresh();

    boolean isRefreshing();

    void onRefresh();

    void smoothScrollTo(int dest);

    int getMeasuredHeight();
}
