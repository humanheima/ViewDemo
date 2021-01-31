源码版本：28

本篇要点：滑动的时候是怎么回收View和填充新的View的。


AbsListView的onTouchEvent方法。

```java
@Override
public boolean onTouchEvent(MotionEvent ev) {
    //...
    initVelocityTrackerIfNotExists();
    final MotionEvent vtev = MotionEvent.obtain(ev);

    final int actionMasked = ev.getActionMasked();
    if (actionMasked == MotionEvent.ACTION_DOWN) {
        mNestedYOffset = 0;
    }
    vtev.offsetLocation(0, mNestedYOffset);

    switch (actionMasked) {
        case MotionEvent.ACTION_DOWN: {
            onTouchDown(ev);
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            onTouchMove(ev, vtev);
            break;
        }
        case MotionEvent.ACTION_UP: {
            onTouchUp(ev);
            break;
        }
       //...
    }
    if (mVelocityTracker != null) {
        mVelocityTracker.addMovement(vtev);
    }
    vtev.recycle();
    return true;
}
```

AbsListView的onTouchDown方法。

```java
private void onTouchDown(MotionEvent ev) {
    //...   
    if (mTouchMode == TOUCH_MODE_OVERFLING) {
        //MotionEvent.ACTION_DOWN的时候，如果正在OVERFLING，则停止
        mFlingRunnable.endFling();
        if (mPositionScroller != null) {
            mPositionScroller.stop();
        }
        //将触摸模式置为TOUCH_MODE_OVERSCROLL
        mTouchMode = TOUCH_MODE_OVERSCROLL;
        mMotionX = (int) ev.getX();
        mMotionY = (int) ev.getY();
        mLastY = mMotionY;
        mMotionCorrection = 0;
        mDirection = 0;
    } else {
        final int x = (int) ev.getX();
        final int y = (int) ev.getY();
        //将坐标转化为转化为ListView中子View的位置
        int motionPosition = pointToPosition(x, y);

        if (!mDataChanged) {
            if (mTouchMode == TOUCH_MODE_FLING) {
                // Stopped a fling. It is a scroll.
                createScrollingCache();
                mTouchMode = TOUCH_MODE_SCROLL;
                mMotionCorrection = 0;
                motionPosition = findMotionRow(y);
                //注释1处，根据当前速度检查是否要停止fling
                mFlingRunnable.flywheelTouch();
            } else if ((motionPosition >= 0) && getAdapter().isEnabled(motionPosition)) {
                //用户正常的按下操作，不是停止一个fling的操作，将mTouchMode置为TOUCH_MODE_DOWN
                mTouchMode = TOUCH_MODE_DOWN;
                //...
            }
        }

        if (motionPosition >= 0) {
            // Remember where the motion event started
            final View v = getChildAt(motionPosition - mFirstPosition);
            mMotionViewOriginalTop = v.getTop();
        }
    
        //按下时候的水平坐标
        mMotionX = x;
        //按下时候的竖直坐标
        mMotionY = y;
        mMotionPosition = motionPosition;
        //按下的时候，将mLastY重置为Integer.MIN_VALUE。
        mLastY = Integer.MIN_VALUE;
    }
    //...
}
```

注释1处，用户按下时，如果ListView当前正在fling，则需要检查根据速度检查是否要停止fling。


AbsListView的onTouchMove方法。

```java
private void onTouchMove(MotionEvent ev, MotionEvent vtev) {
    int pointerIndex = ev.findPointerIndex(mActivePointerId);
    if (pointerIndex == -1) {
        pointerIndex = 0;
        mActivePointerId = ev.getPointerId(pointerIndex);
    }
  
    if (mDataChanged) {
        layoutChildren();
    }

    final int y = (int) ev.getY(pointerIndex);

    switch (mTouchMode) {
        case TOUCH_MODE_DOWN:
        case TOUCH_MODE_TAP:
        case TOUCH_MODE_DONE_WAITING:
            //注释1处，检查是否需要滑动，true的话，直接break。
            if (startScrollIfNeeded((int) ev.getX(pointerIndex), y, vtev)) {
                break;
            }
            //...
            break;
        case TOUCH_MODE_SCROLL:
        case TOUCH_MODE_OVERSCROLL:
            //注释2处，如果需要直接滑动
            scrollIfNeeded((int) ev.getX(pointerIndex), y, vtev);
            break;
    }
}
```

