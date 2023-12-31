1.点击事件发生后，事件先传到Activity、再传到ViewGroup、最终再传到 View。要想充分理解Android分发机制，本质上是要理解： 

* Activity对点击事件的分发机制
* ViewGroup对点击事件的分发机制
* View对点击事件的分发机制

### Activity的事件分发机制
当一个点击事件发生时，事件最先传到 ```Activity``` 的 ```dispatchTouchEvent()``` 进行事件分发

Activity的dispatchTouchEvent方法

```java
public boolean dispatchTouchEvent(MotionEvent ev) {
    //注释1 ，先调用Window的superDispatchTouchEvent方法
    if (getWindow().superDispatchTouchEvent(ev)) {
        return true;
    }
    //注释2，如果Window没有处理事件，则调用onTouchEvent
    return onTouchEvent(ev);
}

```
Activity首先将事件交给Window进行分发，如果返回true，那么这次事件分发结束。返回false，说明没有View对事件进行处理，那么就调用Activity的onTouchEvent方法。

注释1处，获取当前Activity的window对象，Window类是抽象类，其唯一实现类是PhoneWindow类；

PhoneWindow类的superDispatchTouchEvent()方法
```java
@Override
public boolean superDispatchTouchEvent(MotionEvent event) {
    //调用DecorView的superDispatchTouchEvent方法
    return mDecor.superDispatchTouchEvent(event);
}

```
DecorView的superDispatchTouchEvent方法

```java
/**
 * 
 * a. DecorView类是PhoneWindow类的一个内部类
 * b. DecorView继承自FrameLayout，是所有界面的父类
 * c. FrameLayout是ViewGroup的子类，故DecorView的间接父类 = ViewGroup
 */
public boolean superDispatchTouchEvent(MotionEvent event) {
   //调用父类的方法FrameLayout的dispatchTouchEvent()
   return super.dispatchTouchEvent(event);
}

```

```java
private final class DecorView extends FrameLayout implements RootViewSurfaceTaker {
    
}

```

FrameLayout并没有覆盖ViewGroup的dispatchTouchEvent()方法，所以这个时候事件已经到了ViewGroup了。

### ViewGroup的事件分发机制

点击事件到达ViewGroup以后，会调用ViewGroup的dispatchTouchEvent方法，然后逻辑是这样的：
1. 如果顶级ViewGroup拦截事件，即onInterceptTouchEvent方法返回true，那么事件就由ViewGroup处理。注意，ViewGroup的`onInterceptTouchEvent`方法默认是返回false的。
2. 如果ViewGroup设置了OnTouchListener，则OnTouchListener的onTouch回调方法会被调用。如果OnTouchListener的onTouch回调方法返回true，事件分发结束。如果OnTouchListener的onTouch回调方法返回false，那么ViewGroup的onTouchEvent方法会被调用。
3. 如果ViewGroup设置了OnClickListener，在ViewGroup的onTouchEvent方法中，OnClickListener的onClick回调会被调用。

4. 如果ViewGroup不拦截事件，则事件会传递给它的子View，这时子View
的dispatchTouchEvent方法会被调用。如果子View是一个View的时候，这个时候事件分发就到View了。

下面我们看一下ViewGroup的dispatchTouchEvent方法的部分代码逻辑

