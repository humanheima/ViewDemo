LayoutInflater用来把一个xml文件实例化成对应的View对象。

可以使用`Activity#getLayoutInflater()}`或者`Context#getSystemService`来获取一个标准的LayoutInflater实例。

先从Activity的onCreate方法开始：

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //注释1处
    setContentView(R.layout.activity_layout_inflate)
}
```

AppCompatActivity 的 setContentView方法

```java
@Override
public void setContentView(@LayoutRes int layoutResID) {
    //注释1处，调用代理对象的setContentView方法
    getDelegate().setContentView(layoutResID);
}
```

```java
@Override
public void setContentView(int resId) {
    ensureSubDecor();
    ViewGroup contentParent = (ViewGroup) mSubDecor.findViewById(android.R.id.content);
    contentParent.removeAllViews();
    //注释1处
    LayoutInflater.from(mContext).inflate(resId, contentParent);
    mOriginalWindowCallback.onContentChanged();
}
```

注释1处，进入正题

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

PhoneWindow的构造函数

```
public PhoneWindow(Context context) {
    super(context);
    //还是通过LayoutInflater.from方法获取的。
    mLayoutInflater = LayoutInflater.from(context);
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

这是Activity的布局文件。

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/clRoot"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".activity.LayoutInflateActivity">

    <android.support.constraint.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <WebView
            android:id="@+id/webview"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/hello_world"
            android:textAllCaps="false"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/webview" />

    </android.support.constraint.ConstraintLayout>

</RelativeLayout>
```

这个例子的大致流程图如下
![LayoutInflater创建View (1).jpg](https://upload-images.jianshu.io/upload_images/3611193-a2a96f9f65dc3572.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

接下来看看LayoutInflater的inflate方法究竟是怎么把布局文件转换成view对象的。

```
public View inflate(@LayoutRes int resource, @Nullable ViewGroup root) {
    //调用3个参数的重载函数
    return inflate(resource, root, root != null);
}
```


```java
/**
 * 根据指定的xml布局文件中生成一个view层级。有错误抛出InflateException。
 * @param resource xml布局文件ID 
 * @param root 可选的view，如果attachToRoot是true，使用root作为生成的view层级的父布局；
 *             如果attachToRoot是false，为返回的view层级的根view提供一系列的LayoutParams值。
 * @param attachToRoot 用来标志生成的view层级是否应该被添加到root中去。如果是false, 
 *         root只是用来为返回的view层级的根view创建正确的LayoutParams。
 * @return 返回生成的view层级的根view。如果root不为null并且attachToRoot 是true，就返回root；
 *          否则返回view层级的根view。    
 */
public View inflate(int resource, ViewGroup root, boolean attachToRoot){
    final Resources res = getContext().getResources();
    //...
    //注释1处，根据资源id生成xml解析器
    final XmlResourceParser parser = res.getLayout(resource);
    try {
        //注释2处
        return inflate(parser, root, attachToRoot);
    } finally {
        parser.close();
    }
}
```

注释1处,调用Resources的getLayout方法

```
/**
 * 返回一个XmlResourceParser对象，通过这个对象，可以从指定的资源id中读取view 的布局描述。
 * 
 * @param id  由aapt工具生成的所需资源标识符。此整数对包，类型和资源条目进行编码。
 *            0不是一个合法的资源标识符。
 * @return 一个新的解析器，你可以通过这解析器读取XML数据。
 *         
 * @see #getXml
 */
@NonNull
public XmlResourceParser getLayout(@LayoutRes int id) throws NotFoundException {
    return loadXmlResourceParser(id, "layout");
}
```

这个方法返回了一个XmlResourceParser对象，我们可以通过这个对象读取布局文件中的XML数据。

注释2处，调用LayoutInflater的inflate(XmlPullParser parser,ViewGroup root, boolean attachToRoot)方法开始读取解析数据。

```
public View inflate(XmlPullParser parser,ViewGroup root, boolean attachToRoot) {
    synchronized (mConstructorArgs) {     
        //...
        final Context inflaterContext = mContext;
        final AttributeSet attrs = Xml.asAttributeSet(parser);
        Context lastContext = (Context) mConstructorArgs[0];
        mConstructorArgs[0] = inflaterContext;
        View result = root;
        try {
            // Look for the root node.
            int type;
            while ((type = parser.next()) != XmlPullParser.START_TAG &&
                        type != XmlPullParser.END_DOCUMENT) {
                // Empty
            }
            //...
            //注释0处，布局文件中第一个标签名字
            final String name = parser.getName();
						
	         //处理merge标签，标签的情况暂时不去看
            if (TAG_MERGE.equals(name)) {
                if (root == null || !attachToRoot) {
                    throw new InflateException("<merge /> can be used only with a "
                            + "valid ViewGroup root and attachToRoot=true");
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
                        //将布局参数设置给temp
                        temp.setLayoutParams(params);
                    }
                }

                //注释4处，填充temp下面的所有子view
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

注释0处，布局文件中第一个标签名字，在这个例子中就是布局中的`RelativeLayout`。

```java
final String name = parser.getName();
```

在注释1处，调用createViewFromTag方法获取xml文件中的根view。

```
private View createViewFromTag(View parent, String name, Context context, 
            AttributeSet attrs) {
    return createViewFromTag(parent, name, context, attrs, false);
}

```


```
/**
     * 从一个标签名和提供的属性集合创建一个view。
     * 
     * 这个方法的可见性是default，所以BridgeInflater可以覆盖这个方法。
     *
     * @param parent 父view，用来生成layout params
     * @param name XML标签名，用来定义一个view。
     * @param context 用来生成view的上下文。
     * @param attrs 布局文件中view的属性集
     * @param ignoreThemeAttr 用来标志要生成的view是否忽略 {@code android:theme}中定义的属性。
     */
    View createViewFromTag(View parent, String name, Context context, AttributeSet attrs,
            boolean ignoreThemeAttr) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        // 如果允许并指定了一个主题属性集，则使用。
        if (!ignoreThemeAttr) {
            final TypedArray ta = context.obtainStyledAttributes(attrs, ATTRS_THEME);
            final int themeResId = ta.getResourceId(0, 0);
            if (themeResId != 0) {
                context = new ContextThemeWrapper(context, themeResId);
            }
            ta.recycle();
        }
        //...
        try {
            View view;
            if (mFactory2 != null) {//注释1处
                view = mFactory2.onCreateView(parent, name, context, attrs);
            } else if (mFactory != null) {//注释2处
                view = mFactory.onCreateView(name, context, attrs);
            } else {
                view = null;
            }

            if (view == null && mPrivateFactory != null) {
                //注释3处
                view = mPrivateFactory.onCreateView(parent, name, context, attrs);
            }

            if (view == null) {//注释4处
                final Object lastContext = mConstructorArgs[0];
                mConstructorArgs[0] = context;
                try {
                    if (-1 == name.indexOf('.')) {
                        //注释5处
                        view = onCreateView(parent, name, attrs);
                    } else {
                        view = createView(name, null, attrs);
                    }
                } finally {
                    mConstructorArgs[0] = lastContext;
                }
            }

            return view;
        } catch (InflateException e) {
            throw e;

        } catch (ClassNotFoundException e) {
            final InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + name, e);
            ie.setStackTrace(EMPTY_STACK_TRACE);
            throw ie;

        } catch (Exception e) {
            final InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class " + name, e);
            ie.setStackTrace(EMPTY_STACK_TRACE);
            throw ie;
        }
    }
```

![mFactory2.png](https://upload-images.jianshu.io/upload_images/3611193-53534416242f4a3f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

经过调试发现，代码走到了注释2处，调用了`mFactory2`的`onCreateView`方法。并且发现`mFactory2`是一个AppCompatDelegateImpl对象。`mFactory2`是何时赋值的我们暂且不管。

AppCompatDelegateImpl的onCreateView方法。

```java
public final View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
    return this.createView(parent, name, context, attrs);
}
```

AppCompatDelegateImpl的createView方法。

```java
public View createView(View parent, String name, @NonNull Context context, AttributeSet attrs) {
    //内部调用了AppCompatViewInflater的createView方法
    return this.mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext, IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
}
```


AppCompatViewInflater的createView方法，我们从中可以看到一些端倪。

```java
final View createView(View parent, final String name, @NonNull Context context,
            @NonNull AttributeSet attrs, boolean inheritContext,
            boolean readAndroidTheme, boolean readAppTheme, boolean wrapContext) {
        final Context originalContext = context;

        // We can emulate Lollipop's android:theme attribute propagating down the view hierarchy
        // by using the parent's context
        if (inheritContext && parent != null) {
            context = parent.getContext();
        }
        if (readAndroidTheme || readAppTheme) {
            // We then apply the theme on the context, if specified
            context = themifyContext(context, attrs, readAndroidTheme, readAppTheme);
        }
        if (wrapContext) {
            context = TintContextWrapper.wrap(context);
        }

        View view = null;

        // We need to 'inject' our tint aware Views in place of the standard framework versions
        switch (name) {
            case "TextView":
                view = createTextView(context, attrs);
                verifyNotNull(view, name);
                break;
            case "ImageView":
                view = createImageView(context, attrs);
                verifyNotNull(view, name);
                break;
            case "Button":
                view = createButton(context, attrs);
                verifyNotNull(view, name);
                break;
            case "EditText":
                view = createEditText(context, attrs);
                verifyNotNull(view, name);
                break;
            case "Spinner":
                view = createSpinner(context, attrs);
                verifyNotNull(view, name);
                break;
            case "ImageButton":
                view = createImageButton(context, attrs);
                verifyNotNull(view, name);
                break;
            case "CheckBox":
                view = createCheckBox(context, attrs);
                verifyNotNull(view, name);
                break;
            case "RadioButton":
                view = createRadioButton(context, attrs);
                verifyNotNull(view, name);
                break;
            case "CheckedTextView":
                view = createCheckedTextView(context, attrs);
                verifyNotNull(view, name);
                break;
            case "AutoCompleteTextView":
                view = createAutoCompleteTextView(context, attrs);
                verifyNotNull(view, name);
                break;
            case "MultiAutoCompleteTextView":
                view = createMultiAutoCompleteTextView(context, attrs);
                verifyNotNull(view, name);
                break;
            case "RatingBar":
                view = createRatingBar(context, attrs);
                verifyNotNull(view, name);
                break;
            case "SeekBar":
                view = createSeekBar(context, attrs);
                verifyNotNull(view, name);
                break;
            default:
                // The fallback that allows extending class to take over view inflation
                // for other tags. Note that we don't check that the result is not-null.
                // That allows the custom inflater path to fall back on the default one
                // later in this method.
                view = createView(context, name, attrs);
        }

        if (view == null && originalContext != context) {
            // If the original context does not equal our themed context, then we need to manually
            // inflate it using the name so that android:theme takes effect.
            view = createViewFromTag(context, name, attrs);
        }

        if (view != null) {
            //我们在布局中有时候会设置`android:onClick="click"` 这样一个属性，难道是在这里处理的？
            checkOnClickListener(view, attrs);
        }

        return view;
    }
```

最后发现返回的view为null。

我们回到`createViewFromTag`方法的注释2处、注释3处、返回的都是null。

注释5处，调用LayoutInflater的onCreateView方法。

```java
protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
    return onCreateView(name, attrs);
}
```

注意，这里调用的是`PhoneLayoutInflater`类里面的`onCreateView(String name, AttributeSet attrs)`方法。


```java
@Override 
protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        for (String prefix : sClassPrefixList) {
            try {
                //注释1处
                View view = createView(name, prefix, attrs);
                if (view != null) {
                    return view;
                }
            } catch (ClassNotFoundException e) {
                // In this case we want to let the base class take a crack
                // at it.
            }
        }

        return super.onCreateView(name, attrs);
}
```

```java
private static final String[] sClassPrefixList = {
        "android.widget.",
        "android.webkit.",
        "android.app."
};
```

注释1处，调用`LayoutInflater`的`createView(String name, String prefix, AttributeSet attrs)`方法。传入的name是`RelativeLayout`，
prefix是`android.widget.`。

```java
public final View createView(String name, String prefix, AttributeSet attrs)
            throws ClassNotFoundException, InflateException {
        //根据要创建的View的名称获取构造函数
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor != null && !verifyClassLoader(constructor)) {
            constructor = null;
            sConstructorMap.remove(name);
        }
        Class<? extends View> clazz = null;

        try {
            if (constructor == null) {
                //如果构造函数为null，就通过类加载器加载类对象并获取构造函数，然后将构造函数缓存在sConstructorMap中
                // Class not found in the cache, see if it's real, and try to add it
                clazz = mContext.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                if (mFilter != null && clazz != null) {
                    boolean allowed = mFilter.onLoadClass(clazz);
                    if (!allowed) {
                        failNotAllowed(name, prefix, attrs);
                    }
                }
                //获取构造函数
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                //加入缓存
                sConstructorMap.put(name, constructor);
            } else {
                // If we have a filter, apply it to cached constructor
                if (mFilter != null) {
                    // Have we seen this name before?
                    Boolean allowedState = mFilterMap.get(name);
                    if (allowedState == null) {
                        // New class -- remember whether it is allowed
                        clazz = mContext.getClassLoader().loadClass(
                                prefix != null ? (prefix + name) : name).asSubclass(View.class);

                        boolean allowed = clazz != null && mFilter.onLoadClass(clazz);
                        mFilterMap.put(name, allowed);
                        if (!allowed) {
                            failNotAllowed(name, prefix, attrs);
                        }
                    } else if (allowedState.equals(Boolean.FALSE)) {
                        failNotAllowed(name, prefix, attrs);
                    }
                }
            }

            Object lastContext = mConstructorArgs[0];
            if (mConstructorArgs[0] == null) {
                // Fill in the context if not already within inflation.
                mConstructorArgs[0] = mContext;
            }
            Object[] args = mConstructorArgs;
            args[1] = attrs;
            //创建View
            final View view = constructor.newInstance(args);
            if (view instanceof ViewStub) {
                // Use the same context when inflating ViewStub later.
                final ViewStub viewStub = (ViewStub) view;
                viewStub.setLayoutInflater(cloneInContext((Context) args[0]));
            }
            mConstructorArgs[0] = lastContext;
            //返回View
            return view;

        } catch (Exception e) {
            //抛出各种异常
        } 
    }
```

方法内部逻辑就是根据控件的名称和前缀prefix加载对应的类对象，如果缓存然后通过反射创建View对象并返回。到这里我们布局文件中最外层的RelativeLayout就创建好了。

LayoutInflater的inflate(XmlPullParser parser,ViewGroup root, boolean attachToRoot)方法的注释1处

```java
final View temp = createViewFromTag(root, name, inflaterContext, attrs);
```
在这个例子中，temp就是我们创建好的RelativeLayout对象。

然后我们回到LayoutInflater的inflate(XmlPullParser parser,ViewGroup root, boolean attachToRoot)方法的注释4处，填充temp下面的所有子view。

```
//注释4处，填充temp下面的所有子view
rInflateChildren(parser, temp, attrs, true);
```

LayoutInflater的rInflateChildren方法

```java
final void rInflateChildren(XmlPullParser parser, View parent, AttributeSet attrs, boolean finishInflate) throws XmlPullParserException, IOException {
    rInflate(parser, parent, parent.getContext(), attrs, finishInflate);
}
```

LayoutInflater的rInflate方法

```java
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
            } else if (TAG_INCLUDE.equals(name)) {
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
                //注释2处递归创建子View。
                rInflateChildren(parser, view, attrs, true);
                //注释3处，添加创建的View
                viewGroup.addView(view, params);
            }
        }

        if (pendingRequestFocus) {
            parent.restoreDefaultFocus();
        }

        if (finishInflate) {
            //结束填充
            parent.onFinishInflate();
        }
    }
```

填充完子View以后，我们回到注释5处，如果条件满足，应该把temp添加到root中去。

```
if (root != null && attachToRoot) {
    root.addView(temp, params);
}
```

到这里，整个inflate流程结束。

参考链接

* [Android LayoutInflater原理分析，带你一步步深入了解View(一)](https://blog.csdn.net/guolin_blog/article/details/12921889)