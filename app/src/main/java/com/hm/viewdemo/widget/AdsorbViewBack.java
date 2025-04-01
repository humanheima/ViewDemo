package com.hm.viewdemo.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.hm.viewdemo.util.ScreenUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dumingwei on 2020/12/2
 * <p>
 * Desc:
 */
public class AdsorbViewBack extends FrameLayout {

    private static final String TAG = "AdsorbView";

    private final Rect rect = new Rect();
    private int downX, downY;
    private int lastX, lastY;
    private Rect dragRect;  //可以拖动的区域
    private int mTouchSlop;
    private boolean interruptedMove = false;
    private boolean isSuctionStatus = false;//当前是否是吸边状态
    private boolean mIsRightStatus = true;//当前是否在右边
    private boolean isMoving = false;

    public interface IMoveDirection {
        void moveDirection(boolean isRight);
    }

    private final Set<IMoveDirection> moveDirections = new HashSet<>();

    public AdsorbViewBack(Context context) {
        super(context);
        initView();
    }

    public AdsorbViewBack(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AdsorbViewBack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        initRect();

        setClickable(true);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        moveDirections.clear();
    }

    private void initRect() {
        int screenWidth = ScreenUtil.getScreenWidth(getContext());
        int screenHeight = ScreenUtil.getScreenHeight(getContext());
        Log.i(TAG, "initRect: screenWidth" + screenWidth + " , screenHeight = " + screenHeight);
        dragRect = new Rect(0, 0, screenWidth, screenHeight);

        Log.i(TAG, "initRect: drapRect = " + dragRect.flattenToString());
        post(new Runnable() {
            @Override
            public void run() {
                getHitRect(rect);
                if (isSetDrapRect) {
                    return;
                }
                if (getParent() instanceof View) {
                    ((View) getParent()).getHitRect(dragRect);
                    Log.i(TAG, "initRect: post drapRect = " + dragRect.flattenToString());
                }
            }
        });
    }

    public void setLayoutId(int layoutId) {
        if (layoutId > 0) {
            removeAllViews();
            inflate(getContext(), layoutId, this);
        }
    }

    public void setContentView(View view) {
        if (view == null) {
            return;
        }
        removeAllViews();
        addView(view);
    }

    private boolean isSetDrapRect;