注释1处，AbsListView的startScrollIfNeeded方法。

```java
private boolean startScrollIfNeeded(int x, int y, MotionEvent vtev) {
    //检查是否移动了足够远的距离，足够远的话，则认为是滚动。
    //移动的距离，指从下向上滑动，rawDeltaY小于0
    final int deltaY = y - mMotionY;
    final int distance = Math.abs(deltaY);
    final boolean overscroll = mScrollY != 0;
    if ((overscroll || distance > mTouchSlop) &&
            (getNestedScrollAxes() & SCROLL_AXIS_VERTICAL) == 0) {
        //（处于overscroll状态或者移动的距离足够远）并且（不是嵌套滑动状态）

        createScrollingCache();
        if (overscroll) {
            mTouchMode = TOUCH_MODE_OVERSCROLL;
            mMotionCorrection = 0;
        } else {
            mTouchMode = TOUCH_MODE_SCROLL;
            //指从下向上滑动，rawDeltaY为小于0，mMotionCorrection小于0
            mMotionCorrection = deltaY > 0 ? mTouchSlop : -mTouchSlop;
        }
        removeCallbacks(mPendingCheckForLongPress);
        setPressed(false);
        final View motionView = getChildAt(mMotionPosition - mFirstPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
        //报告滑动状态是SCROLL_STATE_TOUCH_SCROLL
        reportScrollStateChange(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
        // Time to start stealing events! Once we've stolen them, don't let anyone
        // steal from us
        final ViewParent parent = getParent();
        if (parent != null) {
            //告诉父级不要拦截事件
            parent.requestDisallowInterceptTouchEvent(true);
        }
        //注释1处，重点方法
        scrollIfNeeded(x, y, vtev);
        return true;
    }

    return false;
}
```


注释1处，重点方法，AbsListView的scrollIfNeeded方法。

```java
private void scrollIfNeeded(int x, int y, MotionEvent vtev) {
    //手指从下向上滑动，rawDeltaY小于0
    int rawDeltaY = y - mMotionY;
    int scrollOffsetCorrection = 0;
    int scrollConsumedCorrection = 0;
    //正常MotionEvent的down事件的时候会将mLastY置为Integer.MIN_VALUE
    if (mLastY == Integer.MIN_VALUE) {
        //减去一个mTouchSlop的量，不影响
        rawDeltaY -= mMotionCorrection;
    }
    //...
    final int deltaY = rawDeltaY;
    //滑动增加的距离
    int incrementalDeltaY =
            mLastY != Integer.MIN_VALUE ? y - mLastY : deltaY;
    int lastYCorrection = 0;
    //我们只看TOUCH_MODE_SCROLL的情况，忽略TOUCH_MODE_OVERSCROLL的情况。哈哈，偷个懒。
    if (mTouchMode == TOUCH_MODE_SCROLL) {
        if (y != mLastY) {
            //...

            final int motionIndex;
            if (mMotionPosition >= 0) {
                motionIndex = mMotionPosition - mFirstPosition;
            } else {
                // If we don't have a motion position that we can reliably track,
                // pick something in the middle to make a best guess at things below.
                motionIndex = getChildCount() / 2;
            }

            int motionViewPrevTop = 0;
            View motionView = this.getChildAt(motionIndex);
            if (motionView != null) {
                motionViewPrevTop = motionView.getTop();
            }

            // No need to do all this work if we're not going to move anyway
            boolean atEdge = false;
            if (incrementalDeltaY != 0) {
                //注释1处
                atEdge = trackMotionScroll(deltaY, incrementalDeltaY);
            }

            // Check to see if we have bumped into the scroll limit
            motionView = this.getChildAt(motionIndex);
            if (motionView != null) {
                // Check if the top of the motion view is where it is
                // supposed to be
                final int motionViewRealTop = motionView.getTop();
                if (atEdge) {
                    // 应用 overscroll，忽略 ...
                }
                mMotionY = y + lastYCorrection + scrollOffsetCorrection;
            }
            mLastY = y + lastYCorrection + scrollOffsetCorrection;
        }
    } 
}

```

