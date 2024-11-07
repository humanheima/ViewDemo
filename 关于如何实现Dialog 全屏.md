# 关于如何实现Dialog/DialogFragment全屏

### 第一阶段，完全使用代码来控制。

```kotlin
class FullScreenDialogFragment : DialogFragment() {

    private val TAG = "FullScreenDialogFragmen"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val bind = FullSCreenDialogBinding.inflate(inflater, container, false)
        val view: View = inflater.inflate(R.layout.full_screen_dialog_frag, container, false)
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //貌似高版本不调用也可以。多高的版本呢？猜测是5.0以上。
        //dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0x00000000))
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

}
```

### 使用Theme 来控制。

1. 在 styles.xml 里面定义 FullScreenDialog

```xml


<style name="FullScreenDialog" parent="Theme.AppCompat.Dialog">
    <item name="android:windowNoTitle">true</item>
    <item name="android:windowBackground">@color/transparent</item>
    <item name="android:windowIsFloating">false</item>

</style>
```

2. 在代码里使用

```xml
class FullScreenDialogFragmentUseTheme : DialogFragment() {

    private val TAG = "FullScreenDialogFragmen"


    override fun onCreate(savedInstanceState: Bundle?) {super.onCreate(savedInstanceState)//注释1处，使用 ThemesetStyle(STYLE_NORMAL, R.style.FullScreenDialog)}

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {val view: View = inflater.inflate(R.layout.full_screen_dialog_frag, container, false)return view}

    }
```

注释1处，使用 Theme。 注意，使用 Theme 的时候，貌似会影响布局文件中，部分控件的默认的样式。比如EditText的
textColorHint 。最终发现是父级Platform.AppCompat里面指定了textColorHint属性。

PhoneWindow 的 generateLayout 方法里面获取了这些属性

```java

protected ViewGroup generateLayout(DecorView decor) {

    mIsFloating = a.getBoolean(R.styleable.Window_windowIsFloating, false);
    int flagsToUpdate = (FLAG_LAYOUT_IN_SCREEN | FLAG_LAYOUT_INSET_DECOR)
            & (~getForcedWindowFlags());
    if (mIsFloating) {
        setLayout(WRAP_CONTENT, WRAP_CONTENT);
        setFlags(0, flagsToUpdate);
    } else {
        setFlags(FLAG_LAYOUT_IN_SCREEN | FLAG_LAYOUT_INSET_DECOR, flagsToUpdate);
    }

    if (a.getBoolean(R.styleable.Window_windowNoTitle, false)) {
        requestFeature(FEATURE_NO_TITLE);
    } else if (a.getBoolean(R.styleable.Window_windowActionBar, false)) {
        // Don't allow an action bar if there is no title.
        requestFeature(FEATURE_ACTION_BAR);
    }

    //...

}
```

### 创建沉浸式全屏DialogFragment

```kotlin   

class FullScreenDialogFragmentUseTheme : DialogFragment() {

    private val TAG = "FullScreenDialogFragmen"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //注释1处，设置全屏
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.full_screen_dialog_frag, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        /**
         * 注释2处，如果要延伸到状态栏，可以使用这个来实现
         */
        if (dialog?.window?.getDecorView() != null) {
            dialog?.window?.getDecorView()?.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
        }
    }

}

```

注释1处，设置全屏。
注释2处，如果要延伸到状态栏，可以使用这个来实现。




参考链接：[三句代码创建全屏Dialog或者DialogFragment：带你从源码角度实现](https://www.jianshu.com/p/3ecad4bfc55e)