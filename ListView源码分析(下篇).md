本篇主要看看AbsListView.RecycleBin这个类。

```java
public abstract class AbsListView extends AdapterView<ListAdapter> {

    /**
     * 用来存储未被使用到的views，这些views在下一次layout过程中应该会被用到从而避免创建新的views。
     */
    final RecycleBin mRecycler = new RecycleBin();
    
    /**
     * RecycleBin用来在布局过程中重用views。RecycleBin有两级存储：ActiveViews 和 ScrapViews。
     * ActiveViews是那些在布局开始的时候在屏幕上的View。在布局结束的时候，ActiveViews中未被用到的View会被移动到ScrapViews中。
     * ScrapViews是老的View，这些View可能被适配器使用，从而避免创建新的View。
     */
    class RecycleBin {
        
       /**
        * 在布局（layout）开始的时候在屏幕上的Views。在布局开始的时候会将屏幕上的Views添加到mActiveViews中。在布局结束的时候，
        * mActiveViews中的Views会被移动到mScrapViews中。mActiveViews中的Views一个范围内连续的Views。
        * 第一个View的存储在mActiveViews中下标为mFirstActivePosition的位置上。
        */
       private View[] mActiveViews = new View[0];
       /**
        * 未排序的Views可以作为 ConvertView被适配器使用。
        */
       private ArrayList<View>[] mScrapViews;

       /**
        * 当只有一种ViewType类型的时候，mCurrentScrap是mScrapViews的第一个元素。
        */
       private ArrayList<View> mCurrentScrap;



        
    }


}

```

RecycleBin的fillActiveViews方法。

```java

/**
 * 使用AbsListView的所有子View填充 ActiveViews 。
 *
 * @param childCount mActiveViews要持有的View的最少数量。
 * @param firstActivePosition ListView中第一个要存储在mActiveViews的子View在ListView中的位置。
 */
void fillActiveViews(int childCount, int firstActivePosition) {
    if (mActiveViews.length < childCount) {
        mActiveViews = new View[childCount];
    }
    mFirstActivePosition = firstActivePosition;

    final View[] activeViews = mActiveViews;
    for (int i = 0; i < childCount; i++) {
        View child = getChildAt(i);
        AbsListView.LayoutParams lp = (AbsListView.LayoutParams) child.getLayoutParams();
        //不要将headView和footView加入mActiveViews 
        if (lp != null && lp.viewType != ITEM_VIEW_TYPE_HEADER_OR_FOOTER) {
            activeViews[i] = child;
            lp.scrappedFromPosition = firstActivePosition + i;
        }
    }
}
```

RecycleBin的getActiveView方法。

```java
/**
 * @param position 在ListView当中的位置
 */
View getActiveView(int position) {
    int index = position - mFirstActivePosition;
    final View[] activeViews = mActiveViews;
    if (index >=0 && index < activeViews.length) {
        final View match = activeViews[index];
        //注释1处，获取到以后，从mActiveViews移除
        activeViews[index] = null;
        return match;
    }
    return null;
}
```

需要注意的是，mActiveViews当中所存储的View，一旦被获取了之后就会从mActiveViews当中移除，下次获取同样位置的View将会返回null，
也就是说mActiveViews不能被重复利用。

```java
/**
 * 将 mActiveViews剩余的所有views移动到mScrapViews中。
 */
void scrapActiveViews() {
    final View[] activeViews = mActiveViews;
    final boolean hasListener = mRecyclerListener != null;
    //是否有多个ViewType
    final boolean multipleScraps = mViewTypeCount > 1;
    
    ArrayList<View> scrapViews = mCurrentScrap;
    final int count = activeViews.length;
    for (int i = count - 1; i >= 0; i--) {
        //要加入到mScrapViews的View
        final View victim = activeViews[i];
        if (victim != null) {
            final AbsListView.LayoutParams lp
                       = (AbsListView.LayoutParams) victim.getLayoutParams();
            //获取View的viewType
            final int whichScrap = lp.viewType;
            //将View从mActiveViews移除
            activeViews[i] = null;
             //...
            //如果是多种viewType，根据viewType获取不同的存储列表
            if (multipleScraps) {
                scrapViews = mScrapViews[whichScrap];
            }
            
            lp.scrappedFromPosition = mFirstActivePosition + i;
            removeDetachedView(victim, false);
            //将victim加入到scrapViews
            scrapViews.add(victim);

            //...
        }
    }
    //注释1处，
    pruneScrapViews();
}
```