注释1处，AbsListView的trackMotionScroll方法。

```java
boolean trackMotionScroll(int deltaY, int incrementalDeltaY) {
    final int childCount = getChildCount();
    if (childCount == 0) {
        return true;
    }

    final int firstTop = getChildAt(0).getTop();
    final int lastBottom = getChildAt(childCount - 1).getBottom();

    final Rect listPadding = mListPadding;

    int effectivePaddingTop = 0;
    int effectivePaddingBottom = 0;
    //...
    final int spaceAbove = effectivePaddingTop - firstTop;
    final int end = getHeight() - effectivePaddingBottom;
    final int spaceBelow = lastBottom - end;

    final int height = getHeight() - mPaddingBottom - mPaddingTop;
    if (deltaY < 0) {
        deltaY = Math.max(-(height - 1), deltaY);
    } else {
        deltaY = Math.min(height - 1, deltaY);
    }

    if (incrementalDeltaY < 0) {
        incrementalDeltaY = Math.max(-(height - 1), incrementalDeltaY);
    } else {
        incrementalDeltaY = Math.min(height - 1, incrementalDeltaY);
    }

    final int firstPosition = mFirstPosition;

    // Update our guesses for where the first and last views are
    if (firstPosition == 0) {
        mFirstPositionDistanceGuess = firstTop - listPadding.top;
    } else {
        mFirstPositionDistanceGuess += incrementalDeltaY;
    }
    if (firstPosition + childCount == mItemCount) {
        mLastPositionDistanceGuess = lastBottom + listPadding.bottom;
    } else {
        mLastPositionDistanceGuess += incrementalDeltaY;
    }
    //判断是否在最顶部且手指从上向下滑动，是的话即不能向下滑动了
    final boolean cannotScrollDown = (firstPosition == 0 &&
            firstTop >= listPadding.top && incrementalDeltaY >= 0);
    //判断是否在最底部且手指从下向上滑动,是的话即不能向上滑动了
    final boolean cannotScrollUp = (firstPosition + childCount == mItemCount &&
            lastBottom <= getHeight() - listPadding.bottom && incrementalDeltaY <= 0);
       
    //注释0处，不能滑动，直接返回
    if (cannotScrollDown || cannotScrollUp) {
        return incrementalDeltaY != 0;
    }
    //注释1处，手指是从下向上滑动，incrementalDeltaY < 0
    final boolean down = incrementalDeltaY < 0;

    final boolean inTouchMode = isInTouchMode();
    if (inTouchMode) {
        hideSelector();
    }

    final int headerViewsCount = getHeaderViewsCount();
    final int footerViewsStart = mItemCount - getFooterViewsCount();

    int start = 0;
    int count = 0;

    if (down) {
        //注释2处，手指是从下向上滑动，incrementalDeltaY < 0，所以top是大于0的。
        int top = -incrementalDeltaY;
        if ((mGroupFlags & CLIP_TO_PADDING_MASK) == CLIP_TO_PADDING_MASK) {
            top += listPadding.top;
        }
        //注释3处，for循环
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getBottom() >= top) {
                break;
            } else {
                //注释3.1处，
                count++;
                int position = firstPosition + i;
                if (position >= headerViewsCount && position < footerViewsStart) {
                    // The view will be rebound to new data, clear any
                    // system-managed transient state.
                    child.clearAccessibilityFocus();
                    //注释3.2处
                    mRecycler.addScrapView(child, position);
                }
            }
        }
    } else {
        //注释4处，手指是从上向下滑动，incrementalDeltaY > 0
        int bottom = getHeight() - incrementalDeltaY;
        //...
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child.getTop() <= bottom) {
                break;
            } else {
                //注释4.1
                start = i;
                count++;
                int position = firstPosition + i;
                if (position >= headerViewsCount && position < footerViewsStart) {
                    // The view will be rebound to new data, clear any
                    // system-managed transient state.
                    child.clearAccessibilityFocus();
                    //注释4.2
                    mRecycler.addScrapView(child, position);
                }
            }
        }
    }

    mMotionViewNewTop = mMotionViewOriginalTop + deltaY;

    mBlockLayoutRequests = true;
        
    //注释5处，滚动出去的子View和ListView解除关联
    if (count > 0) {
        detachViewsFromParent(start, count);
        mRecycler.removeSkippedScrap();
    }

    //...

    //注释6处，核心滚动代码，根据incrementalDeltaY同步偏移所有的子View
    offsetChildrenTopAndBottom(incrementalDeltaY);

    if (down) {
        mFirstPosition += count;
    }

    final int absIncrementalDeltaY = Math.abs(incrementalDeltaY);
    //手指从上向下滑动，down为false，spaceAbove < absIncrementalDeltaY，表示第一个子View已经完全滑进屏幕了。需要填充新的子View。
    //手指从下向上滑动，down为true，spaceBelow < absIncrementalDeltaY，表示最后一个子View已经完全滑进屏幕了。需要填充新的子View。
    if (spaceAbove < absIncrementalDeltaY || spaceBelow < absIncrementalDeltaY) {
        //注释7处，填充新的子View
        fillGap(down);
    }

    mRecycler.fullyDetachScrapViews();
    //...
    invokeOnItemScrollListener();

    return false;
}

```

