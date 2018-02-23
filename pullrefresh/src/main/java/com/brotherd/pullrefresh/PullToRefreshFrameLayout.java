package com.brotherd.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by shucc on 17/3/29.
 * cc@cchao.org
 */
public class PullToRefreshFrameLayout extends PullToRefreshBase<FrameLayout> {

    private OnCheckCanDoRefreshListener onCheckCanDoRefreshListener;

    public PullToRefreshFrameLayout(Context context) {
        super(context);
    }

    public PullToRefreshFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshFrameLayout(Context context, Mode mode) {
        super(context, mode);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected FrameLayout createRefreshableView(Context context, AttributeSet attrs) {
        FrameLayout frameLayout = new FrameLayout(context, attrs);
        frameLayout.setId(R.id.framelayout);
        return frameLayout;
    }

    @Override
    protected boolean isReadyForPullStart() {
        if (onCheckCanDoRefreshListener != null) {
            return onCheckCanDoRefreshListener.checkCanPullStart();
        }
        return false;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        if (onCheckCanDoRefreshListener != null) {
            return onCheckCanDoRefreshListener.checkCanPullEnd();
        }
        return false;
    }

    public void setOnCheckCanDoRefreshListener(OnCheckCanDoRefreshListener onCheckCanDoRefreshListener) {
        this.onCheckCanDoRefreshListener = onCheckCanDoRefreshListener;
    }
}
