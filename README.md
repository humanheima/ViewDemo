# ViewDemo
1. 自定义MaxHeightLayout，限制popWindow数据过多的时候最大高度。
2. 了解BottomSheet,感觉要是在实际中使用还是有问题的。
3. 了解ConstraintLayout
4. 研究了一下自定义View的三个构造函数

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
## LayoutInflater源码分析

LayoutInflater用来把一个xml文件实例化成对应的View对象。
可以使用{@link android.app.Activity#getLayoutInflater()} 或者 {@link Context#getSystemService}来获取一个标准的LayoutInflater实例。

Activity的getLayoutInflater方法
```
public LayoutInflater getLayoutInflater() {
    return getWindow().getLayoutInflater();
}
```
PhoneWindow的getLayoutInflater方法
```
public LayoutInflater getLayoutInflater() {
    return mLayoutInflater;
}

```
LayoutInflater的from方法内部就是使用context.getSystemService来获取LayoutInflater实例的。
```
public static LayoutInflater from(Context context) {
        LayoutInflater LayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (LayoutInflater == null) {
            throw new AssertionError("LayoutInflater not found.");
        }
        return LayoutInflater;
    }
```
获取到了LayoutInflater实例以后，就可以调用它的inflate方法来加载布局文件了。
```
public View inflate(@LayoutRes int resource, @Nullable ViewGroup root) {
        return inflate(resource, root, root != null);
    }
```
举个例子
```
<?xml version="1.0" encoding="utf-8"?>
<Button xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:text="Button"
    android:textAllCaps="false"
    android:layout_height="match_parent">

</Button>
```

```
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_layout_inflate)
 	 //生成Button对象
    val button = layoutInflater.inflate(R.layout.button_layout, null)
    clRoot.addView(button)

}
```
接下来看看inflate方法

```
/**
     * 从指定的xml资源文件中填充一个view层级。有错误抛InflateException。     *
     * @param resource 资源文件ID 
     * @param root 可选的view，如果attachToRoot是true，使用root作为生成的view层级的父布局；如果attachToRoot是false，为返回的view层级的根
     * view提供一系列的LayoutParams值。
     * @param attachToRoot 用来标志填充的view层级是否应该被添加到root中去。如果是false, root只是用来为为返回的布局层级的根
     * view创建正确的LayoutParams。
     * @return 返回填充的view层级的根view。如果root不为null并且attachToRoot 是true，就返回root；否则返回view层级的根view。    
     * */
    public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {
        final Resources res = getContext().getResources();
        //...
			//根据资源id生成xml解析器
        final XmlResourceParser parser = res.getLayout(resource);
        try {
        	  //注释1处
            return inflate(parser, root, attachToRoot);
        } finally {
            parser.close();
        }
    }
```
在注释1处调用了重载函数
```
public View inflate(XmlPullParser parser, @Nullable ViewGroup root, boolean attachToRoot) {
    synchronized (mConstructorArgs) {
            
        final Context inflaterContext = mContext;
        final AttributeSet attrs = Xml.asAttributeSet(parser);
            Context lastContext = (Context) mConstructorArgs[0];
            mConstructorArgs[0] = inflaterContext;
            //将root赋值给result
            View result = root;

            try {
                // Look for the root node.
                int type;
                while ((type = parser.next()) != XmlPullParser.START_TAG &&
                        type != XmlPullParser.END_DOCUMENT) {
                    // Empty
                }

                if (type != XmlPullParser.START_TAG) {
                    throw new InflateException(parser.getPositionDescription()
                            + ": No start tag found!");
                }

                final String name = parser.getName();
						
					   //处理merge标签
                if (TAG_MERGE.equals(name)) {
                    if (root == null || !attachToRoot) {
                        throw new InflateException("<merge /> can be used only with a valid "
                                + "ViewGroup root and attachToRoot=true");
                    }

                    rInflate(parser, root, inflaterContext, attrs, false);
                } else {
                    //注释1处
                    //temp是xml文件中的根view
                    final View temp = createViewFromTag(root, name, inflaterContext, attrs);

                    ViewGroup.LayoutParams params = null;

                    if (root != null) {
                        //注释2处
                        //创建与root匹配的布局参数
                        params = root.generateLayoutParams(attrs);
                        if (!attachToRoot) {
                            //注释3处
                            // 将布局参数设置给temp
                            temp.setLayoutParams(params);
                        }
                    }

                    // 注释4处，填充temp下面的所有子view
                    rInflateChildren(parser, temp, attrs, true);

                    // 注释5处，如果条件满足，应该把temp添加到root中去。
                    if (root != null && attachToRoot) {
                        root.addView(temp, params);
                    }

                    // 注释6处，判断是返回root还是返回result。
                    if (root == null || !attachToRoot) {
                        result = temp;
                    }
                }

            } 
            //...
            return result;
        }
    }

```
在注释1处，调用createViewFromTag方法获取xml文件中的根view。
```
private View createViewFromTag(View parent, String name, Context context, AttributeSet attrs) {
    return createViewFromTag(parent, name, context, attrs, false);
}
```
方法内部使用反射的方式创建对应的view并返回，这里就细去看了。

在注释2处，创建和root匹配的布局参数。
在注释3处，如果attachToRoot为fasle，则将布局参数设置给temp。

在注释4处，调用rInflateChildren方法递归填充temp下面的所有子view。
在注释5处，如果root != null 并且 attachToRoot，就把temp添加到root 中。
在注释6处，root == null 或者!attachToRoot，就把temp赋值给result。

最后方法返回result。

```
final void rInflateChildren(XmlPullParser parser, View parent, AttributeSet attrs,
            boolean finishInflate) throws XmlPullParserException, IOException {
        rInflate(parser, parent, parent.getContext(), attrs, finishInflate);
    }

```
看一下rInflate方法

```
 void rInflate(XmlPullParser parser, View parent, Context context,
            AttributeSet attrs, boolean finishInflate) throws XmlPullParserException, IOException {

        final int depth = parser.getDepth();
        int type;
        boolean pendingRequestFocus = false;

        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String name = parser.getName();

            if (TAG_REQUEST_FOCUS.equals(name)) {
                pendingRequestFocus = true;
                consumeChildElements(parser);
            } else if (TAG_TAG.equals(name)) {
                parseViewTag(parser, parent, attrs);
            } else if (TAG_INCLUDE.equals(name)) {//处理include标签
                if (parser.getDepth() == 0) {
                    throw new InflateException("<include /> cannot be the root element");
                }
                parseInclude(parser, context, parent, attrs);
            } else if (TAG_MERGE.equals(name)) {
                throw new InflateException("<merge /> must be the root element");
            } else {
                //注释1处
                final View view = createViewFromTag(parent, name, context, attrs);
                final ViewGroup viewGroup = (ViewGroup) parent;
                final ViewGroup.LayoutParams params = viewGroup.generateLayoutParams(attrs);
                //注释2处
                rInflateChildren(parser, view, attrs, true);
                viewGroup.addView(view, params);
            }
        }

        if (pendingRequestFocus) {
            parent.restoreDefaultFocus();
        }

        if (finishInflate) {
            parent.onFinishInflate();
        }
    }

```
在注释1处同样调用createViewFromTag创建view对象。
在注释2处调用rInflateChildren方法递归填充所有view。