注释0处，不能滑动，直接返回。

注释1处，手指是从下向上滑动，incrementalDeltaY < 0。

注释2处，手指是从下向上滑动，incrementalDeltaY < 0，所以top是大于0的。

注释3处，for循环是什么意思呢？解释一下：

1. 比如我们手指从下向上滑动，滑动了500像素，那么`incrementalDeltaY = -500`， `top = -incrementalDeltaY =500`。
2. 那么ListView整体是要向上滑动500像素的。遍历ListView的所有的子View，View的bottom坐标小于500像素的都要滑出屏幕。

注释3.1处，记录要滑出的View的数量，注释3.2处将要滑出屏幕的View加入到 RecyclerBin的mScrapViews数组中等待以后复用。

同理，注释4处，手指是从上向下滑动，incrementalDeltaY > 0。

1. 比如我们手指从上向下滑动，滑动了500像素，那么`incrementalDeltaY = 500`。比如ListView的getHeight()=2000，那么`bottom = getHeight() - incrementalDeltaY = 1500`。

2. 那么ListView整体是要向下滑动500像素的。遍历ListView的所有的子View，View的top坐标大于1500像素的都要从滑出屏幕。

注释4.1处，记录要滑出的View的数量，注释4.2处将要滑出屏幕的View加入到RecyclerBin的mScrapViews数组中等待以后复用。

注释5处，滚动出去的子View和ListView解除关联。

注释6处，核心滚动代码,根据incrementalDeltaY同步偏移所有的子View。

```java
/**
 * 将所有子View的竖直方向上的位置偏移指定的像素数。
 *
 * @param offset 指定的像素偏移量。
 */
public void offsetChildrenTopAndBottom(int offset) {
    final int count = mChildrenCount;
    final View[] children = mChildren;
    boolean invalidate = false;

    for (int i = 0; i < count; i++) {
        final View v = children[i];
        v.mTop += offset;
        v.mBottom += offset;
        if (v.mRenderNode != null) {
            invalidate = true;
            v.mRenderNode.offsetTopAndBottom(offset);
        }
    }

    if (invalidate) {
        invalidateViewProperty(false, false);
    }
    notifySubtreeAccessibilityStateChangedIfNeeded();
}
```

ListView正是通过这种将所以子View偏移指定的像素数来实现滚动效果的，偏移出屏幕的子View会和ListView取消关联，并加入到RecyclerBin的mScrapViews数组中等待以后复用。

注意：这里顺便提一嘴，ScrollView是修改自身的scrollY来实现的滚动，从而显示所有的子View的，和ListView是有区别的，注意一下。

注释7处，当我们滑动的时候，会将一些子View滑出屏幕，也需要填充新的子View到屏幕中。

