
### detach

View 的 post 方法
```java
public boolean post(Runnable action) {
      
    //注释1处 正常attach的时候，直接执行
    final AttachInfo attachInfo = mAttachInfo;
    if (attachInfo != null) {
        return attachInfo.mHandler.post(action);
    }
    //注释2处，否则加入View的一个 runnable 队列，等重新 attach的时候执行
    // Postpone the runnable until we know on which thread it needs to run.
    // Assume that the runnable will be successfully placed after attach.
    getRunQueue().post(action);
    return true;
}
```

注释1处 正常attach的时候，直接执行。

注释2处，否则加入View的一个 runnable 队列，等重新 attach的时候执行。


### 重新attach

```java
void dispatchAttachedToWindow(AttachInfo info, int visibility) {
    mAttachInfo = info;
    if (mOverlay != null) {
        mOverlay.getOverlayView().dispatchAttachedToWindow(info, visibility);
    }
    mWindowAttachCount++;
    // We will need to evaluate the drawable state at least once.
    mPrivateFlags |= PFLAG_DRAWABLE_STATE_DIRTY;
    if (mFloatingTreeObserver != null) {
        info.mTreeObserver.merge(mFloatingTreeObserver);
        mFloatingTreeObserver = null;
    }

    registerPendingFrameMetricsObservers();

    if ((mPrivateFlags & PFLAG_SCROLL_CONTAINER) != 0) {
        mAttachInfo.mScrollContainers.add(this);
        mPrivateFlags |= PFLAG_SCROLL_CONTAINER_ADDED;
    }
    // Transfer all pending runnables.
    //注释1处，重新attach之前，执行前面的runnable
    if (mRunQueue != null) {
        mRunQueue.executeActions(info.mHandler);
        mRunQueue = null;
    }

    performCollectViewAttributes(mAttachInfo, visibility);
    //通知重新attach了
    onAttachedToWindow();
}
```
注释1处，重新attach之前，执行前面的runnable.
