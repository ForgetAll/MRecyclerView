package com.xiasuhuei321.mrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiasuhuei321 on 2016/12/29.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class MRecyclerView extends RecyclerView {
    private List<View> mHeadViewList;
    private SparseArray<Integer> mHeadViewType;
    private int headPosition = 1;
    // 和XRecyclerView一样的，取一个很大的viewtype的值，避免和用户
    // 的viewtype发生冲突
    private static final int REFRESH_HEADER_TYPE = 100000;
    private static final int HEAD_VIEW_TYPE = 100001;

    private RefreshHeader mRefreshHeader;
    private Context mContext;

    private float mLastY = -1;

    public MRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public MRecyclerView(Context context) {
        this(context, null);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init() {
        mHeadViewList = new ArrayList<>();
        mHeadViewType = new SparseArray<>();
        setRefreshView(null);
    }

    public void addHeadView(View view) {
        mHeadViewList.add(view);
        mHeadViewType.put(HEAD_VIEW_TYPE + headPosition, headPosition);
        headPosition++;
    }

    public void setRefreshView(RefreshHeader refreshHeader) {
        if (refreshHeader == null) {
            this.mRefreshHeader = new MRefreshHeader(mContext);
        } else {
            this.mRefreshHeader = refreshHeader;
        }
    }

    public int getHeadViewCount() {
        return mHeadViewList.size();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        AdapterWrapper adapterWrapper = new AdapterWrapper(adapter);
        super.setAdapter(adapterWrapper);
    }

    private class AdapterWrapper extends Adapter {

        Adapter adapter;

        public AdapterWrapper(Adapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == REFRESH_HEADER_TYPE) {
                return new SimpleViewHolder(mRefreshHeader.getContainer());
            }
            if (viewType > HEAD_VIEW_TYPE) {
                return new SimpleViewHolder(mHeadViewList
                        .get(mHeadViewType.get(viewType)));
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position == 0 || position <= getHeadViewCount()) return;
            // 传入正确的position
            int adjPosition = position - (getHeadViewCount());
            if (adjPosition < adapter.getItemCount())
                adapter.onBindViewHolder(holder, adjPosition);
        }

        @Override
        public int getItemCount() {
            if (adapter != null) return adapter.getItemCount() + 1 + getHeadViewCount();
            else return getHeadViewCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position)) return REFRESH_HEADER_TYPE;
            if (isHeader(position)) return mHeadViewType.get(position);
            return super.getItemViewType(position);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            // 适配不同的LayoutManager
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        public boolean isHeader(int position) {
            return position > 0 && position <= mHeadViewList.size();
        }

        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        class SimpleViewHolder extends ViewHolder {

            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = e.getRawY() - mLastY;
                mLastY = e.getRawY();
                mRefreshHeader.onMove(deltaY / 3);
                break;

            default:
                mLastY = -1;
                if (mRefreshHeader.isReleaseToRefresh()) {
                    if (!mRefreshHeader.isRefreshing())
                        mRefreshHeader.onRefresh();
                } else {
                    if(!mRefreshHeader.isRefreshing()){
                        mRefreshHeader.smoothScrollTo(0);
                    }else{
                        mRefreshHeader.smoothScrollTo(mRefreshHeader.getMeasuredHeight());
                    }

                }
                break;
        }
        return super.onTouchEvent(e);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHeadViewList.clear();
    }
}
