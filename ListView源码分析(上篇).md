本篇要点

1. 设置适配器后的流程。
2. 自定义的适配器的getView方法的调用。
2. 适配器调用notifyDataSetChanged方法后的流程。

### 基本的使用方法

```xml
<ListView
    android:id="@+id/listView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

自定义适配器

```kotlin
class ListViewAdapter(
        context: Context,
        private val resource: Int,
        lists: ArrayList<MyBean>
) : ArrayAdapter<MyBean>(context, resource, lists) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        val bean = getItem(position)
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false)
            holder = ViewHolder()
            holder.textViewTitle = view.findViewById(R.id.tvTitle)
            holder.textViewDetail = view.findViewById(R.id.tvDetail)
            view.tag = holder
        } else {
            //返回的convertView不为null，复用
            view = convertView
            holder = view.tag as ViewHolder
        }
        //绑定数据
        holder.textViewTitle?.text = bean?.title
        holder.textViewDetail?.text = bean?.detail
        return view
    }

    //Holder类，用来提高listView的性能
    private class ViewHolder {
        var textViewTitle: TextView? = null
        var textViewDetail: TextView? = null
    }
}
```

ListView设置适配器

```java
adapter = ListViewAdapter(this, R.layout.item_list_view, list)
listView.adapter = adapter
```

### 源码分析

我们先从ListView的setAdapter方法开始

```java
@Override
public void setAdapter(ListAdapter adapter) {
    //移除老的观察者
    if (mAdapter != null && mDataSetObserver != null) {
        mAdapter.unregisterDataSetObserver(mDataSetObserver);
    }
    //注释1处，重置某些变量
    resetList();
    //注释2处，清空缓存中所有等待复用的View
    mRecycler.clear();

    //如果ListView添加了header或者footer，则包装适配器
    if (mHeaderViewInfos.size() > 0|| mFooterViewInfos.size() > 0) {
        mAdapter = wrapHeaderListAdapterInternal(mHeaderViewInfos, mFooterViewInfos, adapter);
    } else {
        mAdapter = adapter;
    }

    mOldSelectedPosition = INVALID_POSITION;
    mOldSelectedRowId = INVALID_ROW_ID;

    // AbsListView#setAdapter will update choice mode states.
    super.setAdapter(adapter);

    if (mAdapter != null) {
        mAreAllItemsSelectable = mAdapter.areAllItemsEnabled();
        mOldItemCount = mItemCount;
        //mItemCount，适配器中数据的数量，通常就是我们传入适配器中list的size
        mItemCount = mAdapter.getCount();
        checkFocus();
        //注册新的观察者，观察适配器的数据变化，数据变化后ListView会调用requestLayout方法
        mDataSetObserver = new AdapterDataSetObserver();
        mAdapter.registerDataSetObserver(mDataSetObserver);
        //mRecycler设置适配器ViewType的数量。
        mRecycler.setViewTypeCount(mAdapter.getViewTypeCount());

        //...
    } 
    //注释3处，最重要的一步，请求measure、layout、draw
    requestLayout();
}
```

ListView的resetList方法

```java
@Override
void resetList() {
    //...  
    //调用AbsListView的resetList方法
    super.resetList();
    
    //将布局模式设置为LAYOUT_NORMAL
    mLayoutMode = LAYOUT_NORMAL;
}
```

AbsListView的resetList方法

```java
void resetList() {
    //移除所有的子View
    removeAllViewsInLayout();
    //将mFirstPosition置为0
    mFirstPosition = 0;
    //mDataChanged置为false
    mDataChanged = false;
    mPositionScrollAfterLayout = null;
    mNeedSync = false;
    mPendingSync = null;
    mOldSelectedPosition = INVALID_POSITION;
    mOldSelectedRowId = INVALID_ROW_ID;
    setSelectedPositionInt(INVALID_POSITION);
    setNextSelectedPositionInt(INVALID_POSITION);
    mSelectedTop = 0;
    mSelectorPosition = INVALID_POSITION;
    mSelectorRect.setEmpty();
    invalidate();
}
```

setAdapter方法注释3处，最重要的一步，调用requestLayout方法请求measure、layout、draw。

### ListView 的测量过程

ListView 的 onMeasure 方法

```java
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // Sets up mListPadding
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    //高度
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int childWidth = 0;
    int childHeight = 0;
    int childState = 0;

    mItemCount = mAdapter == null ? 0 : mAdapter.getCount();
        

    if (heightMode == MeasureSpec.AT_MOST) {
        //注释1处，决定最终高度，最后一个参数是-1注意一下。
        heightSize = measureHeightOfChildren(widthMeasureSpec, 0, NO_POSITION, heightSize, -1);
    }
    //保存宽高信息
    setMeasuredDimension(widthSize, heightSize);

    mWidthMeasureSpec = widthMeasureSpec;
}
```

我们平时使用ListView的宽高一般都是 MATCH_PARENT，有时候高度会设置为WRAP_CONTENT，这个时候测量模式就是 MeasureSpec.AT_MOST 。注释1处，如果高度的是测量模式是 MeasureSpec.AT_MOST，则会调用measureHeightOfChildren方法。


ListView 的 measureHeightOfChildren 方法。

```java
/**
 * 测量指定范围内的子View的高度，返回的高度包括ListView的padding和分割线的高度。
 * 如果指定了最大高度，那么当测量高度到达最大高度以后测量会停止测量。
 *
 * @param widthMeasureSpec The width measure spec to be given to a child's
 *            {@link View#measure(int, int)}.
 * @param startPosition The position of the first child to be shown.
 * @param endPosition The (inclusive) position of the last child to be
 *            shown. Specify {@link #NO_POSITION} if the last child should be
 *            the last available child from the adapter.
 * @param maxHeight 如果指定范围内子View的累加高度超过了maxHeight，就返回maxHeight。

 * @param disallowPartialChildPosition 通常情况下，返回的高度是否只包括完整的View ，暂时不关注。

 * @return The height of this ListView with the given children.
 */