```java
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
       
   //...
    boolean handled = false;
    if (onFilterTouchEventForSecurity(ev)) {
        final int action = ev.getAction();
        final int actionMasked = action & MotionEvent.ACTION_MASK;

        // 处理初始化的down事件
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            // 当触摸手势开始的时候丢弃所有先前的状态 
           // 由于app切换，ANR，或者一些其他的状态改变，系统框架可能已经丢弃了先前触摸手势的up和cancel事件
          //注释1处，取消和清除所有的触摸状态
            cancelAndClearTouchTargets(ev);
          //注释2处，
            resetTouchState();
        }

        // 注释3处，检查是否拦截事件
        final boolean intercepted;
        if (actionMasked == MotionEvent.ACTION_DOWN || mFirstTouchTarget != null) {
            //检查是否不允许拦截事件
            final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
            if (!disallowIntercept) {
                //允许拦截，调用onInterceptTouchEvent方法
                intercepted = onInterceptTouchEvent(ev);
                ev.setAction(action); // restore action in case it was changed
            } else {//不允许拦截
                intercepted = false;
            }
        } else {
            // 如果没有触摸事件处理者，并且不是一个down事件，当前ViewGroup会继续拦截
            // 所以事件会继续由当前viewGroup处理。
            intercepted = true;
        }

        // ...
        // Check for cancelation.
        final boolean canceled = resetCancelNextUpFlag(this)
                    || actionMasked == MotionEvent.ACTION_CANCEL;

        // Update list of touch targets for pointer down, if needed.
        final boolean split = (mGroupFlags & FLAG_SPLIT_MOTION_EVENTS) != 0;
        //声明事件处理者newTouchTarget
        TouchTarget newTouchTarget = null;
        //是否已经将事件交给子View进行处理了
        boolean alreadyDispatchedToNewTouchTarget = false;
        //注释4处，如果事件没有取消，并且当前ViewGroup没有拦截事件
        if (!canceled && !intercepted) {
            // ...
           // 注释5处，这里可以认为只有ACTION_DOWN事件，才会进入这个if判断，
            if (actionMasked == MotionEvent.ACTION_DOWN
                        || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)
                        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                final int actionIndex = ev.getActionIndex(); // always 0 for down
                final int idBitsToAssign = split ? 1 << ev.getPointerId(actionIndex)
                            : TouchTarget.ALL_POINTER_IDS;

                final int childrenCount = mChildrenCount;
                if (newTouchTarget == null && childrenCount != 0) {
                    final float x = ev.getX(actionIndex);
                    final float y = ev.getY(actionIndex);
                    // 找到一个可以接受事件的子View
                    // 从前到后遍历Child
                    final ArrayList<View> preorderedList = buildTouchDispatchChildList();
                    final boolean customOrder = preorderedList == null
                                && isChildrenDrawingOrderEnabled();
                    final View[] children = mChildren;
                    //注释6处
                    for (int i = childrenCount - 1; i >= 0; i--) {
                        final int childIndex = getAndVerifyPreorderedIndex(
                                    childrenCount, i, customOrder);
                        //子View
                        final View child = getAndVerifyPreorderedView(
                                    preorderedList, children, childIndex);
                      //注释7处，如果不能接收事件，就continue。
                       if (!canViewReceivePointerEvents(child)
                                    || !isTransformedTouchPointInView(x, y, child, null)) {
                            ev.setTargetAccessibilityFocus(false);
                            continue;
                        }
                        //...
                        //注释8处，获取事件处理者
                        newTouchTarget = getTouchTarget(child);
                        if (newTouchTarget != null) {
                            //如果newTouchTarget不为null，跳出循环。
                            newTouchTarget.pointerIdBits |= idBitsToAssign;
                            break;
                        }

                        //注释9处，将事件交给子View处理，传入的第三个参数child不为null
                        if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                            //...
                            //注释10处，为newTouchTarget和mFirstTouchTarget赋值
                            newTouchTarget = addTouchTarget(child, idBitsToAssign);
                            //已经将事件交给newTouchTarget处理了
                            alreadyDispatchedToNewTouchTarget = true;
                            //跳出循环
                            break;
                        }
                    }
                }
                //...
            }
        }

        // 注释11处，mFirstTouchTarget == null表示没有子View处理事件
        if (mFirstTouchTarget == null) {
            // 没有子View处理事件。有两种情况，一种是当前ViewGroup拦截了事件，另一种情况是子View都没有处理事件。
           //那么就交给当前ViewGroup处理事件，注意我们调用下面的方法，传入的第三参数是null。
            handled = dispatchTransformedTouchEvent(ev, canceled, null,
                        TouchTarget.ALL_POINTER_IDS);
        } 
    }
    //... 
    return handled;
}


```

注释2处，调用了resetTouchState方法
```java
private void resetTouchState() {
    clearTouchTargets();
    resetCancelNextUpFlag(this);
    //这里重置了是否允许ViewGroup拦截事件的标志FLAG_DISALLOW_INTERCEPT
    mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT;
    mNestedScrollAxes = SCROLL_AXIS_NONE;
}
```
在每次down事件到来的时候，都会调用该方法，重置所有的状态准备新一轮的触摸事件分发。ViewGroup的是否允许拦截的标志也会被重新置为允许拦截。也就是说如果是down事件，ViewGroup总是会调用onInterceptTouchEvent方法。但是ViewGroup的`onInterceptTouchEvent`方法默认是返回false的。

注释3处，ViewGroup会在`actionMasked == MotionEvent.ACTION_DOWN || mFirstTouchTarget != null`的情况下判断是否要拦截事件。`mFirstTouchTarget != null`是什么意思呢？当事件由ViewGroup的子元素成功处理时，mFirstTouchTarget会被赋值并指向该子元素。

我们看一下这段代码

```java
 if (actionMasked == MotionEvent.ACTION_DOWN || mFirstTouchTarget != null) {
    //检查是否不允许拦截事件
    final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
    if (!disallowIntercept) {//允许拦截
        intercepted = onInterceptTouchEvent(ev);
        ev.setAction(action); // restore action in case it was changed
      } else {//不允许拦截
            intercepted = false;
      }

```
如果子View通过调用ViewGroup的requestDisallowInterceptTouchEvent方法，将是否允许ViewGroup拦截事件的标记置为FLAG_DISALLOW_INTERCEPT，那么说明此时子View想要处理事件，ViewGroup将不会拦截事件(down事件也不会拦截)，而是将事件交给子View处理。

```java
@Override
public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    if (disallowIntercept == ((mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0)) {
        // We're already in this state, assume our ancestors are too
        return;
    }

    if (disallowIntercept) {
        mGroupFlags |= FLAG_DISALLOW_INTERCEPT;
    } else {
        mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT;
    }

    // Pass it up to our parent
    if (mParent != null) {
        mParent.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}

```

