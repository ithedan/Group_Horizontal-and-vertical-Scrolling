package com.dengxiao.scroll_viewgroup_library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by DengXiao on 2017/2/20.
 */

public class ScrollGroup extends ViewGroup {
    //是否是水平滚动
    private boolean isHorizontalOrVertical = true;
    //是否添加首位滑动阻尼效果
    private boolean isStartEndScroll = true;
    //用于判断滑动翻页的距离
    private int scrollEdge;
    //记录边距
    private int leftEdge;
    private int rightEdge;
    private int topEdge;
    private int bottomEdge;
    //记录坐标
    private float mLastX = 0;
    private float mLastY = 0;
    private float mLastXIntercept = 0;
    private float mLastYIntercept = 0;
    private Scroller mScroller;
    //记录手指按下触摸事件时的滚动位置用于判断滑动方向
    private int startScrollY;
    private int endScrollY;
    private int startScrollX;
    private int endScrollX;
    //当前Page
    private int currentPage = 0;
    private int scrollXY = 0;
    private onPageChangeListener onPageChangeListener;
    //滚动时间默认800毫秒
    private int duration = 800;
    //当前View的尺寸
    private int mWidth,mHeight;

    public boolean isHorizontalOrVertical() {
        return isHorizontalOrVertical;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontalOrVertical = horizontal;
    }

    public ScrollGroup(Context context) {
        this(context, null);
    }

