package com.lxj.xrefreshlayout;

import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.lxj.xrefreshlayout.loadinglayout.DefaultLoadingLayout;
import com.lxj.xrefreshlayout.loadinglayout.ILoadingLayout;
import com.lxj.xrefreshlayout.util.DensityUtil;
import com.lxj.xrefreshlayout.util.L;

/**
 * Created by dance on 2017/4/2.
 */

public class XRefreshLayout extends FrameLayout implements NestedScrollingParent {


    private int MIN_LOADING_LAYOUG_HEIGHT = 40;
    private int MAX_LOADING_LAYOUG_HEIGHT;
    private int MAX_DURATION = 2000;
    //range when overscroll header or footer
    private int OVERSCROLL_RANGE = 200;

    private ILoadingLayout loadingLayout;
    private View header;
    private View footer;
    private View refreshView;
    private OverScroller scroller;
    private boolean isNeedInitLoadingLayout = false;

    boolean isRelease = false;
    private boolean isSmoothScrolling = false;

    public XRefreshLayout(Context context) {
        this(context, null);
    }

    public XRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        MIN_LOADING_LAYOUG_HEIGHT = DensityUtil.dip2px(context, MIN_LOADING_LAYOUG_HEIGHT);
        OVERSCROLL_RANGE = DensityUtil.dip2px(context, OVERSCROLL_RANGE);

