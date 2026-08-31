# ViewCompat.setOnApplyWindowInsetsListener 用法

`ViewCompat.setOnApplyWindowInsetsListener` 用于在 View 收到系统 Insets 时调整布局。它是 AndroidX 对 `View.setOnApplyWindowInsetsListener` 的兼容封装，适合处理状态栏、导航栏、刘海/打孔、软键盘（IME）和手势区域与内容的避让。

> 本文使用 Kotlin 示例。项目已引入 `androidx.core:core-ktx`，如果是新项目，请确保已添加 AndroidX Core 依赖。

## 1. 先理解 WindowInsets

`WindowInsetsCompat` 表示系统窗口与应用内容区域之间的重叠信息。它不是一个应该直接设置到 View 的对象，而是告诉应用：内容在各个方向需要避让多少像素。

`WindowInsetsCompat.Type` 常用类型如下：

| 类型 | 用途 |
| --- | --- |
| `statusBars()` | 状态栏占用区域 |
| `navigationBars()` | 导航栏或三键导航占用区域 |
| `systemBars()` | 状态栏、导航栏等系统栏的合集 |
| `displayCutout()` | 刘海、打孔等显示屏切口 |
| `ime()` | 软键盘占用区域 |
| `systemGestures()` | 系统手势可用的边缘区域 |

`insets.getInsets(typeMask)` 返回一个 `Insets` 对象，包含 `left`、`top`、`right` 和 `bottom`。例如：

```kotlin
val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
val top = bars.top
val bottom = bars.bottom
```

`getInsets()` 用于当前可见状态下的避让值。如果需要获取即使系统栏被隐藏也可能占用的值，使用 `getInsetsIgnoringVisibility()`。

## 2. 最小可用示例

```kotlin
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
    val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    view.setPadding(bars.left, bars.top, bars.right, bars.bottom)

    // 返回原始 insets，让子 View 仍然能收到分发。
    insets
}

// 如果 View 已经 attach，可手动请求立即分发。
ViewCompat.requestApplyInsets(view)
```

### 调用时机

Listener 可能在以下情况被调用：

- View 首次 attach 到窗口时；
- 状态栏、导航栏、键盘或旋转等导致 Insets 变化时；
- 调用 `ViewCompat.requestApplyInsets(view)` 后。

Insets 回调不是一次性初始化。必须使用每次回调提供的最新数据，并且避免将上一次回调已加上的 padding 再次累加。

## 3. 边到边（edge-to-edge）布局

如果内容需要延伸到状态栏或导航栏后面，先关闭窗口的默认避让，再由应用根据 Insets 决定哪些控件加 padding 或 margin：

### `setDecorFitsSystemWindows` 的作用

`WindowCompat.setDecorFitsSystemWindows(window, false)` 控制的是窗口的根布局是否由系统自动避让系统栏：

| 参数 | 布局行为 | 应用需要做什么 |
| --- | --- | --- |
| `true` | Decor View 会按系统窗口区域布局，内容通常不会被状态栏或导航栏遮挡 | 一般不需要手动处理系统栏 Insets |
| `false` | 内容允许延伸到状态栏、导航栏后面，启用边到边布局 | 使用 `WindowInsets` 为 Toolbar、列表、底部按钮等需要避让的控件补上 padding 或 margin |

这个 API 只改变内容的布局范围，不负责设置状态栏/导航栏颜色、切换图标明暗，也不会自动为每个子 View 添加 padding。即使设置了 `false`，仍然要在合适的 View 上安装 Insets listener；否则内容可能被系统栏或软键盘遮挡。通常应在 `setContentView` 之前调用：

```kotlin
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContentView(R.layout.activity_main)

    val root = findViewById<View>(R.id.root)
    ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or
                WindowInsetsCompat.Type.displayCutout()
        )
        root.updatePadding(top = bars.top, bottom = bars.bottom)
        insets
    }
    ViewCompat.requestApplyInsets(root)
}
```

