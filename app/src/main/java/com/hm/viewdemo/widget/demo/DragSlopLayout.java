package com.hm.viewdemo.widget.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.hm.viewdemo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by long on 2016/9/6.
 * 实现拖拽出界外的布局
 */
public class DragSlopLayout extends FrameLayout {

    public static final int MODE_DRAG = 1;
    // 展开
    static final int STATUS_EXPANDED = 1001;
    // 收缩
    public static final int STATUS_COLLAPSED = 1002;
    // 退出
    static final int STATUS_EXIT = 1003;
    // 滚动
    static final int STATUS_SCROLL = 1004;

    // ViewDragHelper 的敏感度
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    // 判断快速滑动的速率
    private static final float FLING_VELOCITY = 5000;
    // 下坠动画时间
    private static final int FALL_BOUND_DURATION = 1000;

    // 模式
    private int mMode;
    // 固定高度
    private int mFixHeight;
    // 最大高度
    private int mMaxHeight;
    // 折叠系数,1.0最大表示完全折叠，mMainView 视图不动；效果同 CollapsingToolbarLayout
    private float mCollapseParallax = 1.0f;
    // 整个布局高度
    private int mHeight;
    // 拖拽模式的临界Top值
    private int mCriticalTop;
    // 拖拽模式的展开状态Top值
    private int mExpandedTop;
    // 拖拽模式的收缩状态Top值
    private int mCollapsedTop;
    // 是否处于拖拽状态
    private boolean mIsDrag = false;
    // 拖拽状态
    @DragStatus
    private int mDragStatus = STATUS_EXPANDED;
    // 是否有 post 显示动画 Runnable
    private boolean mHasShowRunnable = false;

    // 布局的第1个子视图
    private View mMainView;
    // 可拖拽的视图，为布局的第2个子视图
    private View mDragView;
    // 拖拽帮助类
    private ViewDragHelper mDragHelper;
    // 下坠滚动辅助类
    private ScrollerCompat mFallBoundScroller;
    // 回升滚动辅助类
    private ScrollerCompat mDecelerateScroller;
    // ViewPager 监听器
    private ViewPager.OnPageChangeListener mViewPagerListener;
    // 和 ViewPager 联动时的自动执行进入动画的延迟时间，注意和动画的启动延迟时间区分
    private int mAutoAnimateDelay = 1000;
    // 是否手动执行了退出动画，如果为真则不再执行自动进入动画
    private boolean mIsDoOutAnim = false;
    // 是否设置自定义动画，true 的话关闭 ViewPager 联动动画
    private boolean mIsCustomAnimator = false;
    // 关联的 ScrollView，实现垂直方向的平滑滚动
    private View mAttachScrollView;
    // 关联的 ViewPager，实现联动
    private ViewPager mAttachViewPager;
    // 手势控制
    private GestureDetector mGestureDetector;
    // DragView的Top属性值
    private int mDragViewTop = 0;


    public void setmDragStatus(int mDragStatus) {
        this.mDragStatus = mDragStatus;
    }

    public DragSlopLayout(Context context) {
        this(context, null);
    }