```java
@Override
void fillGap(boolean down) {
    final int count = getChildCount();
    if (down) {
        int paddingTop = 0;
        if ((mGroupFlags & CLIP_TO_PADDING_MASK) == CLIP_TO_PADDING_MASK) {
            paddingTop = getListPaddingTop();
        }
        final int startOffset = count > 0 ? getChildAt(count - 1).getBottom() + mDividerHeight :
                paddingTop;
        //手指从下向上滑动，down为true，向下填充新的View。
        fillDown(mFirstPosition + count, startOffset);
        correctTooHigh(getChildCount());
    } else {
        int paddingBottom = 0;
        if ((mGroupFlags & CLIP_TO_PADDING_MASK) == CLIP_TO_PADDING_MASK) {
            paddingBottom = getListPaddingBottom();
        }
        final int startOffset = count > 0 ? getChildAt(0).getTop() - mDividerHeight :
                getHeight() - paddingBottom;
        //手指从上向下滑动，down为false，向上填充新的View。
        fillUp(mFirstPosition - 1, startOffset);
        correctTooLow(getChildCount());
    }
}
```

AbsListView的onTouchUp方法

```java
private void onTouchUp(MotionEvent ev) {
    switch (mTouchMode) {
        case TOUCH_MODE_SCROLL:
        final int childCount = getChildCount();
        if (childCount > 0) {
            final int firstChildTop = getChildAt(0).getTop();
            final int lastChildBottom = getChildAt(childCount - 1).getBottom();
            final int contentTop = mListPadding.top;
            final int contentBottom = getHeight() - mListPadding.bottom;
            if (mFirstPosition == 0 && firstChildTop >= contentTop &&
                    mFirstPosition + childCount < mItemCount &&
                    lastChildBottom <= getHeight() - contentBottom) {
                mTouchMode = TOUCH_MODE_REST;
                //注释1处
                reportScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
            } else {
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);

                final int initialVelocity = (int)
                        (velocityTracker.getYVelocity(mActivePointerId) * mVelocityScale);
                // Fling if we have enough velocity and we aren't at a boundary.
                // Since we can potentially overfling more than we can overscroll, don't
                // allow the weird behavior where you can scroll to a boundary then
                // fling further.
                boolean flingVelocity = Math.abs(initialVelocity) > mMinimumVelocity;
                if (flingVelocity &&
                        !((mFirstPosition == 0 &&
                                firstChildTop == contentTop - mOverscrollDistance) ||
                          (mFirstPosition + childCount == mItemCount &&
                                lastChildBottom == contentBottom + mOverscrollDistance))) {
                        if (!dispatchNestedPreFling(0, -initialVelocity)) {
                        if (mFlingRunnable == null) {
                            mFlingRunnable = new FlingRunnable();
                        }
                        reportScrollStateChange(OnScrollListener.SCROLL_STATE_FLING);
                        //注释2处，可以fling    
                        mFlingRunnable.start(-initialVelocity);
                    } 
                } 
            }
        } 
        break;
        //...
    }
    //...
}
```
注释1处，手指抬起的时候没有触发fling，就将滑动状态置为SCROLL_STATE_IDLE。

注释2处，手指抬起的时候可以fling，直接起飞。

FlingRunnable类实现了Runnable接口。

FlingRunnable的start方法。

```java
void start(int initialVelocity) {
    int initialY = initialVelocity < 0 ? Integer.MAX_VALUE : 0;
    mLastFlingY = initialY;
    mScroller.setInterpolator(null);
    //注释1处，调用OverScroller的fling方法开始根据速度计算下一帧的坐标
    mScroller.fling(0, initialY, 0, initialVelocity,
            0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
    //注释2处，将mTouchMode置为TOUCH_MODE_FLING
    mTouchMode = TOUCH_MODE_FLING;
    mSuppressIdleStateChangeCall = false;
    //注释3处，post一个runnable对象，下一帧到来的时候会调用run方法。
    postOnAnimation(this);

    if (PROFILE_FLINGING) {
        if (!mFlingProfilingStarted) {
            Debug.startMethodTracing("AbsListViewFling");
            mFlingProfilingStarted = true;
        }
    }
}
```