注释1处，确保mScrapViews中，缓存的各种viewType类型的数量不超过mActiveViews的长度。

```java
private void pruneScrapViews() {
    final int maxViews = mActiveViews.length;
    final int viewTypeCount = mViewTypeCount;
    final ArrayList<View>[] scrapViews = mScrapViews;
    for (int i = 0; i < viewTypeCount; ++i) {
        final ArrayList<View> scrapPile = scrapViews[i];
        int size = scrapPile.size();
        while (size > maxViews) {
            scrapPile.remove(--size);
        }
    }
    //...
}
```

```java
/**
 * 将View加入到scrap views。
 *
 * @param scrap 要添加的View
 * @param position View在父View中的位置。
 */
void addScrapView(View scrap, int position) {
    final AbsListView.LayoutParams lp = (AbsListView.LayoutParams) scrap.getLayoutParams();
    if (lp == null) {
        // Can't recycle, but we don't know anything about the view.
        // Ignore it completely.
        return;
    }

    lp.scrappedFromPosition = position;

    //不需要缓存的View
    final int viewType = lp.viewType;
    if (!shouldRecycleViewType(viewType)) {
        //...
        return;
    }

    clearScrapForRebind(scrap);
    if (mViewTypeCount == 1) {
        //只有一种ViewType类型，就加入到mCurrentScrap
        mCurrentScrap.add(scrap);
    } else {
        //多种种ViewType类型，根据viewType加入到不同的列表中
        mScrapViews[viewType].add(scrap);
    }

               
}
```

RecycleBin的getScrapView方法。

```java
View getScrapView(int position) {
    final int whichScrap = mAdapter.getItemViewType(position);
    if (whichScrap < 0) {
        return null;
    }
    if (mViewTypeCount == 1) {
        //只有一种ViewType类型
        return retrieveFromScrap(mCurrentScrap, position);
    } else if (whichScrap < mScrapViews.length) {
        //多种ViewType类型，根据类型获取存储的ArrayList<View>，然后调用retrieveFromScrap方法。
        return retrieveFromScrap(mScrapViews[whichScrap], position);
    }
    return null;
}
```

RecycleBin的getScrapView方法。


```java
private View retrieveFromScrap(ArrayList<View> scrapViews, int position) {
    final int size = scrapViews.size();
    if (size > 0) {
        for (int i = size - 1; i >= 0; i--) {
            final View view = scrapViews.get(i);
            final AbsListView.LayoutParams params =
                        (AbsListView.LayoutParams) view.getLayoutParams();
            //找到params.scrappedFromPosition和position相等的，直接返回。
            if (params.scrappedFromPosition == position) {
                final View scrap = scrapViews.remove(i);
                clearScrapForRebind(scrap);
                return scrap;
            }
        }
        //返回列表中最后一个。
        final View scrap = scrapViews.remove(size - 1);
        clearScrapForRebind(scrap);
        return scrap;
    } else {
        return null;
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
    //注释2处，post一个runnable对象，下一帧到来的时候会调用run方法。
    postOnAnimation(this);

    if (PROFILE_FLINGING) {
        if (!mFlingProfilingStarted) {
            Debug.startMethodTracing("AbsListViewFling");
            mFlingProfilingStarted = true;
        }
    }
}
```

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
            //上一帧和本次的滚动距离差值    
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
            //注释2处，调用trackMotionScroll回收View，填充新的View
            final boolean atEdge = trackMotionScroll(delta, delta);
            final boolean atEnd = atEdge && (delta != 0);
            if (atEnd) {
                //处理overScroll的情况，忽略。。。
            }
            if (more && !atEnd) {
                
                if (atEdge) invalidate();
                //注释3处，
                mLastFlingY = y;
                postOnAnimation(this);
            } else {
                //注释4处
                endFling();
            }
            break;
        }
           
    }
}
```

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

