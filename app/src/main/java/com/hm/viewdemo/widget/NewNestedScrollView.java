package com.hm.viewdemo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.widget.NestedScrollView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by p_dmweidu on 2023/2/2
 * Desc: 测试监听NestedScrollView滑动停止
 */
public class NewNestedScrollView extends NestedScrollView implements NestedScrollView.OnScrollChangeListener {

    private static final String TAG = "NewNestedScrollView";

    /**
     * 监听回调
     */
    private AddScrollChangeListener addScrollChangeListener;
    private ExecutorService threadExecutor;
    private int scaledTouchSlop;

    /**
     * 滚动状态
     */
    public enum ScrollState {
        DRAG,      // 拖拽中
        SCROLLING, // 正在滚动
        IDLE       // 已停止
    }

    /**
     * 记录上一次滑动
     */
    private int lastScrollY;
    /**
     *
     */
    private boolean isStart = false;
    /**
     * 上一次记录的时间
     */
    private long lastTime;


    private Handler handler;
    /**
     * 整個滾動内容高度
     */
    public int totalHeight = 0;

    /**
     * 当前view的高度
     */
    public int viewHeight = 0;

    /**
     * 是否滚动到底了
     */
    private boolean bottom = false;

    /**
     * 是否滚动在顶部
     *
     * @param context
     */
    private boolean top = false;

    /**
     * 是否有move事件，没有的话，up事件的时候不需要检测
     */
    private boolean moved = false;

    private float mLastY = 0f;

    public NewNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public NewNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollChangeListener(this);
        HandlerThread handlerThread = new HandlerThread("NewNestedScrollView");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        threadExecutor = Executors.newSingleThreadExecutor();
        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }


    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        /*实时滚动回调*/
        if (addScrollChangeListener != null) {
            addScrollChangeListener.onScrollChange(scrollX, scrollY, oldScrollX, oldScrollY);
        }

        if (totalHeight > viewHeight && (totalHeight - viewHeight) == scrollY) {
            Log.i(TAG, "[NewNestedScrollView]->onScrollChange = bottom");
            bottom = true;
        } else {
            bottom = false;
        }

        if (getScrollY() <= 0) {
            Log.i(TAG, "[NewNestedScrollView]->onScrollChange = top");
            top = true;
        } else {
            top = false;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        totalHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            totalHeight += view.getMeasuredHeight();
        }
        viewHeight = getHeight();
    }


    /**
     * 是否动到底
     *
     * @return
     */
    public boolean isBottom() {
        return bottom;
    }


    /**
     * 是否滚动到了 顶部
     *
     * @return
     */
    public boolean isTop() {
        return top;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                isStart = false;
                //Log.i(TAG, "[NewNestedScrollView]->DRAG 拖拽中");
                if (addScrollChangeListener != null) {
                    addScrollChangeListener.onScrollState(ScrollState.DRAG);
                }
                float y = ev.getY();
                float movedDistance = y - mLastY;
                Log.i(TAG,
                        "dispatchTouchEvent: mLastY = " + mLastY + " y = " + y + " movedDistance = " + movedDistance
                                + " scaledTouchSlop = " + scaledTouchSlop
                );
                if (Math.abs(movedDistance) >= scaledTouchSlop) {
                    moved = true;
                    Log.i(TAG, "dispatchTouchEvent: [NewNestedScrollView]->DRAG 拖拽中");
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                if (moved) {
                    isStart = true;
                    start();
                    moved = false;
                }

                break;
        }

        mLastY = ev.getY();

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 开始计算是否停止还是正在滚性滑动
     */
    private void start() {
        if (threadExecutor != null) {
            threadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    /**
                     * 表示已停止
                     */
                    while (isStart) {
                        if ((System.currentTimeMillis() - lastTime) > 100) {
                            int newScrollY = getScrollY();
                            lastTime = System.currentTimeMillis();
                            if (newScrollY - lastScrollY == 0) {
                                isStart = false;
                                Log.i(TAG, "[NewNestedScrollView]->IDLE 停止滚动");

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (addScrollChangeListener != null) {
                                            addScrollChangeListener.onScrollState(ScrollState.IDLE);
                                        }
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i(TAG, "[NewNestedScrollView]->SCROLLING 正在滚动中");
                                        if (isStart && addScrollChangeListener != null) {
                                            addScrollChangeListener.onScrollState(ScrollState.SCROLLING);
                                        }
                                    }
                                });
                            }
                            lastScrollY = newScrollY;
                        }
                    }
                }
            });
        }
    }

    /**
     * 设置监听
     *
     * @param addScrollChangeListener
     * @return
     */
    public NewNestedScrollView addScrollChangeListener(AddScrollChangeListener addScrollChangeListener) {
        this.addScrollChangeListener = addScrollChangeListener;
        return this;
    }


    public interface AddScrollChangeListener {

        /**
         * 滚动监听
         *
         * @param scrollX
         * @param scrollY
         * @param oldScrollX
         * @param oldScrollY
         */
        void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY);


        /**
         * 滚动状态
         *
         * @param state
         */
        void onScrollState(ScrollState state);
    }
}