    public void setDragRect(Rect rect) {
        isSetDrapRect = true;
        this.dragRect = rect;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isSetDrapRect) {
            initRect();
        }
    }

    public void addMoveDirectionListener(IMoveDirection moveDirection) {
        this.moveDirections.add(moveDirection);
    }

    public void removeDirectionListener(IMoveDirection moveDirection) {
        this.moveDirections.remove(moveDirection);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //if (!isSuctionStatus) {
        //吸边时禁止拖曳
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取在父坐标系中的位置
                getHitRect(rect);
                Log.i(TAG, "onTouchEvent: ACTION_DOWN rect = " + rect.flattenToString());
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                isMoving = true;
                requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (interruptedMove) {
                    return true;
                }

                int moveX = (int) event.getRawX() - downX;
                int moveY = (int) event.getRawY() - downY;

                rect.offset(moveX, moveY);

                setX(rect.left);
                setY(rect.top);

                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                //手指一动距离过小
                if (Math.abs(lastX - event.getRawX()) < mTouchSlop && Math.abs(lastY - event.getRawY()) < mTouchSlop) {
                    return super.onTouchEvent(event);
                } else {
                    if (interruptedMove) {
                        return true;
                    }
                }

                if (!interruptedMove) {
                    //AdsorbView的中心横坐标和可拖动区域中心横坐标比较，x<0靠左，x>0靠右
                    int x = rect.centerX() - ((dragRect.right - dragRect.left) / 2);
                    if (x < 0) {
                        if (moveDirections != null) {
                            for (IMoveDirection moveDirection : moveDirections) {
                                moveDirection.moveDirection(false);
                            }
                        }
                        //靠左，rect的left坐标设置为0
                        rect.offsetTo(0, rect.top);
                        mIsRightStatus = false;
                    } else {
                        if (moveDirections != null) {
                            for (IMoveDirection moveDirection : moveDirections) {
                                moveDirection.moveDirection(true);
                            }
                        }
                        //靠右，rect的left坐标设置为drapRect的宽度减去AdsorbView的的宽度
                        rect.offsetTo(dragRect.right - dragRect.left - getWidth(), rect.top);
                        mIsRightStatus = true;
                    }

                    if (rect.top < dragRect.top) {
                        //不能超过上边界
                        rect.offsetTo(rect.left, dragRect.top);
                    } else if (rect.bottom > dragRect.bottom) {
                        //不能超过下边界
                        rect.offsetTo(rect.left, dragRect.bottom - getHeight());
                    }
                    startFlingAnim();
                    requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            case MotionEvent.ACTION_CANCEL:
                isMoving = false;
                requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        //}
        return super.onTouchEvent(event);
    }

    public void setInterruptedMove(boolean interrupted) {
        this.interruptedMove = interrupted;
    }

    /**
     * 开始惯性滑动动画
     */
    private void startFlingAnim() {
        if (rect == null || dragRect == null) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, "x", getX(), rect.left);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this, "y", getY(), rect.top);
        animatorSet.playTogether(objectAnimator1, objectAnimator2);

        //计算动画时间
        if (Math.abs(getX() - rect.left) > Math.abs(getY() - rect.top)) {
            if (dragRect.centerX() != 0) {
                float v = 300 * 1.0f / dragRect.centerX() * (Math.abs(getX() - rect.left));
                if (v <= 0) {
                    v = 50;
                }
                animatorSet.setDuration((long) v);
            }
        } else {
            if (dragRect.centerY() != 0) {
                float v = 300 * 1.0f / dragRect.centerY() * (Math.abs(getY() - rect.top));
                if (v <= 0) {
                    v = 50;
                }
                animatorSet.setDuration((long) v);
            }
        }

        animatorSet.start();
    }

    /**
     * 设置吸边 漏出1/3
     *
     * @param isSuction 是否要设置吸边
     */
    public void setSuction(boolean isSuction) {
        if (isSuctionStatus == isSuction || interruptedMove || isMoving) {
            return;
        }
        if (isSuction) {
            //吸边操作
            if (mIsRightStatus) {
                //右吸边
                rect.offsetTo(dragRect.right - dragRect.left - getWidth() / 3, rect.top);
            } else {
                //左吸边
                rect.offsetTo(-getWidth() * 2 / 3, rect.top);
            }
        } else {
            //取消吸边操作
            if (mIsRightStatus) {
                rect.offsetTo(dragRect.right - dragRect.left - getWidth(), rect.top);
            } else {
                rect.offsetTo(0, rect.top);
            }
        }

        startFlingAnim();

        isSuctionStatus = isSuction;
    }

    /**
     * 获取View距拖动范围底部的距离
     */
    public int getBottomMargin() {
        Log.i(TAG, "drapRect.bottom=" + dragRect.bottom);
        Log.i(TAG, "drapRect.top=" + dragRect.top);
        Log.i(TAG, "drapRect.left=" + dragRect.left);
        Log.i(TAG, "drapRect.right=" + dragRect.right);
        Log.i(TAG, "drapRect.高度=" + (dragRect.bottom - dragRect.top));

        Log.i(TAG, "===========");

        Log.i(TAG, "rect.bottom=" + rect.bottom);
        Log.i(TAG, "rect.top=" + rect.top);
        Log.i(TAG, "rect.left=" + rect.left);
        Log.i(TAG, "rect.right=" + rect.right);
        Log.i(TAG, "rect.高度=" + (rect.bottom - rect.top));

        Log.i(TAG, "===========");
        Log.i(TAG, "bottom差=" + (dragRect.bottom - rect.bottom));
        Log.i(TAG, "top差=" + rect.top);

        //计算出
        int height = rect.bottom - rect.top;
        if (height == 0) {
            return 0;
        }
        //处于可拖动区域的最顶部
        if (rect.top <= 0) {
            return dragRect.bottom - height;
        }
        //处于可拖动区域的最底部
        if (rect.bottom >= dragRect.bottom) {
            return 0;
        }
        //在正确范围内，可拖动区域的底部减去AdsorbView的底部就是bottomMargin
        return dragRect.bottom - rect.bottom;
    }

    public boolean isSuctionStatus() {
        return isSuctionStatus;
    }
}
