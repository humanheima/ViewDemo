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
        //获取到以后，从mActiveViews移除
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