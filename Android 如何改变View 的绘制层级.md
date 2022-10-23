正常来说，我们向一个ViewGroup中添加两个View，后添加的View会显示在最上层。举个例子：

首先自定义一个ViewGroup，用来添加两个View

```kotlin
class MyViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {


    var mFirstView: View? = null
        set(value) {
            field = value
            addView(
                value,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )
        }
    var mSecondView: View? = null
        set(value) {
            field = value
            addView(
                value,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )

        }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChild(mFirstView, widthMeasureSpec, heightMeasureSpec)
        measureChild(mSecondView, widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mFirstView?.layout(l, t, r, b)
        mSecondView?.layout(l, t, r, b)
    }

}
```

依次添加 mFirstView 和 mSecondView

```kotlin
 lateinit var fm_root_layout: FrameLayout
 override fun onCreate(savedInstanceState: Bundle?) {
     super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_my_view_group)
     fm_root_layout = findViewById(R.id.fm_root_layout)
         //先把 MyViewGroup 添加到 Activity 的根布局文件中
     val viewGroup = MyViewGroup(this)
     fm_root_layout.addView(viewGroup, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
     val firstView = TextView(this).apply {
         text = "firstView"
         textSize = 20 f
         gravity = Gravity.CENTER
         setTextColor(resources.getColor(R.color.white))
         background = ColorDrawable(resources.getColor(R.color.colorPrimary))
     }
     val secondView = TextView(this).apply {
             text = "secondView"
             textSize = 20 f
             gravity = Gravity.CENTER
             setTextColor(resources.getColor(R.color.white))
             background = ColorDrawable(resources.getColor(R.color.colorAccent))
         }
     //注释1处，依次添加 mFirstView 和 mSecondView
     viewGroup.mFirstView = firstView
     viewGroup.mSecondView = secondView
 }
```
注释1处，依次添加 mFirstView 和 mSecondView，我们添加View的时候，宽高都指定为 MATCH_PARENT。效果如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/84ee622ea5ee46f1a91a91fbe7b3e530.jpeg#pic_center)

如果说，我们不改变添加View 的顺序，想让 mFirstView 显示在最上层，怎么实现呢？答案是ViewGroup 的 getChildDrawingOrder 方法。我们先来实现效果，后面再分析原理。

修改  MyViewGroup

```kotlin
class MyViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //注释1处，记录两个View 的下标。
    var mFirstViewIndex = -1
    var mSecondViewIndex = -1
    var mFirstView: View? = null
        set(value) {
            field = value
            addView(
                value,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )

            mFirstViewIndex = indexOfChild(mFirstView)

        }
    var mSecondView: View? = null
        set(value) {
            field = value
            addView(
                value,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )

            mSecondViewIndex = indexOfChild(mSecondView)
        }

    init {

        //注释2处，这里调用的是 ViewGroup 的 setChildrenDrawingOrderEnabled 方法 并传递了 true。
        isChildrenDrawingOrderEnabled = true

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChild(mFirstView, widthMeasureSpec, heightMeasureSpec)
        measureChild(mSecondView, widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mFirstView?.layout(l, t, r, b)
        mSecondView?.layout(l, t, r, b)
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        //注释3处，修改绘制顺序，当要绘制 mFirstView的时候，绘制mSecondView。要绘制 mSecondView 的时候，绘制mFirstView。
        if (i == mFirstViewIndex) {
            return mSecondViewIndex
        } else if (i == mSecondViewIndex) {
            return mFirstViewIndex
        }
        return super.getChildDrawingOrder(childCount, i)
    }
}
```

注释1处，记录两个View 的下标。
注释2处，这里调用的是 ViewGroup 的 setChildrenDrawingOrderEnabled 方法并传递了 true。

注释3处，修改绘制顺序，当要绘制 mFirstView的时候，绘制mSecondView。要绘制 mSecondView 的时候，绘制mFirstView。效果如下所示：