`setDecorFitsSystemWindows(window, false)` 只是让内容可以延伸到系统栏后面，它不会自动为工具栏、列表或底部按钮留出空间。这些避让必须在 Insets listener 中明确处理。

### 避免 padding 累加

下面的写法会在回调重新触发时反复累加：

```kotlin
// 错误：第二次回调会把第一次的 bottom 再加一遍。
view.updatePadding(bottom = view.paddingBottom + bars.bottom)
```

正确做法是在安装 listener 前保存布局文件或代码中的初始 padding：

```kotlin
import androidx.core.view.updatePadding

val initialLeft = view.paddingLeft
val initialTop = view.paddingTop
val initialRight = view.paddingRight
val initialBottom = view.paddingBottom

ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
    val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    view.updatePadding(
        left = initialLeft + bars.left,
        top = initialTop + bars.top,
        right = initialRight + bars.right,
        bottom = initialBottom + bars.bottom
    )
    insets
}
```

## 4. 常见场景

### 4.1 顶部 Toolbar 避让状态栏

Toolbar 背景可以延伸到状态栏，但文字和图标需要在状态栏下方：

```kotlin
val initialToolbarTop = toolbar.paddingTop

ViewCompat.setOnApplyWindowInsetsListener(toolbar) { _, insets ->
    val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
    toolbar.updatePadding(top = initialToolbarTop + status.top)
    insets
}
```

### 4.2 底部操作栏同时避让导航栏和键盘

底部按钮在键盘弹出后应该位于键盘之上；键盘收起后则避让导航栏。取两者较大值可以避免重叠：

```kotlin
val initialBottom = bottomBar.paddingBottom

ViewCompat.setOnApplyWindowInsetsListener(bottomBar) { _, insets ->
    val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
    val bottom = maxOf(bars.bottom, ime.bottom)

    bottomBar.updatePadding(bottom = initialBottom + bottom)
    insets
}
```

如果只想在键盘可见时处理 IME，可以同时判断：

```kotlin
val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
```

### 4.3 RecyclerView 或 ScrollView

滚动容器通常需要避让四个边，同时保留它自己的初始 padding：

```kotlin
val initialLeft = recyclerView.paddingLeft
val initialTop = recyclerView.paddingTop
val initialRight = recyclerView.paddingRight
val initialBottom = recyclerView.paddingBottom

ViewCompat.setOnApplyWindowInsetsListener(recyclerView) { _, insets ->
    val safe = insets.getInsets(
        WindowInsetsCompat.Type.systemBars() or
            WindowInsetsCompat.Type.displayCutout()
    )
    recyclerView.updatePadding(
        left = initialLeft + safe.left,
        top = initialTop + safe.top,
        right = initialRight + safe.right,
        bottom = initialBottom + safe.bottom
    )
    insets
}
```

列表内容如果已经通过 `clipToPadding="false"` 绘制到 padding 区域，配合底部 padding 可以让最后一项滚动到导航栏上方；是否开启该属性应根据交互设计决定。

## 5. 回调的返回值与分发

Insets 会沿着 View 树从父到子分发。Listener 最后返回哪个 `WindowInsetsCompat` 很重要：

```kotlin
ViewCompat.setOnApplyWindowInsetsListener(parent) { _, insets ->
    // 父 View 根据 insets 调整自己的布局
    insets // 继续把原始 insets 交给子 View
}
```

- 返回原始 `insets`：表示当前 View 处理自己的避让，但仍然允许后代继续处理，这是最常用的返回值。
- 返回 `WindowInsetsCompat.CONSUMED`：表示当前分支已经消费 Insets，后续 View 可能收不到这次分发。只在当前层明确拥有这些 Insets 的处理权时才使用。
- 返回修改后的 `WindowInsetsCompat`：适合自定义容器需要向子 View 传递“剩余” Insets 的情况，但这比直接返回原始对象更容易出错。

一般不要在只修改了自己 padding 的情况下返回 `CONSUMED`，否则容易导致子控件、其他页面或低版本设备的避让失效。

## 6. 和其他 API 的区别

