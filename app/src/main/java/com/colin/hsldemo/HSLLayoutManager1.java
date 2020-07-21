package com.colin.hsldemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * create by colin
 * 2020/6/1
 */
public class HSLLayoutManager1 extends RecyclerView.LayoutManager {

    private int midMargin; //中间的margin
    private int itemWidth;
    private int itemHeight;
    private int mScrollOffset = 0; //竖直方向总的偏移量。
    private int mFirstPosition; //当前第一个可见的item position
    private int mLastPosition; //当前layout最后一个的item position
    private int mMidPosition; //当前处于中间位置的item position
    private int totalHeight; // itemCount * itemHeight
    private float scale = 1.25f;
    private int realSize; //真是数据的个数。
    private SelectMidListener listener;

    HSLLayoutManager1(Context context) {
        midMargin = context.getResources().getDimensionPixelSize(R.dimen.hsl_mid_margin);
        itemWidth = context.getResources().getDimensionPixelSize(R.dimen.hsl_item_width);
        itemHeight = context.getResources().getDimensionPixelSize(R.dimen.hsl_item_height);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            //没有Item可布局，就回收全部临时缓存 (参考自带的LinearLayoutManager)
            //这里的没有Item，是指Adapter里面的数据集，
            //可能临时被清空了，但不确定何时还会继续添加回来
            removeAndRecycleAllViews(recycler);
            return;
        }
        totalHeight = (int) ((getItemCount() - 1) * itemHeight + (itemHeight * scale) + midMargin * 2);
        logInfo("onLayoutChildren , totalHeight = " + totalHeight);
        //暂时分离和回收全部有效的Item
        detachAndScrapAttachedViews(recycler);
        //计算需要布局的item个数
        fill(recycler, state, 0);
    }

    private int getRecyclerViewVerSpace() {
        return totalHeight - getHeight() - getPaddingBottom();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        mScrollOffset += dy;
        int fill = fill(recycler, state, dy);
        logInfo("child count = " + getChildCount() + ", cache size = " + recycler.getScrapList().size());
        return fill;
    }

    private int fill(RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int dy) {
        if (state.isPreLayout())
            return 0;
        //计算需要布局的item个数
        int resultDy = dy;
        //上边界。
        if (dy < 0)
            if (mScrollOffset < 0) {
                mScrollOffset = resultDy = 0;
            }
        //下边界
        if (dy > 0)
            if (mScrollOffset > getRecyclerViewVerSpace()) {
                mScrollOffset -= dy;
                resultDy = mScrollOffset - getRecyclerViewVerSpace();
                mScrollOffset = getRecyclerViewVerSpace();
            }
        logInfo("mScrollOffset = " + mScrollOffset + ", resultDy = " + resultDy + ",dy = " + dy);
        //计算 first 与 last
        mFirstPosition = mScrollOffset / itemHeight;
        // 多layout一个，保证滑动的时候视觉连贯性。
        mLastPosition = mFirstPosition + realSize + 1;
        if (mLastPosition > getItemCount())
            mLastPosition = getItemCount();
        int offset = mScrollOffset % itemHeight;
        int left = getPaddingLeft();
        int right = left + itemWidth;
        int warpHeight = getPaddingTop() - offset;
        int mid = (mFirstPosition + mLastPosition) / 2;
        mMidPosition = mid;
        logInfo("onLayout mFirstPosition = " + mFirstPosition + ", mLastPosition = " + mLastPosition);
        for (int i = mFirstPosition; i < mLastPosition; i++) {
            View viewForPosition = recycler.getViewForPosition(i);
            addView(viewForPosition);
            measureChild(viewForPosition, 0, 0);
            int margin = 0;
            int childHeight;
            viewForPosition.setPivotX(itemWidth / 2f);
            viewForPosition.setPivotY(0f);
            if (i == mid) {
                margin = midMargin;
                childHeight = (int) (itemHeight * scale);
                viewForPosition.setScaleX(scale);
                viewForPosition.setScaleY(scale);
            } else {
                childHeight = itemHeight;
                viewForPosition.setScaleX(1f);
                viewForPosition.setScaleY(1f);
            }

            int top = warpHeight + margin;
            int bottom = top + childHeight;
            layoutDecorated(viewForPosition, left, top, right, bottom);
            warpHeight += (childHeight + margin * 2);
        }

        //遍历，然后先移除，后回收，其实也就是removeAndRecycleView方法所做的事
        List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
        for (int i = 0; i < scrapList.size(); i++) {
            RecyclerView.ViewHolder holder = scrapList.get(i);
            removeView(holder.itemView);
            recycler.recycleView(holder.itemView);
        }
        if (listener != null) {
            listener.onSelected(mid);
        }
        return resultDy;
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            //保证中间放大的view滑动到中间，
            logInfo("first = " + mFirstPosition + ",mid = " + mMidPosition + ",last = " + mLastPosition);
            View view = findViewByPosition(mMidPosition);
            if (view == null)
                return;
            int viewMid = (view.getTop() + view.getBottom()) / 2;
            int rvMid = getHeight() / 2;
            int offset = -(rvMid - viewMid);
            final int lastScrollOffset = mScrollOffset;
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, offset);
            valueAnimator.setDuration(50);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    animatorOffset(valueAnimator, lastScrollOffset);
                }
            });
            valueAnimator.start();

        }
    }

    @Override
    public void onMeasure(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        int resultWidthSpec = widthSpec;
        int mode = View.MeasureSpec.getMode(resultWidthSpec);
        if (mode == View.MeasureSpec.AT_MOST) {
            resultWidthSpec = View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY);
        }
        super.onMeasure(recycler, state, resultWidthSpec, heightSpec);
    }

    @Override
    public void scrollToPosition(int position) {
        int offset = position * itemHeight - Math.abs(mScrollOffset);
        if (offset == 0)
            return;
        mScrollOffset += offset;
        requestLayout();
    }

    /**
     * 将position平滑滑动到中间。
     *
     * @param position 索引
     */
    void smoothScrollToPositionMid(int position) {
        if (position > (getItemCount() - realSize / 2))
            return;
        int offset = (position - mMidPosition) * itemHeight;
        if (offset == 0)
            return;
        long duration;
        if (Math.abs(position - mMidPosition) > realSize)
            duration = 400;
        else
            duration = 200;
        final int lastScrollOffset = mScrollOffset;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, offset);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatorOffset(valueAnimator, lastScrollOffset);
            }
        });
        valueAnimator.start();
    }

    //将position滑动到中间。
    private void scrollToPositionMid(int position) {
        if (position > (getItemCount() - realSize / 2))
            return;
        int offset = (position - mMidPosition) * itemHeight;
        if (offset == 0)
            return;
        mScrollOffset += offset;
        logInfo("scrollToPositionMid , position = " + position + ",mMidPosition = " + mMidPosition + ",offset = " + offset);
        requestLayout();
    }

    private void animatorOffset(@NonNull ValueAnimator valueAnimator, int lastScrollOffset) {
        Integer animatedValue = (Integer) valueAnimator.getAnimatedValue();
        mScrollOffset = lastScrollOffset + animatedValue;
        requestLayout();
    }

    void setRealSize(int realSize) {
        this.realSize = realSize;
        int itemCount = getItemCount();
        int targetMid = itemCount / 2;
        mScrollOffset += itemHeight / 2;
        int targetPosition;
        int mu = targetMid % realSize;
        //这个时候因为还没进行第一次布局。所以 first，mid，last，offset 都未0
        //这里就手动减去第一次布局的mid 为 realSize / 2 个item
        targetPosition = targetMid - mu - realSize / 2;
        scrollToPositionMid(targetPosition);
    }

    void setSelectMidListener(SelectMidListener listener) {
        this.listener = listener;
    }

    private void logInfo(String info) {
        Log.i("TestLayoutManager", info);
    }

    public interface SelectMidListener {

        /**
         * @param position 当停下的时候选中的中间的 position。
         *                 这个position会比data的size大。因此真正使用的时候要模除data.size
         */
        void onSelected(int position);
    }

}