final int measureHeightOfChildren(int widthMeasureSpec, int startPosition, int endPosition,
        int maxHeight, int disallowPartialChildPosition) {
    final ListAdapter adapter = mAdapter;
    if (adapter == null) {
        return mListPadding.top + mListPadding.bottom;
    }

    // 指定范围内子View的所有累加高度，包括ListView的 padding 和 分割线高度。
    int returnedHeight = mListPadding.top + mListPadding.bottom;
    final int dividerHeight = mDividerHeight;
    // The previous height value that was less than maxHeight and contained
    // no partial children
    int prevHeightWithoutPartialChild = 0;
    int i;
    View child;

    // mItemCount - 1 since endPosition parameter is inclusive
    endPosition = (endPosition == NO_POSITION) ? adapter.getCount() - 1 : endPosition;
    final AbsListView.RecycleBin recycleBin = mRecycler;
    //注释1处，在MeasureSpec.AT_MOST模式下是否在测量过程中将View加入到recycleBin，默认是true
    final boolean recyle = recycleOnMeasure();
    final boolean[] isScrap = mIsScrap;

    for (i = startPosition; i <= endPosition; ++i) {
        //获取子View
        child = obtainView(i, isScrap);

        measureScrapChild(child, i, widthMeasureSpec, maxHeight);

        if (i > 0) {
            // Count the divider for all but one child
            returnedHeight += dividerHeight;
        }

        // 是否在测量过程中将 View 加入到 recycleBin
        if (recyle && recycleBin.shouldRecycleViewType(
                ((LayoutParams) child.getLayoutParams()).viewType)) {
            recycleBin.addScrapView(child, -1);
        }

        returnedHeight += child.getMeasuredHeight();

        if (returnedHeight >= maxHeight) {
            //...
            return maxHeight;
        }
        //...
    }

    // 指定范围内所有的子View累加高度没有超过 maxHeight，就返回returnedHeight。
    return returnedHeight;
}
```

注释1处，在MeasureSpec.AT_MOST模式下是否在测量过程中将View加入到recycleBin，默认是true。那么就是说在MeasureSpec.AT_MOST模式下，第一次在测量过程中，就会通过obtainView获取View（第一次测量获取的View是新创建的）并加入到缓存recycleBin中。

这里我们先不关注这种场景，我们以ListView的宽高都是都是 MATCH_PARENT 的情况，即宽高的测量模式都是  MeasureSpec.EXACTLY 模式来分析。所以我们简单的认为ListView 的 onMeasure 方法就是简单的保存了宽高信息即可。


### ListView 的布局过程

ListView 的 onLayout 方法

注意：开始第一次布局的时候，此时所有的数据都在适配器中，而ListView是没有子View的(即ListView还没有调用过 addViewInLayout 方法添加子View)，所以 getChildCount = 0 。

AbsListView 的 onLayout 方法。

```java
@Override
protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);

    //标记正在layout，避免重复layout
    mInLayout = true;
    //注释1处，此时获取的 childCount 为 0 。
    final int childCount = getChildCount();
    if (changed) {
    //注释2处，ListView位置或者大小发生了改变，强制子View在下一个layout过程中重新布局
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).forceLayout();
        }
        mRecycler.markChildrenDirty();
    }
    //注释3处，布局子View
    layoutChildren();

    //...
    //布局结束后，将mInLayout置为false
    mInLayout = false;
}
```

注释1处，此时获取的 childCount 为 0 。

注释2处，ListView位置或者大小发生了改变，changed为true。强制子View在下一个layout过程中重新布局。比如我们在第一次布局测量出来ListView的宽高是1080 * 1960 。这时候changed就是true。

注释3处，布局子View。

ListView的 layoutChildren 方法。

```java
@Override
protected void layoutChildren() {
    //如果阻止布局就直接返回。
    final boolean blockLayoutRequests = mBlockLayoutRequests;
    if (blockLayoutRequests) {
        return;
    }

    mBlockLayoutRequests = true;

    try {
        super.layoutChildren();

        invalidate();

        if (mAdapter == null) {
            resetList();
            invokeOnItemScrollListener();
            return;
        }
        //布局子View最上面的坐标
        final int childrenTop = mListPadding.top;
        //布局子View最下面的坐标
        final int childrenBottom = mBottom - mTop - mListPadding.bottom;
        //注释1处，记录ListView当前的子View个数，第一次布局的时候为0。
        final int childCount = getChildCount();

        int index = 0;
        int delta = 0;

        //...

        //只有在调用adapter.notifyDataSetChanged()方法一直到layout()布局结束，dataChanged为true,默认为false
        boolean dataChanged = mDataChanged;
        if (dataChanged) {
            handleDataChanged();
        }

        //mItemCount是mAdapter.getCount()返回的数据的数量
        if (mItemCount == 0) {
            resetList();
            invokeOnItemScrollListener();
            return;
        } else if (mItemCount != mAdapter.getCount()) {
        //在布局过程中，adapter的数据发生了改变但是没调用notifyDataSetChanged方法会抛出异常。
            throw new IllegalStateException("The content of the adapter has changed but "
                    + "ListView did not receive a notification. Make sure the content of "
                    + "your adapter is not modified from a background thread, but only from "
                    + "the UI thread. Make sure your adapter calls notifyDataSetChanged() "
                    + "when its content changes. [in ListView(" + getId() + ", " + getClass()
                    + ") with Adapter(" + mAdapter.getClass() + ")]");
        }

        //...
        final int firstPosition = mFirstPosition;
        final RecycleBin recycleBin = mRecycler;
        if (dataChanged) {//数据发生了改变，将所有的子View加入到RecycleBin。这些View将来可能会被复用。
            for (int i = 0; i < childCount; i++) {
                recycleBin.addScrapView(getChildAt(i), firstPosition+i);
            }
        } else {
            //注释2处，dataChanged为false，会走到这里
            recycleBin.fillActiveViews(childCount, firstPosition);
        }

        //注释3处，将子View和ListView取消关联。调用此方法以后getChildCount返回值为0。
        detachAllViewsFromParent();
        recycleBin.removeSkippedScrap();
        //测量模式默认是LAYOUT_NORMAL，会走到 default分支
        switch (mLayoutMode) {
        //...
        default:
            //注释4处，此时childCount为0
            if (childCount == 0) {
                //mStackFromBottom标记是否从ListView的底部向上填充，默认是false
                if (!mStackFromBottom) {
                    final int position = lookForSelectablePosition(0, true);
                    setSelectedPositionInt(position);
                    //注释5处
                    sel = fillFromTop(childrenTop);
                } else {
                    final int position = lookForSelectablePosition(mItemCount - 1, false);
                    setSelectedPositionInt(position);
                    sel = fillUp(mItemCount - 1, childrenBottom);
                }
            } else {
                if (mSelectedPosition >= 0 && mSelectedPosition < mItemCount) {
                    sel = fillSpecific(mSelectedPosition,
                            oldSel == null ? childrenTop : oldSel.getTop());
                } else if (mFirstPosition < mItemCount) {
                    sel = fillSpecific(mFirstPosition,
                            oldFirst == null ? childrenTop : oldFirst.getTop());
                } else {
                    sel = fillSpecific(0, childrenTop);
                }
            }
            break;
        }

        //注释6处，recycleBin将=mActiveViews没有用到的View移动到mScrapViews中。
        recycleBin.scrapActiveViews();

        //...
        //将mLayoutMode置为LAYOUT_NORMAL
        mLayoutMode = LAYOUT_NORMAL;
        //将mDataChanged置为false
        mDataChanged = false;
        if (mPositionScrollAfterLayout != null) {
            post(mPositionScrollAfterLayout);
            mPositionScrollAfterLayout = null;
        }
            //...
    }
}
```

注释1处，记录ListView当前的子View个数，第一次布局的时候为0。

注释2处，dataChanged为false，会走到这里。

```java
recycleBin.fillActiveViews(childCount, firstPosition);
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

    //noinspection MismatchedReadAndWriteOfArray
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