### `WindowInsetsControllerCompat`

`WindowInsetsControllerCompat` 负责显示或隐藏系统栏、设置状态栏图标深浅色等窗口控制；`setOnApplyWindowInsetsListener` 负责读取占用区域并调整 View 布局。两者可以同时使用，职责不同。

### `android:fitsSystemWindows`

`fitsSystemWindows` 是较早的自动避让机制，行为依赖控件类型和系统版本，不适合复杂的键盘、刘海或多层嵌套布局。采用边到边方案时，通常保持只有一种负责避让的方式，否则可能出现重复 padding。

### `WindowInsetsAnimationCompat`

Listener 可以获取键盘动画的最终状态，但不会自动让布局随键盘滑动。要实现平滑跟手，另外使用 `WindowInsetsAnimationCompat` 在动画进度中更新 View。

## 7. 自定义 View 的写法

可以在自定义 View 内部安装 listener，但要避免把外部使用者可能需要的 listener 覆盖掉。如果自定义 View 自己就要处理 Insets，也可以覆写 `onApplyWindowInsets` 并保留返回值：

```kotlin
import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class InsetsAwareLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val initialLeft = paddingLeft
    private val initialTop = paddingTop
    private val initialRight = paddingRight
    private val initialBottom = paddingBottom

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        val compat = WindowInsetsCompat.toWindowInsetsCompat(insets, this)
        val bars = compat.getInsets(WindowInsetsCompat.Type.systemBars())
        updatePadding(
            left = initialLeft + bars.left,
            top = initialTop + bars.top,
            right = initialRight + bars.right,
            bottom = initialBottom + bars.bottom
        )
        return super.onApplyWindowInsets(insets)
    }
}
```

如果只需要在页面中配置某个已有控件，优先使用 `ViewCompat.setOnApplyWindowInsetsListener`，这样不需要改写控件基类。

## 8. 常见问题排查

1. **回调没有触发**：确认 listener 设置在正确的 View 上，View 已经加载后调用 `ViewCompat.requestApplyInsets(view)`，并检查父 View 是否返回了 `CONSUMED`。
2. **内容仍然被状态栏挡住**：边到边布局需要 `WindowCompat.setDecorFitsSystemWindows(window, false)`，并确认是否在正确的子 View 上调整了 padding/margin。
3. **内容距离越来越大**：回调被多次调用时重复累加了当前 padding，应改为“初始 padding + 当前 Insets”。
4. **键盘弹出时底部按钮被盖住**：同时读取 `Type.ime()` 和 `Type.navigationBars()`，使用两者 bottom 的较大值。
5. **状态栏和导航栏颜色不正确**：这属于系统栏外观和图标对比度配置，使用 `WindowInsetsControllerCompat`，不要试图在 Insets 回调里修改颜色。

## 9. 推荐模板

对大多数 Activity，可以遵循以下顺序：

1. 根据设计决定是否开启 `WindowCompat.setDecorFitsSystemWindows(window, false)`。
2. 在需要避让的具体 View 上设置 listener，而不是所有子 View 都设置一遍。
3. 用 `getInsets(Type...)` 读取需要的系统区域，用初始 padding 加 Insets 计算最终值。
4. 回调结束返回原始 `insets`，除非有明确的消费和重新分发设计。
5. 用真机或模拟器分别测试三键导航、手势导航、键盘、刘海/打孔和旋转。

## 参考资料

- [ViewCompat.setOnApplyWindowInsetsListener](https://developer.android.com/reference/androidx/core/view/ViewCompat#setOnApplyWindowInsetsListener(android.view.View,androidx.core.view.OnApplyWindowInsetsListener))
- [WindowInsetsCompat](https://developer.android.com/reference/androidx/core/view/WindowInsetsCompat)
- [Handle edge-to-edge](https://developer.android.com/develop/ui/views/layout/edge-to-edge)
- [WindowInsetsAnimationCompat](https://developer.android.com/reference/androidx/core/view/WindowInsetsAnimationCompat)