    public ScrollGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;//判断是否拦截
        float interceptX = ev.getX();//获取X坐标
        float interceptY = ev.getY();//获取Y坐标
        switch (ev.getAction()) {
            //按下时，不拦截，返回false
            case MotionEvent.ACTION_DOWN:
                startScrollX = getScrollX();
                startScrollY = getScrollY();
                scrollXY = 0;
                isIntercept = false;
                break;
            //移动时对滑动进行处理
            case MotionEvent.ACTION_MOVE:
                float deltaX = interceptX - mLastXIntercept;
                float deltaY = interceptY - mLastYIntercept;
                //根据水平距离与垂直距离的大小判断是否是水平滚动或垂直滚动进行事件拦截
                if (isHorizontalOrVertical && Math.abs(deltaX) > Math.abs(deltaY)) {
                    isIntercept = true;
                } else if (!isHorizontalOrVertical && Math.abs(deltaY) > Math.abs(deltaX)) {
                    isIntercept = true;
                } else {
                    isIntercept = false;
                }
                break;
            //抬起手指，不拦截，返回false
            case MotionEvent.ACTION_UP:
                isIntercept = false;
                break;
        }
        mLastX = interceptX;
        mLastY = interceptY;
        mLastXIntercept = interceptX;//记录最后X轴到滑动坐标
        mLastYIntercept = interceptY;//记录最后Y轴到滑动坐标

        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                float scrollByStartX = isHorizontalOrVertical ? touchX - mLastX : 0;
                float scrollByStartY = isHorizontalOrVertical ? 0 : touchY - mLastY;
                //判断左右上下边界,超过边界后设置不可以滑动或者可以滑动
                if (getScrollX() - scrollByStartX < leftEdge && isHorizontalOrVertical) {
                    scrollByStartX = isStartEndScroll ? (touchX - mLastX) / 3 : 0;
                } else if (getScrollX() + getWidth() - scrollByStartX > rightEdge && isHorizontalOrVertical) {
                    scrollByStartX = isStartEndScroll ? (touchX - mLastX) / 3 : 0;
                } else if (getScrollY() - scrollByStartY < topEdge && !isHorizontalOrVertical) {
                    scrollByStartY = isStartEndScroll ? (touchY - mLastY) / 3 : 0;
                } else if (getScrollY() + getHeight() - scrollByStartY > bottomEdge && !isHorizontalOrVertical) {
                    scrollByStartY = isStartEndScroll ? (touchY - mLastY) / 3 : 0;
                }
                scrollBy((int) -scrollByStartX, (int) -scrollByStartY);
                break;
            case MotionEvent.ACTION_UP:
                endScrollX = getScrollX();
                endScrollY = getScrollY();
                currentPage = isHorizontalOrVertical ? getScrollX() / getWidth() : getScrollY() / getHeight();
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                if(scrollEdge==0){
                    //设置滑动边距横向是宽度的1/3，纵向是高度的1/5；
                    scrollEdge = isHorizontalOrVertical ? getWidth() / 3 : getHeight() / 5;
                }
                int edgeX = isScrollPath() ? scrollEdge : getWidth() - scrollEdge;
                int edgeY = isScrollPath() ? scrollEdge : getHeight() - scrollEdge;
                int index = isHorizontalOrVertical ? ((getScrollX() + getWidth() - edgeX) / getWidth()) : ((getScrollY() + getHeight() - edgeY) / getHeight());
                if (index > getChildCount() - 1) {
                    index = getChildCount() - 1;
                }
                int dx = isHorizontalOrVertical ? index * getWidth() - getScrollX() : 0;
                int dy = isHorizontalOrVertical ? 0 : index * getHeight() - getScrollY();
                scrollXY = isHorizontalOrVertical ? dx : dy;
                int scrollX = isHorizontalOrVertical ? getScrollX() : 0;
                int scrollY = isHorizontalOrVertical ? 0 : getScrollY();
                mScroller.startScroll(scrollX, scrollY, dx, dy, duration);
                invalidate();
                break;
        }
        mLastX = touchX;
        mLastY = touchY;
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            //测量每一个子控件到大小
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 判断滑动方向
     *
     * @return
     */
    private boolean isScrollPath() {
        return isHorizontalOrVertical ? endScrollX > startScrollX : endScrollY > startScrollY;
    }


    /**
     * @param changed 参数changed表示view有新的尺寸或位置
     * @param l       参数l表示相对于父view的Left位置
     * @param t       参数t表示相对于父view的Top位置
     * @param r       参数r表示相对于父view的Right位置
     * @param b       参数b表示相对于父view的Bottom位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int left = 0, top = 0, right = 0, bottom = 0;
                if (isHorizontalOrVertical) {
                    left = i * getMeasuredWidth();
                    top = 0;
                    right = i * getMeasuredWidth() + childView.getMeasuredWidth() + getPaddingLeft();
                    bottom = childView.getMeasuredHeight();
                } else {
                    left = 0;
                    top = i * getMeasuredHeight();
                    right = childView.getMeasuredWidth();
                    bottom = i * getMeasuredHeight() + childView.getMeasuredHeight() + getPaddingTop();
                }
                childView.layout(left, top, right, bottom);
            }


            //初始化左右边界
            leftEdge = 0;
            rightEdge = getChildCount() * getMeasuredWidth();
            topEdge = 0;
            bottomEdge = getChildCount() * getMeasuredHeight();


        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        } else {
            if (Math.abs(scrollXY) > scrollEdge && Math.abs(scrollXY) != 0) {
                int width = isHorizontalOrVertical ? getWidth() : getHeight();
                int scroll = isHorizontalOrVertical ? getScrollX() : getScrollY();
                currentPage = scroll / width;
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageChange(currentPage + 1);
                }
            }
        }
    }

    public interface onPageChangeListener {
        void onPageChange(int currentPage);
    }

    public void setOnPageChangeListener(ScrollGroup.onPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     *
     * @param horizontalOrVertical 默认为水平滑动true，false为纵向滑动
     */
    public ScrollGroup setHorizontalOrVertical(boolean horizontalOrVertical) {
        isHorizontalOrVertical = horizontalOrVertical;
        return this;
    }

    /**
     *默认ture,边缘可以有滑动效果，false边缘没有滑动效果
     */
    public ScrollGroup setStartEndScroll(boolean startEndScroll) {
        isStartEndScroll = startEndScroll;
        return this;
    }

    /**
     * 设置滑动翻页边界
     * @param scrollEdge
     */
    public ScrollGroup setScrollEdge(int scrollEdge) {
        this.scrollEdge = scrollEdge;
        return this;
    }

    /**
     *设置滑动时间默认为800毫秒
     */
    public ScrollGroup setDuration(int duration) {
        this.duration = duration;
        return  this;
    }

    public void setInvalidate(){
        invalidate();
    }
}