如果ListView已经有子View了，即childCount大于0，就将子View添加到mActiveViews数组中，第一次布局的时候childCount为0，该方法调用无意义。


注释3处，调用detachAllViewsFromParent方法将子View和ListView取消关联。

```java
detachAllViewsFromParent();
```

此时ListView中是没有子View的，所以调用此方法也没意义。注意：ViewGroup的子类调用此方法以后getChildCount返回值为0。

注释4处，此时childCount为0。

mStackFromBottom标记是否从ListView的底部向上填充，默认是false，所以会走到注释5处。

```java
sel = fillFromTop(childrenTop);
```

ListView的 fillFromTop方法。

```java
/**
 * 从mFirstPosition开始，从上到下填充ListView。
 *
 * @param nextTop 第一个要被绘制的itemView的top坐标。
 *
 * @return 当前选中的itemView
 */
private View fillFromTop(int nextTop) {
    mFirstPosition = Math.min(mFirstPosition, mSelectedPosition);
    mFirstPosition = Math.min(mFirstPosition, mItemCount - 1);
    if (mFirstPosition < 0) {
        mFirstPosition = 0;
    }
    return fillDown(mFirstPosition, nextTop);
}
```

ListView的 fillDown 方法

```java
/**
 * 从pos开始向下填充ListView。
 *
 * @param pos 第一个要填充到ListView的位置。
 *
 * @param nextTop 第一个要填充到ListView的位置的itemView的top坐标。
 *
 * @return 如果当前选中的itemView出现在我们的绘制范围内的话，则返回该itemView。否则返回null。
 */
private View fillDown(int pos, int nextTop) {
    View selectedView = null;
    
    //ListView的底部坐标
    int end = (mBottom - mTop);

    //如果要填充的itemView的top坐标小于end并且pos小于mItemCount，则循环填充。
    while (nextTop < end && pos < mItemCount) {
        // is this the selected item?
        boolean selected = pos == mSelectedPosition;
        //注释1处，创建并将子View添加到ListView中
        View child = makeAndAddView(pos, nextTop, true, mListPadding.left, selected);
        
        //累加nextTop，包含了分割线的高度mDividerHeight
        nextTop = child.getBottom() + mDividerHeight;
        if (selected) {
            selectedView = child;
        }
        //累加pos
        pos++;
    }

    return selectedView;
}

```