![在这里插入图片描述](https://img-blog.csdnimg.cn/cb13b6517860482b9cf026078e56c896.jpeg#pic_center)

好了，效果实现了，接下来我们来分析一下原理。


子View 的绘制是在 ViewGroup 的 dispatchDraw 方法开始的。
```java
@Override
protected void dispatchDraw(Canvas canvas) {
    boolean usingRenderNodeProperties = canvas.isRecordingFor(mRenderNode);
    final int childrenCount = mChildrenCount;
    final View[] children = mChildren;
    int flags = mGroupFlags;
    //...
    int clipSaveCount = 0;
    
    // We will draw our child's animation, let's reset the flag
    mPrivateFlags &= ~PFLAG_DRAW_ANIMATION;
    mGroupFlags &= ~FLAG_INVALIDATE_REQUIRED;
    boolean more = false;
    final long drawingTime = getDrawingTime();
    if(usingRenderNodeProperties) canvas.insertReorderBarrier();
    final int transientCount = mTransientIndices == null ? 0 : mTransientIndices.size();
    int transientIndex = transientCount != 0 ? 0 : -1;
    // Only use the preordered list if not HW accelerated, since the HW pipeline will do the
    // draw reordering internally
    final ArrayList < View > preorderedList = usingRenderNodeProperties ? null : buildOrderedChildList();
    //注释1处，preorderedList == null 并且 isChildrenDrawingOrderEnabled() 为 true  那么使用自定义的绘制顺序。
    final boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
    for(int i = 0; i < childrenCount; i++) {
        //...
        //注释2处，在循环绘制的过程中，获取要绘制的child的下标。
        final int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
        //注释3处，根须下标获取要绘制的View。
        final View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
        if((child.mViewFlags & VISIBILITY_MASK) == VISIBLE || child.getAnimation() != null) {
            //注释4处，绘制View。
            more |= drawChild(canvas, child, drawingTime);
        }
    }
    //...
}
```

注释1处，preorderedList == null 并且 isChildrenDrawingOrderEnabled() 为 true  那么使用自定义的绘制顺序。 MyViewGroup 调用了 ViewGroup 的 setChildrenDrawingOrderEnabled 方法并传递了 true。

```java
/**
 * 告诉 ViewGroup 是否 使用 {@link #getChildDrawingOrder(int, int)} 方法中定义子View的绘制顺序。
 * <p>
 * Note that {@link View#getZ() Z} reordering, done by {@link #dispatchDraw(Canvas)},
 * will override custom child ordering done via this method.
 *
 * @param enabled true if the order of the children when drawing is determined by
 *        {@link #getChildDrawingOrder(int, int)}, false otherwise
 *
 * @see #isChildrenDrawingOrderEnabled()
 * @see #getChildDrawingOrder(int, int)
 */
protected void setChildrenDrawingOrderEnabled(boolean enabled) {
    setBooleanFlag(FLAG_USE_CHILD_DRAWING_ORDER, enabled);
}
```

注释2处，在循环绘制的过程中，获取要绘制的child的下标。

```java
private int getAndVerifyPreorderedIndex(int childrenCount, int i, boolean customOrder) {
    final int childIndex;
    if(customOrder) {
        //注释1处，这里返回了真正要绘制的View 的下标。
        final int childIndex1 = getChildDrawingOrder(childrenCount, i);
        if(childIndex1 >= childrenCount) {
            throw new IndexOutOfBoundsException("getChildDrawingOrder() " + "returned invalid index " + childIndex1 + " (child count is " + childrenCount + ")");
        }
        childIndex = childIndex1;
    } else {
        childIndex = i;
    }
    return childIndex;
}
```

getAndVerifyPreorderedIndex方法注释1处，通过调用 getChildDrawingOrder 获取真正要绘制的View 的下标。我们正是通过重写了这方法实现了交换 mFirstView 和 mSecondView 的 绘制顺序。


```kotlin
/**
 * 返回要绘制的 View 的下标。如果想改变自 View 的绘制顺序，可以重写这个方法。 
 * <p>
 * 注意:这个方法必须配合 {@link #setChildrenDrawingOrderEnabled(boolean)} 方法使用才有效果。 需要调用{@link #setChildrenDrawingOrderEnabled(boolean)} 方法并传递 true。
 *
 * @param i The current iteration. 注意我感觉这里叫 iteration 不合适，应该叫 index 。是当前要绘制的View 的下标。
 * @return The index of the child to draw 返回真正要绘制的 View 的 下标。
 *
 * @see #setChildrenDrawingOrderEnabled(boolean)
 * @see #isChildrenDrawingOrderEnabled()
 */
override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
    //注释3处，修改绘制顺序，当要绘制 mFirstView的时候，绘制mSecondView。要绘制 mSecondView 的时候，绘制mFirstView。
    if (i == mFirstViewIndex) {
        return mSecondViewIndex
    } else if (i == mSecondViewIndex) {
        return mFirstViewIndex
    }
    return super.getChildDrawingOrder(childCount, i)
}
```

dispatchDraw 方法注释3处，根须下标获取要绘制的View。
注释4处，绘制View。

总结一下，要改变View的绘制顺序步骤如下：

1. 自定义 ViewGroup 调用 `setChildrenDrawingOrderEnabled(boolean)` 方法并传递 true。
2. 重写 getChildDrawingOrder(int childCount, int i) 方法，根据自己的逻辑返回真正要绘制的View 的下标。



