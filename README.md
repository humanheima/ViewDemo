# ViewDemo
1. 自定义MaxHeightLayout，限制popWindow数据过多的时候最大高度。
2. 了解BottomSheet。
3. 了解ConstraintLayout。
4. 研究了一下自定义View的三个构造函数。


![Activity的事件分发机制](activity_touch_event_dispatch.png)

## ViewGroup事件的分发机制

从上面Activity事件分发机制可知，ViewGroup事件分发机制从dispatchTouchEvent()开始
![ViewGroup事件的分发机制](ViewGroup_dispatchTouchEvent.png)

## View事件的分发机制

从上面ViewGroup事件分发机制知道，View事件分发机制从dispatchTouchEvent()开始
![View事件的分发机制](View_dispatch_event.png)

如果一个View的可见性不是visible，并且view也没有与之关联的动画，那么他是收不到事件的
下面的代码摘自ViewGroup
```
 /**
     * Returns true if a child view can receive pointer events.
     * @hide
     */
    private static boolean canViewReceivePointerEvents(@NonNull View child) {
        return (child.mViewFlags & VISIBILITY_MASK) == VISIBLE
                || child.getAnimation() != null;
    }
```
## View.post() 到底干了啥
[【Andorid源码解析】View.post() 到底干了啥](https://www.jianshu.com/p/85fc4decc947)

遍历View树
```
fun printAllChildView(view: View) {
        Log.d(TAG, "printAllChildView: ")
        val queue: Deque<View> = LinkedList<View>()
        var root = view
        queue.add(root)
        while (queue.isNotEmpty()) {
            root = queue.pop()
            Log.d(TAG, "printAllChildView: $root")
            if (root is ViewGroup) {
                for (i in 0 until root.childCount) {
                    queue.add(root.getChildAt(i))
                }

            }
        }
    }
```

#### custom_view 包下面 是《Android自定义控件开发入门与实战》的部分示例