注释1处，创建并将子View添加到ListView中。

ListView 的 makeAndAddView 方法。

```java
/**
 * 获取View并将其添加到子View列表中。View可以是新创建的，从未使用的View转化来的，或者从RecyclerBin中获取的。
 *
 * @param position 该View在ListView的mChildren数组中的位置。
 * @param y 该View被添加到ListView的top坐标或者bottom坐标。
 * @param flow true 将View的上边界和y对齐。即 y是View的top坐标。 false 将View的下边界和y对齐。即 y是View的bottom坐标。
 * @param childrenLeft View的left坐标。
 * @param selected 这个位置上的View是否被选中。

 * @return 该View
 */
private View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected) {
     
     // 默认情况mDataChanged==false,在调用adapter.notifyDataSetChanged()之后的
     // layout阶段中mDataChanged == true
     if (!mDataChanged) {
     //注释1处，第一次布局的时候childCount为0，getActiveView是获取不到的。
     final View activeView = mRecycler.getActiveView(position);
         if (activeView != null) {
             //找到可直接使用的View，将其添加到ListView中去。
             setupChild(activeView, position, y, flow, childrenLeft, selected, true);
             return activeView;
         }
     }

     //注释2处，为该位置创建一个新的View，或者从一个未被使用的View转换。
     final View child = obtainView(position, mIsScrap);
   
     //注释3处 通过obtainView获取到的View需要被重新测量和布局。注意调用setupChild方法的最后一个参数。
     setupChild(child, position, y, flow, childrenLeft, selected, mIsScrap[0]);

     return child;
}
```

注释1处，首先从mRecycler的mActiveViews数组中尝试获取可复用的View，在前面layoutChildren方法中，如果mDataChanged==false会尝试把View放到mRecycler的mActiveViews中保存，mRecycler.getActiveView()中对应position的View被获取到之后本身就不再保存。第一次布局的时候childCount为0，mRecycler的mActiveViews数组是空数组，getActiveView是获取不到的。

注释2处，为该位置创建一个新的View，或者从一个未被使用的View转换。

AbsListView的obtainView方法。

```java
/**
 * 获取一个View并让它展示指定位置上的数据。
 *
 * @param position the position to display
 * @param outMetadata
 *
 * @return 返回一个展示了指定位置上的数据的View
 */
View obtainView(int position, boolean[] outMetadata) {
    //...
    //注释1处，先从mRecycler的mScrapViews数组中获取一个在滑动时候废弃保存的子view
    final View scrapView = mRecycler.getScrapView(position);
    //注释2处，调用平时写的adapter的getView()方法获取View，注意第二个参数传入了scrapView
    final View child = mAdapter.getView(position, scrapView, this);
    if (scrapView != null) {
        if (child != scrapView) {
            //注释3处，如果child != scrapView和不相等，就是说我们无法复用scrapView，
            //那么就再将scrapView保存起来后续复用。
            mRecycler.addScrapView(scrapView, position);
        }
        //...
    }

    //...
    //返回获取到的View
    return child;
}
```

注释1处，先从mRecycler的mScrapViews数组中获取一个在滑动时候废弃保存的子view

我们先看下注释2处，调用平时写的adapter的getView()方法获取View，注意第二个参数传入了scrapView。

```kotlin

override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view: View
    val holder: ViewHolder
    val bean = getItem(position)
    //开始convertView为null，创建新的View返回
    if (convertView == null) {
        //注意这里会为创建的View设置ListView的布局参数AbsListView.LayoutParams
        view = LayoutInflater.from(context).inflate(resource, parent, false)
        holder = ViewHolder()
        holder.textViewTitle = view.findViewById(R.id.tvTitle)
        holder.textViewDetail = view.findViewById(R.id.tvDetail)
        view.tag = holder
    } else {
        //convertView不为null的时候，直接使用convertView
        view = convertView
        holder = view.tag as ViewHolder
    }
    //将View与数据绑定以后返回
    holder.textViewTitle?.text = bean?.title
    holder.textViewDetail?.text = bean?.detail
    notifyDataSetChanged()
    return view
}
```