        scroller = new OverScroller(getContext());
        loadingLayout = new DefaultLoadingLayout();
    }


    protected void initLoadingLayout() {
        if(header!=null)removeView(header);
        if(footer!=null)removeView(footer);

        header = loadingLayout.createLoadingHeader(getContext(), this);
        footer = loadingLayout.createLoadingFooter(getContext(), this);

        addView(header);
        addView(footer);

        //init header and footer view.
        loadingLayout.initAndResetHeader();
        loadingLayout.initAndResetFooter();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new IllegalArgumentException("XRefreshLayout must have only 1 child to pull!");
        }
        refreshView = getChildAt(0);

        //create loading layout:  header and footer
        initLoadingLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MAX_LOADING_LAYOUG_HEIGHT = getMeasuredHeight() / 2;
        measureHeaderAndFooter(widthMeasureSpec, header);
        measureHeaderAndFooter(widthMeasureSpec, footer);

        refreshView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
    }

    /**
     * limit header and footer min-height, max-height
     *
     * @param widthMeasureSpec
     * @param view
     */
    private void measureHeaderAndFooter(int widthMeasureSpec, View view) {
        int height = Math.max(MIN_LOADING_LAYOUG_HEIGHT, view.getMeasuredHeight());
        height = Math.min(height, MAX_LOADING_LAYOUG_HEIGHT);
        view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        header.layout(0, -header.getMeasuredHeight(), header.getMeasuredWidth(), 0);
        refreshView.layout(0, 0, refreshView.getMeasuredWidth(), refreshView.getMeasuredHeight());
        footer.layout(0, refreshView.getBottom(), footer.getMeasuredWidth(), refreshView.getBottom()
                + footer.getMeasuredHeight());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isSmoothScrolling)return true;
        return super.dispatchTouchEvent(ev) ;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        isRelease = false;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        loadingLayout.initAndResetHeader();
        loadingLayout.initAndResetFooter();
        isPullHeader = false;
        isPullFooter = false;
        return true;
    }

    /**
     * when release from XRefreshLayout!
     *
     * @param child
     */
    @Override
    public void onStopNestedScroll(View child) {
        isRelease = true;
        if (isPullHeader) {
            if (getScrollY() <= -header.getMeasuredHeight()) {
                //header shown fully.
                int dy = -header.getMeasuredHeight() - getScrollY();
                smoothScroll(dy);
                loadingLayout.onHeaderRefreshing();
                if (listener != null) {
                    listener.onRefresh();
                }
            } else {
                //hide header smoothly.
                isNeedInitLoadingLayout = true;
                int dy = 0 - getScrollY();
                smoothScroll(dy);

            }
        } else if (isPullFooter) {
            if (getScrollY() >= footer.getMeasuredHeight()) {
                //footer shown fully.
                int dy = footer.getMeasuredHeight() - getScrollY();
                smoothScroll(dy);
                loadingLayout.onFooterRefreshing();
                if (listener != null) {
                    listener.onLoadMore();
                }
            } else {
                //hide footer smoothly.
                isNeedInitLoadingLayout = true;
                int dy = 0 - getScrollY();
                smoothScroll(dy);
            }
        }else {
            //hide footer smoothly.
            isNeedInitLoadingLayout = true;
            int dy = 0 - getScrollY();
            smoothScroll(dy);
        }
    }


    /**
     * smooth scroll to target val.
     *
     * @param dy
     */
    private void smoothScroll(int dy) {
        int duration = calculateDuration(Math.abs(dy));
        scroller.startScroll(0, getScrollY(), 0, dy, duration);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * calculate the duration for animation by dy.
     *
     * @param dy
     * @return
     */
    private int calculateDuration(int dy) {
        float fraction = dy * 1F / MAX_LOADING_LAYOUG_HEIGHT;
        return (int) (fraction * MAX_DURATION);
    }

    boolean isPullHeader, isPullFooter;
    float dy;

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        this.dy = dy;

        isPullHeader = (dy < 0 && getScrollY() <= 0 && !ViewCompat.canScrollVertically(refreshView, -1))
                || (dy >= 0 && getScrollY() < 0);
        isPullFooter = (dy > 0 && !ViewCompat.canScrollVertically(refreshView, 1) && getScrollY() >= 0)
                || (dy < 0 && getScrollY() > 0 && getScrollY() <= getFooterScrollRange() && !isPullHeader);

        if (isPullHeader || isPullFooter) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }

    }

    private int getHeaderScrollRange() {
        return header.getMeasuredHeight() + OVERSCROLL_RANGE;
    }

    private int getFooterScrollRange() {
        return footer.getMeasuredHeight() + OVERSCROLL_RANGE;
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (isPullFooter) return true;
        if (dy < 0 || getScrollY() <= getFooterScrollRange() && getScrollY() >= 0) return false;
        return true;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (isPullHeader) {
            if (y < -getHeaderScrollRange()) {
                y = -getHeaderScrollRange();
            } else if (y > 0) {
                y = 0;
            }

            //call percent
            float percent = Math.abs(y) * 1f / header.getMeasuredHeight();
            percent = Math.min(percent, 1f);
            if(!isRelease){
                loadingLayout.onPullHeader(percent);
            }

        } else if (isPullFooter) {
            if (y > getFooterScrollRange()) {
                y = getFooterScrollRange();
            } else if (y < 0) {
                y = 0;
            }

            //call percent
            float percent = Math.abs(y) * 1f / footer.getMeasuredHeight();
            percent = Math.min(percent, 1f);
            if(!isRelease){
                loadingLayout.onPullFooter(percent);
            }
        }
        super.scrollTo(x, y);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        isSmoothScrolling = isRelease && Math.abs(scroller.getCurrY())>8;
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            //animation finish.
            if (isNeedInitLoadingLayout) {
                L.d("scroll finish, call initAndReset Header!");
                isNeedInitLoadingLayout = false;
                loadingLayout.initAndResetHeader();
                loadingLayout.initAndResetFooter();
            }
        }
    }


    /**
     * set your custom loadinglayout.
     *
     * @param loadingLayout
     */
    public void setLoadingLayout(ILoadingLayout loadingLayout) {
        if(isRelease && isSmoothScrolling)return;
        this.loadingLayout = loadingLayout;
        initLoadingLayout();
        requestLayout();
    }

    /**
     * complete the refresh state!
     */
    public void completeRefresh() {
        isNeedInitLoadingLayout = true;
        smoothScroll(0 - getScrollY());

    }

    private OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }


}
