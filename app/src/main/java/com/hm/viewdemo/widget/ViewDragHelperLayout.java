package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

import com.hm.viewdemo.util.ScreenUtil;

/**
 * Crete by dumingwei on 2019-06-28
 * Desc:
 */
public class ViewDragHelperLayout extends LinearLayout {

    private static final String TAG = "ViewDragHelperLayout";

    private ViewDragHelper viewDragHelper;
    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;

    private Point mAutoBackOriginPos = new Point();

    private int dp30;

    public ViewDragHelperLayout(Context context) {
        this(context, null);
    }

    public ViewDragHelperLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewDragHelperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dp30 = ScreenUtil.dpToPx(context, 30);
        Log.d(TAG, "ViewDragHelperLayout: dp30 = " + dp30);

        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //mEdgeTrackerView禁止直接移动
                return child == mDragView || child == mAutoBackView;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                Log.i("DragLayout", "clampViewPositionHorizontal " + left + "," + child.getX());
                final int leftBound = getPaddingLeft() - dp30;
                final int rightBound = getWidth() - getPaddingRight() - child.getWidth() + dp30;
                return Math.min(Math.max(left, leftBound), rightBound);
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - getPaddingBottom() - child.getHeight();
                return Math.min(Math.max(top, topBound), bottomBound);
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild == mAutoBackView) {
                    viewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                } else if (releasedChild == mDragView) {
                    //viewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    //invalidate();
                    OutOfBoundsDirection outOfBoundsDirection = getViewOutOfBoundsDirection(mDragView, ViewDragHelperLayout.this);
                    Log.e(TAG, "onViewReleased: " + outOfBoundsDirection.name());
                    if (outOfBoundsDirection == OutOfBoundsDirection.LEFT) {
                        //Toast.makeText(getContext(), "超出左边界", Toast.LENGTH_SHORT).show();

                    } else if (outOfBoundsDirection == OutOfBoundsDirection.RIGHT) {
                        //Toast.makeText(getContext(), "超出右边界", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                viewDragHelper.captureChildView(mEdgeTrackerView, pointerId);
            }

            //给内部的view添加了点击事件以后要重写这个方法
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            //给内部的view添加了点击事件以后要重写这个方法
            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

    }

    public enum OutOfBoundsDirection {
        NONE,       // 未超出
        LEFT,       // 超出左边界
        TOP,        // 超出上边界
        RIGHT,      // 超出右边界
        BOTTOM,     // 超出下边界
        LEFT_TOP,   // 超出左上角
        LEFT_BOTTOM,// 超出左下角
        RIGHT_TOP,  // 超出右上角
        RIGHT_BOTTOM// 超出右下角
    }

    public OutOfBoundsDirection getViewOutOfBoundsDirection(View child, View parent) {
        Rect childRect = new Rect();
        child.getHitRect(childRect);

        Rect parentRect = new Rect(
                parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.getWidth() - parent.getPaddingRight(),
                parent.getHeight() - parent.getPaddingBottom()
        );

        // 如果没有交集，说明完全超出
        if (!Rect.intersects(parentRect, childRect)) {
            if (childRect.right < parentRect.left && childRect.bottom < parentRect.top) {
                return OutOfBoundsDirection.LEFT_TOP;
            } else if (childRect.right < parentRect.left && childRect.top > parentRect.bottom) {
                return OutOfBoundsDirection.LEFT_BOTTOM;
            } else if (childRect.left > parentRect.right && childRect.bottom < parentRect.top) {
                return OutOfBoundsDirection.RIGHT_TOP;
            } else if (childRect.left > parentRect.right && childRect.top > parentRect.bottom) {
                return OutOfBoundsDirection.RIGHT_BOTTOM;
            } else if (childRect.right < parentRect.left) {
                return OutOfBoundsDirection.LEFT;
            } else if (childRect.left > parentRect.right) {
                return OutOfBoundsDirection.RIGHT;
            } else if (childRect.bottom < parentRect.top) {
                return OutOfBoundsDirection.TOP;
            } else if (childRect.top > parentRect.bottom) {
                return OutOfBoundsDirection.BOTTOM;
            }
        }

        // 检查部分超出
        boolean leftOut = childRect.left < parentRect.left;
        boolean topOut = childRect.top < parentRect.top;
        boolean rightOut = childRect.right > parentRect.right;
        boolean bottomOut = childRect.bottom > parentRect.bottom;

        if (!leftOut && !topOut && !rightOut && !bottomOut) {
            return OutOfBoundsDirection.NONE;
        }

        // 组合判断
        if (leftOut && topOut) return OutOfBoundsDirection.LEFT_TOP;
        if (leftOut && bottomOut) return OutOfBoundsDirection.LEFT_BOTTOM;
        if (rightOut && topOut) return OutOfBoundsDirection.RIGHT_TOP;
        if (rightOut && bottomOut) return OutOfBoundsDirection.RIGHT_BOTTOM;
        if (leftOut) return OutOfBoundsDirection.LEFT;
        if (topOut) return OutOfBoundsDirection.TOP;
        if (rightOut) return OutOfBoundsDirection.RIGHT;
        if (bottomOut) return OutOfBoundsDirection.BOTTOM;

        return OutOfBoundsDirection.NONE;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mDragView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "I am mDragView", Toast.LENGTH_SHORT).show();
            }
        });

        mAutoBackView = getChildAt(1);
        mAutoBackView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "I am mAutoBackView", Toast.LENGTH_SHORT).show();
            }
        });

        mEdgeTrackerView = getChildAt(2);

        mEdgeTrackerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "I am mEdgeTrackerView", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