我们回到makeAndAddView方法的注释3处ListView的 setupChild 方法。


```java
/**
 * 将一个子View添加到ListView中去，如果必要的话测量子View并将子View布局在合适的位置。
 *
 * @param child the view to add
 * @param position the position of this child
 * @param y 要添加的View的top坐标或者bottom坐标
 * @param flowDown true的话，y参数是要添加的View的top坐标，false，y参数是View的bottom坐标。
 * @param childrenLeft left edge where children should be positioned
 * @param selected {@code true} if the position is selected, {@code false}
 *                 otherwise
 * @param isAttachedToWindow 要添加的View是否已经添加到window上了。
 */
private void setupChild(View child, int position, int y, boolean flowDown, int childrenLeft,
        boolean selected, boolean isAttachedToWindow) {
    Trace.traceBegin(Trace.TRACE_TAG_VIEW, "setupListItem");

    final boolean isSelected = selected && shouldShowSelector();
    final boolean updateChildSelected = isSelected != child.isSelected();
    final int mode = mTouchMode;
    final boolean isPressed = mode > TOUCH_MODE_DOWN && mode < TOUCH_MODE_SCROLL
            && mMotionPosition == position;
    final boolean updateChildPressed = isPressed != child.isPressed();
    //注释1处，如果child未被添加到ListView中过，或者选中状态发生了改变，或者调用了child的forceLayout方法，则需要重新测量child。
    final boolean needToMeasure = !isAttachedToWindow || updateChildSelected
            || child.isLayoutRequested();

    // Respect layout params that are already in the view. Otherwise make
    // some up...
    AbsListView.LayoutParams p = (AbsListView.LayoutParams) child.getLayoutParams();
    if (p == null) {
        p = (AbsListView.LayoutParams) generateDefaultLayoutParams();
    }
    //child的类型viewType
    p.viewType = mAdapter.getItemViewType(position);
    p.isEnabled = mAdapter.isEnabled(position);

    //...
    
    if ((isAttachedToWindow && !p.forceAdd) || (p.recycledHeaderFooter
            && p.viewType == AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER)) {
        //注释2处，表明child是曾经使用过的，直接将child关联到ListView即可。
        attachViewToParent(child, flowDown ? -1 : 0, p);

        //...
        
    } else {
        p.forceAdd = false;
        if (p.viewType == AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER) {
            p.recycledHeaderFooter = true;
        }
        //注释3处，将child添加到ListView。
        addViewInLayout(child, flowDown ? -1 : 0, p, true);
        //...
    }

    //需要测量child
    if (needToMeasure) {
        //...
        //注释4处，测量Child
        child.measure(childWidthSpec, childHeightSpec);
    } else {
        //清除child的FLAG_FORCE_LAYOUT标记,避免多次测量和布局View
        cleanupLayoutState(child);
    }

    final int w = child.getMeasuredWidth();
    final int h = child.getMeasuredHeight();
    final int childTop = flowDown ? y : y - h;

    if (needToMeasure) {
        final int childRight = childrenLeft + w;
        final int childBottom = childTop + h;
        //注释5处，布局child
        child.layout(childrenLeft, childTop, childRight, childBottom);
    } else {
        //修正child的坐标
        child.offsetLeftAndRight(childrenLeft - child.getLeft());
        child.offsetTopAndBottom(childTop - child.getTop());
    }

    //...

    Trace.traceEnd(Trace.TRACE_TAG_VIEW);
}
```

注释1处，如果child未被添加到ListView中过，或者选中状态发生了改变，或者调用了child的forceLayout方法，则需要重新测量child。

注意当ListView的宽高测量模式的都是EXACTLY的情况下，在自己的测量过程中是没有测量child的，所以在第一次调动setupChild会测量子View。

注释2处，表明child是曾经使用过的，直接将child关联到ListView即可。

ViewGroup的 attachViewToParent 方法。

```java

protected void attachViewToParent(View child, int index, LayoutParams params) {
    child.mLayoutParams = params;

    if (index < 0) {
        index = mChildrenCount;
    }

    //将child加入到ListView的mChildren中
    addInArray(child, index);
    //将child的父View设置为ListView
    child.mParent = this;
    child.mPrivateFlags = (child.mPrivateFlags & ~PFLAG_DIRTY_MASK
                       & ~PFLAG_DRAWING_CACHE_VALID)
            | PFLAG_DRAWN | PFLAG_INVALIDATED;
    this.mPrivateFlags |= PFLAG_INVALIDATED;
    
}
```

注释3处，需要调用addViewInLayout将child添加到ListView。

ViewGroup的 addViewInLayout 方法。再往里面的逻辑我们就不跟了。大致逻辑就是将child添加到ViewGroup的mChildren中，并将child attach 到当前窗口。只有调用完addViewInLayout方法以后，ListView的getChildCount才大于0。