注释1处，调用OverScroller的fling方法开始根据速度计算下一帧的坐标。

注释2处，将mTouchMode置为TOUCH_MODE_FLING。

注释3处，post一个runnable对象，下一帧到来的时候会调用FlingRunnable的run方法。

FlingRunnable的run方法。

```java
@Override
public void run() {
    switch (mTouchMode) {
        default:
            //停止fling
            endFling();
        return;

    case TOUCH_MODE_SCROLL:
        //fling已经结束了，直接return
        if (mScroller.isFinished()) {
            return;
        }
        // Fall through
        case TOUCH_MODE_FLING: {
            if (mDataChanged) {
                layoutChildren();
            }

            if (mItemCount == 0 || getChildCount() == 0) {
                endFling();
                return;
            }

            final OverScroller scroller = mScroller;
            //注释1处，more为true表示fling未结束
            boolean more = scroller.computeScrollOffset();
            //要到达的坐标
            final int y = scroller.getCurrY();

            // Flip sign to convert finger direction to list items direction
            //注释2处，上一帧和本次的滚动距离差值    
            int delta = mLastFlingY - y;

            // Pretend that each frame of a fling scroll is a touch scroll
            if (delta > 0) {
                //向上滚动。 List is moving towards the top. Use first view as mMotionPosition
                mMotionPosition = mFirstPosition;
                final View firstView = getChildAt(0);
                mMotionViewOriginalTop = firstView.getTop();

                // Don't fling more than 1 screen
                delta = Math.min(getHeight() - mPaddingBottom - mPaddingTop - 1, delta);
            } else {
                //向下滚动。 List is moving towards the bottom. Use last view as mMotionPosition
                int offsetToLast = getChildCount() - 1;
                mMotionPosition = mFirstPosition + offsetToLast;

                final View lastView = getChildAt(offsetToLast);
                mMotionViewOriginalTop = lastView.getTop();

                // Don't fling more than 1 screen
                delta = Math.max(-(getHeight() - mPaddingBottom - mPaddingTop - 1), delta);
            }

            // Check to see if we have bumped into the scroll limit
            View motionView = getChildAt(mMotionPosition - mFirstPosition);
            int oldTop = 0;
            if (motionView != null) {
                oldTop = motionView.getTop();
            }

            // Don't stop just because delta is zero (it could have been rounded)
            //注释3处，调用trackMotionScroll回收View，填充新的View。
            final boolean atEdge = trackMotionScroll(delta, delta);
            final boolean atEnd = atEdge && (delta != 0);
            if (atEnd) {
                //处理overScroll的情况，忽略。。。
            }
            if (more && !atEnd) {
                if (atEdge) invalidate();
                //注释4处
                mLastFlingY = y;
                postOnAnimation(this);
            } else {
                //注释5处
                endFling();
            }
            break;
        }
           
    }
}
```

注释1处，more为true表示fling未结束。

注释2处，计算上一帧和本次的滚动距离差值 。delta > 0 表示向上滚动否则是向下滚动。
  
注释3处，调用trackMotionScroll缓存滑出屏幕View，填充新的View。

注释4处，fling还没有结束，重新为mLastFlingY赋值，并继续postFlingRunnable，再下一帧到来的时候继续处理。

注释5处，如果fling结束那就结束。

FlingRunnable的endFling方法。


```java
void endFling() {
    //将mTouchMode置为TOUCH_MODE_REST
    mTouchMode = TOUCH_MODE_REST;
    //移除掉FlingRunnable，不再响应下一帧
    removeCallbacks(this);
    removeCallbacks(mCheckFlywheel);
   //...

}
```

总结：ListView的源码学习到此告一段落。源码阅读起来感觉不是太难，哈哈，主要是没有去抠每一个细节。接下来没有变化的话，要开始写RecyclerView相关的源码分析了。

参考链接：

* [Android ListView工作原理完全解析，带你从源码的角度彻底理解](https://blog.csdn.net/guolin_blog/article/details/44996879)
* [ListView源码分析](https://www.jianshu.com/p/7f95297b6271)