如果 `mFirstTouchTarget != null` 条件满足，move，up，事件到来，如果没有将ViewGroup拦截事件的标记置为FLAG_DISALLOW_INTERCEPT，那么还是允许ViewGroup拦截事件的，即还会走到onInterceptTouchEvent方法里。我们看看这个方法做了什么。

```java
public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (ev.isFromSource(InputDevice.SOURCE_MOUSE)
            && ev.getAction() == MotionEvent.ACTION_DOWN
            && ev.isButtonPressed(MotionEvent.BUTTON_PRIMARY)
            && isOnScrollbarThumb(ev.getX(), ev.getY())) {
        return true;
    }
    return false;
}
```
这里我们注意：
* onInterceptTouchEvent方法并没有处理move和up事件的逻辑，这里也说明了一点：ViewGroup默认是不拦截move和up事件的。
* 默认情况下，即使是down事件，这个方法返回的也是false。
* 如果ViewGroup想要拦截事件的话需要重写这方法，比如`RecyclerView`。

注释6 处开始遍历子View，查找是否有子View可以接收事件。
注释7处，如果子View不能接收事件，就continue。
注释8处，获取处理者，如果已经存在处理者，就跳出循环。
注释9处，调用dispatchTransformedTouchEvent方法，将事件交给子View处理，传入的第三个参数child不为null。

```java
private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,
            View child, int desiredPointerIdBits) {
    //...
    if (child == null) {
            //如果child为null，就由当前ViewGrouop处理事件，
           //直接调用父类View的dispatchTouchEvent方法
            handled = super.dispatchTouchEvent(transformedEvent);
    } else {
        final float offsetX = mScrollX - child.mLeft;
        final float offsetY = mScrollY - child.mTop;
        transformedEvent.offsetLocation(offsetX, offsetY);
        if (! child.hasIdentityMatrix()) {
            transformedEvent.transform(child.getInverseMatrix());
        }
        //child不为null，由child处理事件
        handled = child.dispatchTouchEvent(transformedEvent);
    }
    return handled;
}

```

注释10处，在addTouchTarget方法里，为newTouchTarget和mFirstTouchTarget赋值，并将alreadyDispatchedToNewTouchTarget置为true;然后跳出循环。

```java
private TouchTarget addTouchTarget(View child, int pointerIdBits) {
    final TouchTarget target = TouchTarget.obtain(child, pointerIdBits);
    target.next = mFirstTouchTarget;
    mFirstTouchTarget = target;
    return target;
}

```

注释11处，mFirstTouchTarget == null表示没有子View处理事件，那么由当前ViewGroup处理事件，在上面的dispatchTransformedTouchEvent方法中已经看过了。

当由子View 处理事件的时候，如果子View是一个View，那么就事件分发就到了View的dispatchTouchEvent方法中。

### View的事件分发机制
```java
public boolean dispatchTouchEvent(MotionEvent event) {
        
        boolean result = false;
        //...

        if (onFilterTouchEventForSecurity(event)) {
            if ((mViewFlags & ENABLED_MASK) == ENABLED && handleScrollBarDragging(event)) {
                result = true;
            }
            //noinspection SimplifiableIfStatement
            ListenerInfo li = mListenerInfo;
            //注释1处，调用mOnTouchListener的onTouch
            if (li != null && li.mOnTouchListener != null
                    && (mViewFlags & ENABLED_MASK) == ENABLED
                    && li.mOnTouchListener.onTouch(this, event)) {
                result = true;
            }
            //注释2处
            if (!result && onTouchEvent(event)) {
                result = true;
            }
        }
        //...
        return result;
    }

```
注释1处，如果View设置了OnTouchListener，则OnTouchListener的onTouch回调方法会被调用。如果OnTouchListener的onTouch回调方法返回true，事件分发结束。如果OnTouchListener的onTouch回调方法返回false，那么View的onTouchEvent方法会被调用。

注释2处，调用onTouchEvent方法。

```java
@Override
public boolean onTouchEvent(MotionEvent event) {
    String action = "";
    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            Log.i(TAG, "onTouchEvent ACTION_DOWN");
            action = "ACTION_DOWN";
            break;
        case MotionEvent.ACTION_MOVE:
            Log.i(TAG, "onTouchEvent ACTION_MOVE");
            action = "ACTION_MOVE";
            break;
        case MotionEvent.ACTION_UP:
            Log.i(TAG, "onTouchEvent ACTION_UP");
            action = "ACTION_UP";
            break;
    }
    //注释1处
    boolean handled = super.onTouchEvent(event);
    Log.i(TAG, "onTouchEvent: action = " + action + " handled = " + handled);
    return handled;
}
```

注释1处，`super.onTouchEvent(event)`默认是返回false。当`ACTION_DOWN`的时候，如果我们返回了false，后续是不会收到`ACTION_MOVE`和`ACTION_UP`事件的。



如果View设置了OnClickListener，在View的onTouchEvent方法中，OnClickListener的onClick回调会被调用。