```java

protected boolean addViewInLayout(View child, int index, LayoutParams params, boolean preventRequestLayout) {
    if (child == null) {
        throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
    }
    child.mParent = null;
    //调用addViewInner添加。
    addViewInner(child, index, params, preventRequestLayout);
    child.mPrivateFlags = (child.mPrivateFlags & ~PFLAG_DIRTY_MASK) | PFLAG_DRAWN;
    return true;
}
```

注释4处，测量Child。

注释注释5处，布局child。

到这里ListView的布局过程就结束了。

注意：第一次布局结束子View已经测量完毕并布局到正确的位置了，接下来就是绘制了。

### ListView绘制子View

我们先看ListView的dispatchDraw方法。

```java
@Override
protected void dispatchDraw(Canvas canvas) {
    final int dividerHeight = mDividerHeight;
    final Drawable overscrollHeader = mOverScrollHeader;
    final Drawable overscrollFooter = mOverScrollFooter;
    final boolean drawOverscrollHeader = overscrollHeader != null;
    final boolean drawOverscrollFooter = overscrollFooter != null;
    final boolean drawDividers = dividerHeight > 0 && mDivider != null;
    //需要绘制分割线，则先绘制绘制分割线
    if (drawDividers || drawOverscrollHeader || drawOverscrollFooter) {
        // Only modify the top and bottom in the loop, we set the left and right here
        final Rect bounds = mTempRect;
        bounds.left = mPaddingLeft;
        bounds.right = mRight - mLeft - mPaddingRight;

        final int count = getChildCount();
        //...
        if (!mStackFromBottom) {
            int bottom = 0;
            //...
            for (int i = 0; i < count; i++) {
                final int itemIndex = (first + i);
                final boolean isHeader = (itemIndex < headerCount);
                final boolean isFooter = (itemIndex >= footerLimit);
                if ((headerDividers || !isHeader) && (footerDividers || !isFooter)) {
                    final View child = getChildAt(i);
                    bottom = child.getBottom();
                    final boolean isLastItem = (i == (count - 1));

                    if (drawDividers && (bottom < listBottom)
                            && !(drawOverscrollFooter && isLastItem)) {
                        final int nextIndex = (itemIndex + 1);
                        // Draw dividers between enabled items, headers
                        // and/or footers when enabled and requested, and
                        // after the last enabled item.
                        if (adapter.isEnabled(itemIndex) && (headerDividers || !isHeader
                                && (nextIndex >= headerCount)) && (isLastItem
                                || adapter.isEnabled(nextIndex) && (footerDividers || !isFooter
                                        && (nextIndex < footerLimit)))) {
                            bounds.top = bottom;
                            bounds.bottom = bottom + dividerHeight;
                            //绘制分割线
                            drawDivider(canvas, bounds, i);
                        }
                        //...
                    }
                }
            }
            //...
        } 
        //...
    }
    // 绘制子View
    super.dispatchDraw(canvas);
}
```


ListView重写了dispatchDraw方法，需要绘制分割线的话，ListView会先绘制分割线，再绘制子View。

当绘制结束以后，ListView就可以正常显示了。

注意：在测试过程中发现，ListView会经过至少两次测量和布局过程。不知道第二次布局是怎么触发的。

```kotlin 
class MyListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ListView(context, attrs, defStyleAttr) {

    private val TAG: String = "MyListView"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i(TAG, "onMeasure: measuredWidth = $measuredWidth , measuredHeight =$measuredHeight")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.i(TAG, "onLayout: ${childCount} measuredWidth = $measuredWidth , measuredHeight =$measuredHeight changed = $changed")

    }
}
```

打印日志