    public DragSlopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DragSlopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mDragHelper = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, callback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
        mGestureDetector = new GestureDetector(context, mGestureListener);
        mFallBoundScroller = ScrollerCompat.create(context, new BounceInterpolator());
        mDecelerateScroller = ScrollerCompat.create(context, new DecelerateInterpolator());
        mMinTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DragSlopLayout, defStyleAttr, 0);
        mFixHeight = a.getDimensionPixelOffset(R.styleable.DragSlopLayout_fix_height, mFixHeight);
        mMaxHeight = a.getDimensionPixelOffset(R.styleable.DragSlopLayout_max_height, 0);
        a.recycle();
        mMode = MODE_DRAG;
        mDragStatus = STATUS_COLLAPSED;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount < 2) {
            // DragLayout 必须包含两个子视图，第一个为主视图，另一个为可拖拽的视图
            throw new IllegalArgumentException("DragLayout must contains two sub-views.");
        }

        mMainView = getChildAt(0);
        mDragView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMode == MODE_DRAG) {
            if (mMaxHeight == 0) {
                // 未设置最大高度则为布局高度的 2/3
                mMaxHeight = getMeasuredHeight() * 2 / 3;
            } else if (mMaxHeight > getMeasuredHeight()) {
                // MODE_DRAG 模式最大高度不超过布局高度
                mMaxHeight = getMeasuredHeight();
            }
            View childView = getChildAt(1);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            // 限定视图的最大高度
            if (childHeight > mMaxHeight) {
                childView.measure(MeasureSpec.makeMeasureSpec(childWidth - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(mMaxHeight - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY));
            }
            //限定视图最小高度
            if (childHeight < mFixHeight) {
                childView.measure(MeasureSpec.makeMeasureSpec(childWidth - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(mFixHeight - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View childView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
        int childWidth = childView.getMeasuredWidth();
        int childHeight = childView.getMeasuredHeight();
        mCriticalTop = b - (childHeight - mFixHeight) / 2 - mFixHeight;
        mExpandedTop = b - childHeight;
        mCollapsedTop = b - mFixHeight;
            if (mDragStatus == STATUS_EXIT) {
                // 对于 ViewPager 换页后会回调 onLayout()，需要进行处理
                if (mMode == MODE_DRAG) {
                    mDragViewTop = b;
                } else {
                    mDragViewTop = b - mFixHeight;
                    childView.setTranslationY(childHeight);
                }
            } else if (mDragStatus == STATUS_COLLAPSED) {
                mDragViewTop = b - mFixHeight;
            } else if (mDragStatus == STATUS_EXPANDED) {
                //初始化的时候是这个状态
                mDragViewTop = b - childHeight;
            } else {
                mDragViewTop = b - mFixHeight;
            }
        childView.layout(lp.leftMargin, mDragViewTop, lp.leftMargin + childWidth, mDragViewTop + childHeight);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAllScroller();
    }

    /***********************************
     * ViewDragHelper
     ********************************************/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsDrag = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 调用父类的方法，避免可能出现的 IllegalArgumentException: pointerIndex out of range
        super.onInterceptTouchEvent(ev);
        boolean isIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        if (isNeedIntercept(ev)) {
            isIntercept = true;
        } else if (mDragHelper.isViewUnder(mDragView, (int) ev.getX(), (int) ev.getY())) {
            // 处于拖拽模式且点击到拖拽视图则停止滚动
            stopAllScroller();
        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN && (
                mDragHelper.isViewUnder(mDragView, (int) event.getX(), (int) event.getY()))) {
            // 处理一些点击事件没被消费的情况
            stopAllScroller();
            mDragHelper.captureChildView(mDragView, 0);
            mIsDrag = true;
        } else {
            mDragHelper.processTouchEvent(event);
        }
        return mIsDrag;
    }

    /**
     * 手势监听
     */
    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        // 是否是按下的标识，默认为其他动作，true为按下标识，false为其他动作
        private boolean isDownTouch;

        @Override
        public boolean onDown(MotionEvent e) {
            isDownTouch = true;
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isDownTouch) {
                // 如果为上下滑动则控制拖拽
                if (Math.abs(distanceY) > Math.abs(distanceX)) {
                    stopAllScroller();
                    mDragHelper.captureChildView(mDragView, 0);
                    mIsDrag = true;
                }
                isDownTouch = false;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };


    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        private static final float SCROLL_DURATION = 0.3f;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            mIsDrag = child == mDragView;
            return mIsDrag;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            float velocity = FLING_VELOCITY / 2;
            if (Math.abs(yvel) < velocity) {
                if (mDragView.getTop() > mCriticalTop) {
                    if (mDragStatus == STATUS_EXPANDED) {
                        mDragHelper.smoothSlideViewTo(mDragView, 0, mCollapsedTop);
                    } else {
                        mFallBoundScroller.startScroll(0, mDragView.getTop(), 0, mCollapsedTop - mDragView.getTop(),
                                FALL_BOUND_DURATION);
                    }
                } else {
                    mDragHelper.smoothSlideViewTo(mDragView, 0, mExpandedTop);
                }
                ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
            } else if (yvel > 0) {
                if (!_flingScrollView(yvel)) {
                    mDragHelper.settleCapturedViewAt(0, mCollapsedTop);
                    ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
                }
            } else {
                if (!_flingScrollView(yvel)) {
                    mDragHelper.settleCapturedViewAt(0, mExpandedTop);
                    ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
                }
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (state == ViewDragHelper.STATE_IDLE) {
                switchStatus();
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            final float percent = (mCollapsedTop - top) * 1.0f / (mCollapsedTop - mExpandedTop);
            dragPositionChanged(mHeight - top, percent);
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (mAttachScrollView != null) {
                if (mAttachScrollView.getScrollY() > 0 || (mDragView.getTop() == mExpandedTop && dy < 0)) {
                    mAttachScrollView.scrollBy(0, -dy);
                    return mExpandedTop;
                }
            }
            int newTop = Math.max(mExpandedTop, top);
            newTop = Math.min(mCollapsedTop, newTop);
            return newTop;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return 0;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return child == mDragView ? child.getHeight() : 0;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mDragHelper.captureChildView(mDragView, pointerId);
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true) ||
                _continueSettling(mFallBoundScroller) || _continueSettling(mDecelerateScroller)) {
            mDragStatus = STATUS_SCROLL;
            final float percent = (mCollapsedTop - mDragView.getTop()) * 1.0f / (mCollapsedTop - mExpandedTop);
            dragPositionChanged(mHeight - mDragView.getTop(), percent);
            ViewCompat.postInvalidateOnAnimation(this);
        }
        super.computeScroll();
    }

    /**
     * 处理自定义滚动动画
     */
    private boolean _continueSettling(ScrollerCompat scroller) {
        boolean keepGoing = scroller.computeScrollOffset();
        if (!keepGoing) {
            return false;
        }
        final int x = scroller.getCurrX();
        final int y = scroller.getCurrY();
        final int dx = x - mDragView.getLeft();
        final int dy = y - mDragView.getTop();
        if (dx != 0) {
            ViewCompat.offsetLeftAndRight(mDragView, dx);
        }
        if (dy != 0) {
            ViewCompat.offsetTopAndBottom(mDragView, dy);
        }
        if (keepGoing && x == scroller.getFinalX() && y == scroller.getFinalY()) {
            // Close enough. The interpolator/scroller might think we're still moving
            // but the user sure doesn't.
            scroller.abortAnimation();
            keepGoing = false;
            ViewCompat.postInvalidateOnAnimation(this);
            switchStatus();
        }
        return keepGoing;
    }

    /**
     * 停止所有滚动
     */
    private void stopAllScroller() {
        if (!mFallBoundScroller.isFinished()) {
            mFallBoundScroller.abortAnimation();
        }
        if (!mDecelerateScroller.isFinished()) {
            mDecelerateScroller.abortAnimation();
        }
    }

    /**
     * 滚出屏幕
     *
     * @param duration 时间
     */
    public void scrollOutScreen(int duration) {
        mIsDoOutAnim = true;
        mDecelerateScroller.startScroll(0, mDragView.getTop(), 0, mHeight - mDragView.getTop(), duration);
        ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
    }

    /**
     * 滚进屏幕
     *
     * @param duration 时间
     */
    public void scrollInScreen(int duration) {
        mIsDoOutAnim = false;
        mDecelerateScroller.startScroll(0, mDragView.getTop(), 0, mCollapsedTop - mDragView.getTop(), duration);
        ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
    }

    /***********************************
     * Inside
     ********************************************/

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_EXPANDED, STATUS_COLLAPSED, STATUS_EXIT, STATUS_SCROLL})
    @interface DragStatus {
    }

    /**
     * 切换状态
     */
    private void switchStatus() {
        if (mDragView.getTop() == mExpandedTop) {
            mDragStatus = STATUS_EXPANDED;
        } else if (mDragView.getTop() == mCollapsedTop) {
            mDragStatus = STATUS_COLLAPSED;
        } else if (mDragView.getTop() == mHeight) {
            mDragStatus = STATUS_EXIT;
        } else {
            mDragStatus = STATUS_SCROLL;
        }
    }

    /**
     * 隐藏 DragView
     *
     * @param percent ViewPager 滑动百分比
     */
    private void _hideDragView(float percent, int curTop) {
        if (!mHasShowRunnable && !mIsDoOutAnim) {
            float hidePercent = percent * 3;
            if (hidePercent > 1.0f) {
                hidePercent = 1.0f;
                mDragStatus = STATUS_EXIT;
            } else {
                mDragStatus = STATUS_SCROLL;
            }
            stopAllScroller();
            final int y = (int) ((mCollapsedTop + mFixHeight - curTop) * hidePercent + curTop);
            final int dy = y - mDragView.getTop();
            if (dy != 0) {
                ViewCompat.offsetTopAndBottom(mDragView, dy);
                final float dragPercent = (mCollapsedTop - mDragView.getTop()) * 1.0f / (mCollapsedTop - mExpandedTop);
                dragPositionChanged(mHeight - mDragView.getTop(), dragPercent);
            }
        }
    }

    /**
     * 显示 DragView
     *
     * @param delay 延迟时间
     */
    private void showDragView(int delay) {
        mHasShowRunnable = true;
        postDelayed(mShowRunnable, delay);
    }

    /**
     * 动画显示 Runnable
     */
    private Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            mHasShowRunnable = false;
            mDecelerateScroller.startScroll(0, mDragView.getTop(), 0, mCollapsedTop - mDragView.getTop(), 500);
            ViewCompat.postInvalidateOnAnimation(DragSlopLayout.this);
        }
    };

    /**
     * 拖拽位移监听
     *
     * @param visibleHeight 当前可见高度
     * @param percent       百分比
     */
    private void dragPositionChanged(int visibleHeight, float percent) {
        if (mDragViewTop == 0) {
            mLastDragViewTop = mHeight - visibleHeight;
        }
        mDragViewTop = mHeight - visibleHeight;
        // 拖拽距离超过最小滑动距离则进行判断
        if (Math.abs(mDragViewTop - mLastDragViewTop) > mMinTouchSlop) {
            mIsUp = (mDragViewTop < mLastDragViewTop);
            mLastDragViewTop = mDragViewTop;
        }
        if (visibleHeight >= 0) {
            ViewCompat.setTranslationY(mMainView, -visibleHeight * (1 - mCollapseParallax));
        }
    }

    /*********************************** ScrollView ********************************************/

    /**
     * 设置关联的 ScrollView 如果有的话，目前只支持 ScrollView 和 NestedScrollView 及其自视图
     *
     * @param attachScrollView ScrollView or NestedScrollView
     */
    public void setAttachScrollView(View attachScrollView) {
        if (!_isScrollView(attachScrollView)) {
            throw new IllegalArgumentException("The view must be ScrollView or NestedScrollView.");
        }
        mAttachScrollView = attachScrollView;
    }

    /**
     * 如果点击在关联的 ScrollView 区域则由父类 DragSlopLayout 中断事件接收，并全权控制处理 ScrollView
     *
     * @param ev 点击事件
     * @return
     */
    private boolean isNeedIntercept(MotionEvent ev) {
        if (mAttachScrollView == null) {
            return false;
        }
        int y = (int) ev.getY() - mDragView.getTop();
        if (mDragHelper.isViewUnder(mAttachScrollView, (int) ev.getX(), y)) {
            return true;
        }
        return false;
    }

    /**
     * 滑动 ScrollView
     *
     * @param yvel 滑动速度
     * @return
     */
    private boolean _flingScrollView(float yvel) {
        if (mAttachScrollView == null || mAttachScrollView.getScrollY() == 0) {
            return false;
        }
        if (mAttachScrollView instanceof ScrollView) {
            ((ScrollView) mAttachScrollView).fling((int) -yvel);
        } else if (mAttachScrollView instanceof NestedScrollView) {
            ((NestedScrollView) mAttachScrollView).fling((int) -yvel);
        }
        return true;
    }

    /**
     * 判断视图是否为 ScrollView or NestedScrollView 或它的子类
     *
     * @param view View
     * @return
     */
    private boolean _isScrollView(View view) {
        boolean isScrollView = false;
        if (view instanceof ScrollView || view instanceof NestedScrollView) {
            isScrollView = true;
        } else {
            ViewParent parent = view.getParent();
            while (parent != null) {
                if (parent instanceof ScrollView || parent instanceof NestedScrollView) {
                    isScrollView = true;
                    break;
                }
            }
        }
        return isScrollView;
    }

    /*********************************** ViewPager ********************************************/


    /**
     * 和 ViewPager 进行联动
     *
     * @param attachViewPager 关联 ViewPager
     */
    public void attachViewPager(ViewPager attachViewPager) {
        if (!_isViewPager(attachViewPager)) {
            throw new IllegalArgumentException("The first child view must be ViewPager.");
        }
        mAttachViewPager = attachViewPager;
        if (mViewPagerListener != null) {
            mAttachViewPager.removeOnPageChangeListener(mViewPagerListener);
        }
        mViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {

            boolean isRightSlide = true;
            float mLastOffset = 0;
            int status = ViewPager.SCROLL_STATE_IDLE;
            int curDragViewTop;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (status != ViewPager.SCROLL_STATE_IDLE && !mIsCustomAnimator) {
                    // 判断拖拽过界的方向
                    if (Math.abs(positionOffset - mLastOffset) > 0.8f &&
                            status == ViewPager.SCROLL_STATE_DRAGGING) {
                        if (positionOffset > 0.5f) {
                            isRightSlide = false;
                        } else {
                            isRightSlide = true;
                        }
                    }
                    float percent;
                    if (isRightSlide) {
                        percent = positionOffset;
                        if (positionOffset == 0 && status == ViewPager.SCROLL_STATE_SETTLING && mLastOffset > 0.5f) {
                            percent = 1.0f;
                        }
                    } else {
                        percent = 1 - positionOffset;
                        if (positionOffset == 0 && status == ViewPager.SCROLL_STATE_SETTLING && mLastOffset > 0.5f) {
                            percent = 0;
                        }
                    }
                    _hideDragView(percent, curDragViewTop);
                    mLastOffset = positionOffset;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && !mIsCustomAnimator) {
                    isRightSlide = true;
                    mLastOffset = 0;
                    // 如果手动调用退出动画则不做自动启动动画
                    if (mDragStatus == STATUS_EXIT && !mIsDoOutAnim) {
                        showDragView(mAutoAnimateDelay);
                    }
                } else {
                    if (mDragStatus == STATUS_EXIT) {
                        getHandler().removeCallbacks(mShowRunnable);
                    }
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                        curDragViewTop = mDragView.getTop();
                    }
                }
                status = state;
            }
        };
        mAttachViewPager.addOnPageChangeListener(mViewPagerListener);
    }

    /**
     * 取消 ViewPager 关联
     */
    public void detachViewPager() {
        if (mViewPagerListener != null && mAttachViewPager != null) {
            mAttachViewPager.removeOnPageChangeListener(mViewPagerListener);
        }
        mViewPagerListener = null;
        mAttachViewPager = null;
    }

    public int getAutoAnimateDelay() {
        return mAutoAnimateDelay;
    }

    public void setAutoAnimateDelay(int autoAnimateDelay) {
        mAutoAnimateDelay = autoAnimateDelay;
    }

    /**
     * 判断视图是否为 ViewPager 或它的子类
     *
     * @param view View
     * @return
     */
    private boolean _isViewPager(View view) {
        boolean isViewPager = false;
        if (view instanceof ViewPager) {
            isViewPager = true;
        } else {
            ViewParent parent = view.getParent();
            while (parent != null) {
                if (parent instanceof ViewPager) {
                    isViewPager = true;
                    break;
                }
            }
        }
        return isViewPager;
    }

    /**
     * ================================ 监听器 ================================
     */

    // 监听器
    private OnDragPositionListener mDragPositionListener;
    // 是否向上拖拽
    private boolean mIsUp = false;
    // 上一次比较时的拖拽高度
    private int mLastDragViewTop;
    // 最小触摸滑动距离
    private int mMinTouchSlop;

    /**
     * 设置监听器
     *
     * @param dragPositionListener
     */
    public void setDragPositionListener(OnDragPositionListener dragPositionListener) {
        mDragPositionListener = dragPositionListener;
    }

    /**
     * 拖拽监听器
     */
    public interface OnDragPositionListener {

        /**
         * 拖拽监听
         *
         * @param visibleHeight 可见高度
         * @param percent       可见百分比
         * @param isUp          是否向上拖拽
         */
        void onDragPosition(int visibleHeight, float percent, boolean isUp);
    }
}
