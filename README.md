# ViewDemo
1. 自定义MaxHeightLayout，限制popWindow数据过多的时候最大高度。
2. 了解BottomSheet。
3. 了解ConstraintLayout。
4. 研究了一下自定义View的三个构造函数。

## 事件分发机制

1. 点击事件发生后，事件先传到Activity、再传到ViewGroup、最终再传到 View。要想充分理解Android分发机
制，本质上是要理解： 
* Activity对点击事件的分发机制
* ViewGroup对点击事件的分发机制
* View对点击事件的分发机制

**Activity的事件分发机制**
当一个点击事件发生时，事件最先传到 ```Activity``` 的 ```dispatchTouchEvent()``` 进行事件分发
```java
public boolean dispatchTouchEvent(MotionEvent ev) {
        // 一般事件列开始都是DOWN事件 = 按下事件，故此处基本是true
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        //分析4
        return onTouchEvent(ev);
    }
```
这个回调方法旨在帮助活动智能的管理状态栏通知
```java
 public void onUserInteraction() {
    }
```

```java
getWindow().superDispatchTouchEvent(ev);
```
获取当前Activity的window对象，Window类是抽象类，其唯一实现类 = PhoneWindow类；即此处的Window类对象 = PhoneWindow类对象

PhoneWindow类的superDispatchTouchEvent()方法
```java

    @Override
    public boolean superDispatchTouchEvent(MotionEvent event) {
        return mDecor.superDispatchTouchEvent(event);
    }
```

```java
/**
  * 分析3：mDecor.superDispatchTouchEvent(event)
  * 定义：属于顶层View（DecorView）
  * 说明：
  *     a. DecorView类是PhoneWindow类的一个内部类
  *     b. DecorView继承自FrameLayout，是所有界面的父类
  *     c. FrameLayout是ViewGroup的子类，故DecorView的间接父类 = ViewGroup
  */
    public boolean superDispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
        // 调用父类的方法 = ViewGroup的dispatchTouchEvent()
        // 即 将事件传递到ViewGroup去处理，详细请看ViewGroup的事件分发机制

    }
```
```java
private final class DecorView extends FrameLayout implements RootViewSurfaceTaker {
    
}
```
分析4，Activity.onTouchEvent().当没有任何View处理点击事件的时候调用，例如当事件发生在Window边界外，没有View接收事件。
 即只有在点击事件在Window边界外才会返回true，一般情况都返回false。
```java
public boolean onTouchEvent(MotionEvent event) {
        if (mWindow.shouldCloseOnTouch(this, event)) {
            finish();
            return true;
        }

        return false;
    }
```
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


### dp sp 

dp: 就是dip Density-independent Pixels 
密度无关像素 - 基于屏幕物理密度的抽象单位。这个单位是相对于160dpi（dot per inch）的屏幕来计算的，在160dpi的屏幕上，1dp大约等于1px。
在较高密度屏幕上运行时，用于绘制1dp的像素数按照适合屏幕dpi的系数放大。同样，当在较低密度屏幕上时，用于1dp的像素数按比例缩小。
dp与像素的比率将随着屏幕密度而变化，但不一定是成正比的。使用dp单位（而不是px单位）是一种简单的解决方案，可以使布局中的视图尺寸适当调整以适应不同的屏幕密度。
换句话说，它为不同设备的UI元素的实际大小提供了一致性。

sp Scale-independent Pixels 
比例无关的像素 - 这与dp单位类似，但它也可以通过用户的字体大小首选项进行缩放。建议您在指定字体大小时使用此单位，以便根据屏幕密度和用户偏好调整它们。

pt Points
点数 - 是一个标准长度单位，1pt = 1/72英寸，用于印刷业。

px Pixels 

像素 - 对应于屏幕上的实际像素。建议不要使用此计量单位，因为实际表示可能因设备而异; 每个设备可以具有每英寸不同数量的像素，并且可以在屏幕上具有更多或更少的总像素。

参考链接

1. [Android Developers>Dos>指南More resource types](https://developer.android.google.cn/guide/topics/resources/more-resources)
2. [Android像素单位dp、sp、px、pt的区别和比较](https://blog.csdn.net/wzy_1988/article/details/43732467)
3. [Android中分辨率，DPI，DP与PX对应关系](https://blog.csdn.net/u012741741/article/details/51075518)

|   | ||||||
|---|---|---|---|---|---|---|
|  dpi等级 |ldpi|mdpi|hdpi|xhdpi|xxhdpi|xxxhdpi|
|  dpi数值 |120|160|240|320|480|640|
|  1dp=?px | 0.75 | 1 | 1.5 | 2 | 3 | 4 |

上面表格的信息来自`android.utilDisplayMetrics`

目前一些设备的dpi数值以及对应的dpi等级如下

| 设备型号| dpi数值|dpi等级|
|---| ---|---|
|||