![ListView两次布局.png](https://upload-images.jianshu.io/upload_images/3611193-41004c8d57011840.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


我们也来分析一下在第二次布局过程中layoutChildren方法的逻辑。

```java
@Override
protected void layoutChildren() {
    //如果阻止布局就直接返回。
    final boolean blockLayoutRequests = mBlockLayoutRequests;
    if (blockLayoutRequests) {
        return;
    }

    mBlockLayoutRequests = true;

    try {
        super.layoutChildren();

        invalidate();

        if (mAdapter == null) {
            resetList();
            invokeOnItemScrollListener();
            return;
        }
        //布局子View最上面的坐标
        final int childrenTop = mListPadding.top;
        //布局子View最下面的坐标
        final int childrenBottom = mBottom - mTop - mListPadding.bottom;
        //注释1处，记录ListView当前的子View个数，第二次布局的时候大于0。
        final int childCount = getChildCount();

        int index = 0;
        int delta = 0;

        //...

        //只有在调用adapter.notifyDataSetChanged()方法一直到layout()布局结束，dataChanged为true,默认为false
        boolean dataChanged = mDataChanged;
        if (dataChanged) {
            handleDataChanged();
        }

        //mItemCount是mAdapter.getCount()返回的数据的数量
        if (mItemCount == 0) {
            resetList();
            invokeOnItemScrollListener();
            return;
        } else if (mItemCount != mAdapter.getCount()) {
        //在布局过程中，adapter的数据发生了改变但是没调用notifyDataSetChanged方法会抛出异常。
            throw new IllegalStateException("The content of the adapter has changed but "
                    + "ListView did not receive a notification. Make sure the content of "
                    + "your adapter is not modified from a background thread, but only from "
                    + "the UI thread. Make sure your adapter calls notifyDataSetChanged() "
                    + "when its content changes. [in ListView(" + getId() + ", " + getClass()
                    + ") with Adapter(" + mAdapter.getClass() + ")]");
        }

        //...
        // 将所有的子View加入到RecycleBin。这些View将来可能会被复用。
        // These views will be reused if possible
        final int firstPosition = mFirstPosition;
        final RecycleBin recycleBin = mRecycler;
        if (dataChanged) {//数据发生了改变，将所有的子View加入到RecycleBin。
            for (int i = 0; i < childCount; i++) {
                recycleBin.addScrapView(getChildAt(i), firstPosition+i);
            }
        } else {
            //注释2处，dataChanged为false，会走到这里
            recycleBin.fillActiveViews(childCount, firstPosition);
        }

        //注释3处，将子View和ListView取消关联。调用此方法以后getChildCount返回值为0。
        detachAllViewsFromParent();
        recycleBin.removeSkippedScrap();
        //测量模式默认是LAYOUT_NORMAL，会走到 default分支
        switch (mLayoutMode) {
        //...
        default:
            //注释4处，此时childCount为0条件不满足。
            if (childCount == 0) {
                //mStackFromBottom标记是否从ListView的底部向上填充，默认是false
                if (!mStackFromBottom) {
                    final int position = lookForSelectablePosition(0, true);
                    setSelectedPositionInt(position);
                    //注释5处
                    sel = fillFromTop(childrenTop);
                } else {
                    final int position = lookForSelectablePosition(mItemCount - 1, false);
                    setSelectedPositionInt(position);
                    sel = fillUp(mItemCount - 1, childrenBottom);
                }
            } else {
                if (mSelectedPosition >= 0 && mSelectedPosition < mItemCount) {
                    sel = fillSpecific(mSelectedPosition,
                            oldSel == null ? childrenTop : oldSel.getTop());
                } else if (mFirstPosition < mItemCount) {
                    //注释6处
                    sel = fillSpecific(mFirstPosition,
                            oldFirst == null ? childrenTop : oldFirst.getTop());
                } else {
                    sel = fillSpecific(0, childrenTop);
                }
            }
            break;
        }

        //注释7处，recycleBin将=mActiveViews没有用到的View移动到mScrapViews中。
        recycleBin.scrapActiveViews();

        //...
        //将mLayoutMode置为LAYOUT_NORMAL
        mLayoutMode = LAYOUT_NORMAL;
        //将mDataChanged置为false
        mDataChanged = false;
        if (mPositionScrollAfterLayout != null) {
            post(mPositionScrollAfterLayout);
            mPositionScrollAfterLayout = null;
        }
            //...
    }
}
```

注释1处，记录ListView当前的子View个数，第二次布局的时候大于0。

注释2处，dataChanged为false，会走到这里。

RecycleBin的fillActiveViews方法。此时childCount大于0，会将ListView的所有子View加入到mActiveViews中。

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

    //noinspection MismatchedReadAndWriteOfArray
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

//注释3处，将子View和ListView取消关联。调用此方法以后getChildCount返回值为0。也就是说ListView不存在子View了。

```java
detachAllViewsFromParent();
```

然后注释6处条件会满足。


```java
/**
 * 将一个指定的itemView放置在屏幕上的指定位置上，然后从这个位置开始向上和向下填充ListView。
 *
 * @param position 指定的itemView要摆放的位置。
 * @param top 指定的itemView的top坐标。
 *
 * @return 选中的View，如果选中的View在可见区域之外，返回null。
 */
private View fillSpecific(int position, int top) {
    boolean tempIsSelected = position == mSelectedPosition;
    //先填充该View
    View temp = makeAndAddView(position, top, true, mListPadding.left, tempIsSelected);
    // Possibly changed again in fillUp if we add rows above this one.
    mFirstPosition = position;

    View above;
    View below;

    final int dividerHeight = mDividerHeight;
    if (!mStackFromBottom) {//默认情况下不会从ListView的底部开始填充，mStackFromBottom为false
        //向上填充
        above = fillUp(position - 1, temp.getTop() - dividerHeight);
        // This will correct for the top of the first view not touching the top of the list
        adjustViewsUpOrDown();
        //向下填充
        below = fillDown(position + 1, temp.getBottom() + dividerHeight);
        int childCount = getChildCount();
        if (childCount > 0) {
            //纠正LsitView的高度
            correctTooHigh(childCount);
        }
    } else {
        below = fillDown(position + 1, temp.getBottom() + dividerHeight);
        // This will correct for the bottom of the last view not touching the bottom of the list
        adjustViewsUpOrDown();
        above = fillUp(position - 1, temp.getTop() - dividerHeight);
        int childCount = getChildCount();
        if (childCount > 0) {
             correctTooLow(childCount);
        }
    }

    if (tempIsSelected) {
        return temp;
    } else if (above != null) {
        return above;
    } else {
        return below;
    }
}

```

不过其实它和fillUp()、fillDown()方法功能也是差不多的，主要的区别在于，fillSpecific()方法会优先将指定位置的子View先加载到屏幕上，然后再加载该子View往上以及往下的其它子View。

ListView的fillUp方法。

```java
/**
 * 从pos开始向上填充ListView。
 *
 * @param pos 第一个要填充到ListView的位置。
 *
 * @param nextBottom 要填充到ListView的位置的View的bottom坐标。
 *
 * @return 当前选中的的View
 */
private View fillUp(int pos, int nextBottom) {
    View selectedView = null;
 
    //这时候end为0
    int end = 0;
    if ((mGroupFlags & CLIP_TO_PADDING_MASK) == CLIP_TO_PADDING_MASK) {
        end = mListPadding.top;
    }
    
    //nextBottom大于0并且pos大于0，循环填充
    while (nextBottom > end && pos >= 0) {
        // is this the selected item?
        boolean selected = pos == mSelectedPosition;
        View child = makeAndAddView(pos, nextBottom, false, mListPadding.left, selected);
        //减小nextBottom，包含分割线的高度mDividerHeight
        nextBottom = child.getTop() - mDividerHeight;
        if (selected) {
            selectedView = child;
        }
        //减小pos，最终pos会减到-1
        pos--;
    }
    //最终pos会减到-1，所以mFirstPosition要加1变成0
    mFirstPosition = pos + 1;
    setVisibleRangeHint(mFirstPosition, mFirstPosition + getChildCount() - 1);
    return selectedView;
}
```

ListView的fillDown方法上面看过了，这里就不再看了。我们再看一下makeAndAddView方法的逻辑。

```java
private View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected) {
     
     // 默认情况mDataChanged==false,在调用adapter.notifyDataSetChanged()之后的
     // layout阶段中mDataChanged == true
     if (!mDataChanged) {
     //注释1处，这个时候getActiveView是可以获取到的。
     final View activeView = mRecycler.getActiveView(position);
         if (activeView != null) {
             //找到可直接使用的View，将其添加到ListView中去。
             setupChild(activeView, position, y, flow, childrenLeft, selected, true);
             return activeView;
         }
     }

     //注释2处，为该位置创建一个新的View，或者从一个未被使用的View转换。
     final View child = obtainView(position, mIsScrap);
   
     //注释3处 通过obtainView获取到的View需要被重新测量和布局。注意调用setupChild方法的最后一个参数。
     setupChild(child, position, y, flow, childrenLeft, selected, mIsScrap[0]);

     return child;
}
```

注释1处，这个时候getActiveView是可以获取到的，因为前面我们调用了RecycleBin的fillActiveViews()方法来缓存子View。然后会调用setupChild方法，并且最后一个参数传入的是true。

```java

/**
 * 将一个子View添加到ListView中去，如果必要的话测量子View并将子View布局在合适的位置。
 *
 * @param child the view to add
 * @param position the position of this child
 * @param y 要添加的View的top坐标或者bottom坐标
 * @param flowDown true的话，y参数是要添加的View的top坐标，false，y参数是View的bottom坐标。
 * @param childrenLeft left edge where children should be positioned
 * @param selected {@code true} if the position is selected, {@code false}
 *                 otherwise
 * @param isAttachedToWindow 要添加的View是否已经添加到window上了。
 */
private void setupChild(View child, int position, int y, boolean flowDown, int childrenLeft,
        boolean selected, boolean isAttachedToWindow) {

    //...
    //注释1处，条件满足
    if ((isAttachedToWindow && !p.forceAdd) || (p.recycledHeaderFooter
            && p.viewType == AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER)) {
        //注释2处，表明child是曾经使用过的，直接将child关联到ListView即可。
        attachViewToParent(child, flowDown ? -1 : 0, p);

        //...
    } else {
        p.forceAdd = false;
        if (p.viewType == AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER) {
            p.recycledHeaderFooter = true;
        }
        //注释3处，将child添加到ListView。
        addViewInLayout(child, flowDown ? -1 : 0, p, true);
        //...
    }
    //...

}
```

注释1处的条件满足。所以会执行注释2处的attachViewToParent()方法，直接将child关联到ListView即可。

注意：第一次Layout过程则是执行的是注释3处的addViewInLayout()方法。这两个方法最大的区别在于，如果我们需要向ViewGroup中添加一个新的子View，应该调用addViewInLayout()方法。
而如果是想要将一个之前detach的View重新attach到ViewGroup上，就应该调用attachViewToParent()方法。那么由于前面在layoutChildren()方法当中调用了detachAllViewsFromParent()方法，
这样ListView中所有的子View都是处于detach状态的，所以这里直接调用attachViewToParent()方法，直接将child关联到ListView即可。

