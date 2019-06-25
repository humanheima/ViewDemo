package com.hm.viewdemo.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.util.ScreenUtil;

/**
 * Created by long on 2016/9/6.
 * 实现拖拽出界外的布局
 * 链接 https://github.com/Rukey7/DragSlopLayout
 */
public class DragSlopLayout extends FrameLayout {

    private static final String TAG = "DragSlopLayout";
    // 固定高度
    private int mFixHeight;
    // 最大高度
    private int mMaxHeight;
    // 拖拽模式的展开状态Top值
    private int mExpandedTop;
    // 拖拽模式的收缩状态Top值
    private int mCollapsedTop;
    // 是否处于拖拽状态
    private boolean mIsDrag = false;
    // 可拖拽的视图，为布局的第2个子视图
    private View mDragView;
    // 拖拽帮助类
    private ViewDragHelper mDragHelper;
    // 关联的 ScrollView，实现垂直方向的平滑滚动
    private View mAttachScrollView;
    // DragView的Top属性值
    private int mDragViewTop = 0;
    // 回升滚动辅助类
    private ObjectAnimator dragOutAnimator;
    private ObjectAnimator dragInAnimator;

    // 判断快速滑动的速率
    private static final float FLING_VELOCITY = 5000;

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
        mDragHelper = ViewDragHelper.create(this, 1.0f, callback);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DragSlopLayout, defStyleAttr, 0);
        mFixHeight = a.getDimensionPixelOffset(R.styleable.DragSlopLayout_fix_height, ScreenUtil.dpToPx(context, 180));
        mMaxHeight = a.getDimensionPixelOffset(R.styleable.DragSlopLayout_max_height, ScreenUtil.dpToPx(context, 400));
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount < 2) {
            throw new IllegalArgumentException("DragLayout must contains two sub-views.");
        }
        mDragView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure");
        if (mMaxHeight > getMeasuredHeight()) {
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout");
        View childView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
        int childWidth = childView.getMeasuredWidth();
        int childHeight = childView.getMeasuredHeight();
        mExpandedTop = b - childHeight;
        //向下滑动时最小高度为固定高度
        mCollapsedTop = b - mFixHeight;
        //可拖拽view的top属性设为固定高度
        mDragViewTop = b - mFixHeight;
        if (mAttachScrollView != null)
            mAttachScrollView.scrollTo(0, 0);
        childView.layout(lp.leftMargin, mDragViewTop, lp.leftMargin + childWidth, mDragViewTop + childHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            //手抬起mIsDrag置为false
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
        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN && (
                mDragHelper.isViewUnder(mDragView, (int) event.getX(), (int) event.getY()))) {
            //当触摸点在dragView范围之内，把mIsDrag置为true
            Log.e(TAG, "onTouchEvent MotionEvent.ACTION_DOWN");
            mIsDrag = true;
        } else {
            mDragHelper.processTouchEvent(event);
        }
        return mIsDrag;
    }

    /**
     * 滚出屏幕
     *
     * @param duration 时间
     */
    public void scrollOutScreen(int duration) {
        Log.e(TAG, "scrollOutScreen");
        if (dragOutAnimator == null) {
            dragOutAnimator = ObjectAnimator.ofFloat(mDragView, "translationY", 0, mMaxHeight)
                    .setDuration(duration);
        }
        dragOutAnimator.start();
    }

    /**
     * 滚进屏幕
     *
     * @param duration 时间
     */
    public void scrollInScreen(int duration) {
        Log.e(TAG, "scrollInScreen");
        if (dragInAnimator == null) {
            dragInAnimator = ObjectAnimator.ofFloat(mDragView, "translationY", mMaxHeight, 0)
                    .setDuration(duration);
        }
        dragInAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (dragOutAnimator != null && dragOutAnimator.isRunning())
            dragOutAnimator.cancel();
        if (dragInAnimator != null && dragInAnimator.isRunning())
            dragInAnimator.cancel();
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //只有mDragView才可以拖动
            return child == mDragView;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            float velocity = FLING_VELOCITY;
            if (Math.abs(yvel) < velocity) {
                // 在 MODE_DRAG_OUTSIDE 模式下才做自动滚动处理，其它模式做收缩滚动,快速滚动在任何模式都做
                mDragHelper.smoothSlideViewTo(mDragView, 0, mExpandedTop);
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
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (mAttachScrollView != null) {
                /**
                 * mAttachScrollView.getScrollY() > 0 ： 如果mAttachScrollView向上滚动了，那么向下拖动的时候，先让mAttachScrollView滚动到初始位置
                 * (mDragView.getTop() == mExpandedTop && dy < 0) ：当向上拖动mDragView到了最高的高度后继续向上拖动，就让ScrollView向上滚动，
                 */
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
        public int getViewVerticalDragRange(View child) {
            //可拖动范围是mDragView的高度
            return child == mDragView ? child.getHeight() : 0;
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }


    /*********************************** ScrollView ********************************************/

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
     * 设置关联的 ScrollView 如果有的话，目前只支持 ScrollView 和 NestedScrollView 及其子视图
     *
     * @param attachScrollView ScrollView or NestedScrollView
     */
    public void setAttachScrollView(View attachScrollView) {
        if (!isScrollView(attachScrollView)) {
            throw new IllegalArgumentException("The view must be ScrollView or NestedScrollView.");
        }
        mAttachScrollView = attachScrollView;
    }

    private boolean isNeedIntercept(MotionEvent ev) {
        if (mAttachScrollView == null) {
            return false;
        }
        int y = (int) ev.getY() - mDragView.getTop();
        Log.e(TAG, "isNeedIntercept y=" + y + ",mAttachScrollView.getTop()=" + mAttachScrollView.getTop());
        //如果拖拽的区域是关联的ScrollView的区域，拦截事件
        if (mDragHelper.isViewUnder(mAttachScrollView, (int) ev.getX(), y)) {
            return true;
        }
        return false;
    }

    /**
     * 判断视图是否为 ScrollView or NestedScrollView 或它的子类
     *
     * @param view View
     * @return
     */
    private boolean isScrollView(View view) {
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
}
