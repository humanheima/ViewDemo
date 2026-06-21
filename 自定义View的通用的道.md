# 自定义 View 的“通用的道”

很多人学自定义 View，是一个效果一个效果地学：今天抄一个波浪进度条，明天抄一个刻度尺。抄完能用，但换个需求又抓瞎。这是在学“术”——具体招式。

“道”是招式背后不变的东西：无论做什么效果，思考路径、职责划分、踩坑规律都是相通的。掌握了道，看到任何一个新效果，你都能在脑子里把它拆成几个已知的问题。

这篇文档不讲某个具体效果，只讲这套通用的方法论。

---

## 一、先问三个问题，再动手写代码

接到一个自定义 View 需求，不要急着继承 `View` 重写 `onDraw`。先回答：

**1. 我到底要不要“自定义绘制”？**

层级从轻到重，能用轻的就别用重的：

| 方案 | 适用场景 | 成本 |
| --- | --- | --- |
| 组合现有控件（继承 ViewGroup / 用 XML 组合） | 效果能用已有控件拼出来 | 最低 |
| 继承现有控件（如继承 TextView） | 在已有控件上加一点料 | 低 |
| 自定义 Drawable | 只是一块“可绘制内容”，要能复用、能当背景 | 中 |
| 继承 View 完全自绘 | 完全独特的视觉 / 交互 | 高 |
| 继承 ViewGroup 自定义布局 | 子 View 的摆放规则是独特的 | 最高 |

> 90% 的“自定义 View”需求，其实用组合或继承就能解决。真正需要从 `onDraw` 画起的没那么多。

**2. 这个 View 要不要管子 View？**

- 不管子 View → 继承 `View`，重点是 `onMeasure` + `onDraw`。
- 要管子 View 的摆放 → 继承 `ViewGroup`，重点是 `onMeasure` + `onLayout`，**通常不重写 onDraw**。

**3. 这个效果是“画出来的”还是“摆出来的”？**

- 画出来的（一条曲线、一个圆环、一段渐变）→ 是 `Canvas` + `Paint` 的活。
- 摆出来的（一堆子 View 按某种规则排列）→ 是测量与布局的活。

把需求归到正确的类别，后面就不会用错工具。

---

## 二、三大流程：measure / layout / draw

这是自定义 View 一切的地基。一句话概括：

> **测量**决定“我多大”，**布局**决定“我（的孩子）在哪”，**绘制**决定“我长什么样”。

### 1. Measure —— 必须处理 MeasureSpec 的三种模式

`onMeasure` 收到的不是一个尺寸，而是一个 `MeasureSpec`（模式 + 数值）。**自定义 View 最常见的 bug 就是不处理 `wrap_content`。**

| 模式 | 含义 | 对应 XML |
| --- | --- | --- |
| `EXACTLY` | 父布局已经定死尺寸，照着用 | `match_parent` / 具体 dp |
| `AT_MOST` | 你最大能到这么大，自己看着办 | `wrap_content` |
| `UNSPECIFIED` | 随便，要多大给多大 | ScrollView 子项等 |

通用写法（记住这个模板）：

```java
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = measureDimension(mDefaultWidth, widthMeasureSpec);
    int height = measureDimension(mDefaultHeight, heightMeasureSpec);
    setMeasuredDimension(width, height);
}

private int measureDimension(int desiredSize, int measureSpec) {
    int mode = MeasureSpec.getMode(measureSpec);
    int size = MeasureSpec.getSize(measureSpec);
    switch (mode) {
        case MeasureSpec.EXACTLY:   return size;                    // 听父布局的
        case MeasureSpec.AT_MOST:   return Math.min(desiredSize, size); // 不超过上限
        default:                    return desiredSize;             // wrap_content 时用自己想要的
    }
}
```

> 道：**`onMeasure` 的本质是一次“父子协商”**。父亲给约束（MeasureSpec），孩子报需求（desiredSize），最终必须调用 `setMeasuredDimension` 给出结论。不调用就会崩。

ViewGroup 的 measure 多一步：要先遍历测量每个子 View（`measureChild` / `measureChildWithMargins`），再根据子 View 的结果决定自己的尺寸。

### 2. Layout —— 只有 ViewGroup 才真正关心

普通自定义 View 一般不用重写 `onLayout`。ViewGroup 必须重写，核心就是对每个子 View 调用：

```java
child.layout(left, top, right, bottom);
```

这四个值是相对**父容器**的坐标。布局阶段做的事：拿着 measure 阶段算出的子 View 尺寸，决定每个孩子摆在哪个坐标。

> 道：**measure 和 layout 是分开的两步，不要在 layout 里再去算尺寸**。尺寸在 measure 阶段就该定好，layout 只负责摆位置。

### 3. Draw —— onDraw 里只做“画”，不做别的

绘制顺序（系统帮你串好的）：背景 → `onDraw`（自己的内容）→ `dispatchDraw`（子 View）→ 前景/装饰。

`onDraw(Canvas canvas)` 的铁律：

- **不要在 onDraw 里 new 对象**。onDraw 可能每秒被调几十次，`new Paint()` / `new Rect()` 会疯狂触发 GC。Paint、Path、Rect 一律在构造函数里创建，onDraw 里复用。
- **不要在 onDraw 里做耗时计算**。算好的结果缓存成成员变量。
- onDraw 只回答一个问题：“此刻我该长什么样”，它应该是当前状态的纯函数。

---

## 三、Canvas 与 Paint：自绘的两件兵器

如果落到了“自绘”，所有视觉都靠这两个类。理解它们的分工：

- **Paint 决定“怎么画”**：颜色、粗细、填充/描边（`Style`）、抗锯齿、字体、渐变（Shader）、混合模式（Xfermode）、阴影（ShadowLayer）。
- **Canvas 决定“画在哪、画什么”**：`drawCircle` / `drawPath` / `drawText` / `drawBitmap`……以及坐标系变换。

### 几个反复出现的“道”

**1. 一切复杂图形最终都能拆成基本图形或 Path。** 不会画？先想能不能用圆、矩形、圆角矩形、弧拼出来；拼不出来就用 `Path` 把轮廓描出来。曲线效果背后往往是数学（贝塞尔、三角函数），见本仓库 `三角函数.md`。

**2. 坐标系变换比逐点计算更省脑子。** 想旋转一个东西，不要去算每个点旋转后的坐标，而是 `canvas.rotate()` 把坐标系转过去，照常画，再转回来。`translate` / `rotate` / `scale` 配合 `save()` / `restore()` 使用：

```java
canvas.save();              // 保存当前坐标系
canvas.translate(cx, cy);   // 把原点挪到中心
canvas.rotate(angle);       // 旋转坐标系
// ... 在新坐标系下画，逻辑变简单
canvas.restore();           // 一定要还原，否则影响后续绘制
```

> 道：**save/restore 必须配对**。漏掉 restore，坐标系污染会让后面所有绘制错位，而且极难排查。

**3. 离屏缓冲与图层混合（进阶）。** 要做“两个图形相交部分镂空”这类效果，靠的是 `Xfermode`（PorterDuff），本质是图层混合。见本仓库 `Android 混合模式之 PorterDuffXfermode.md`、`BitmapShader的作用.md`。

---

## 四、状态与刷新：invalidate vs requestLayout

自定义 View 几乎都有“变化”——进度变了、被点击了、动画在跑。变化后怎么让界面更新？

- **只是外观变了，尺寸没变** → `invalidate()`（主线程）/ `postInvalidate()`（子线程）。只触发 `onDraw`，便宜。
- **尺寸 / 布局变了** → `requestLayout()`。会触发 measure + layout + draw，贵，慎用。

详见本仓库 `Android  invalidate 和 requestLayout 的区别.md`。

> 道：**把 View 当成“状态的渲染器”**。对外暴露 `setXxx()` 方法 → 改成员变量 → 调 `invalidate()`，让 onDraw 根据新状态重画。不要在外部直接操作画布。这就是“数据驱动绘制”，和现代 UI 框架（Compose）的思想是一致的。

---

## 五、触摸事件：让 View 活起来

需要交互时，重写 `onTouchEvent`，处理 `ACTION_DOWN / MOVE / UP`。

通用要点：

1. **返回值即“消费”**。`onTouchEvent` 返回 `true` 表示“这个事件我要了”，后续的 MOVE/UP 才会继续给你。`DOWN` 时返回 false，后面就没你的事了。
2. **滑动用 `getScaledTouchSlop` 判定阈值**，不要自己写死像素，保证不同屏幕手感一致。
3. **跟父容器抢事件用 `requestDisallowInterceptTouchEvent(true)`**，解决“我在滑，但被外层 ScrollView 抢走了”的嵌套滑动冲突。
4. 平滑滚动 / 惯性用 `Scroller` 或 `OverScroller` 配合 `computeScroll()`，见本仓库 `Scroller实现滚动的原理`。

> 道：**事件分发的核心是 `dispatchTouchEvent → onInterceptTouchEvent（仅 ViewGroup）→ onTouchEvent` 这条链，以及“谁消费谁后续接收”的规则。** 一切滑动冲突都是在这条链上的取舍。见 `Android事件分发机制.md`。

---

## 六、对外 API 设计：自定义属性与可配置性

一个能复用的自定义 View，要让别人在 XML 里就能配置它，而不是改源码。

1. 在 `res/values/attrs.xml` 用 `declare-styleable` 声明属性。
2. 在三参构造函数里通过 `obtainStyledAttributes` 读取，**用完一定 `recycle()`**。
3. 给每个属性提供对应的 `setter`，并在 setter 里按需 `invalidate()` / `requestLayout()`。

```java
public CustomView(Context c, AttributeSet attrs, int defStyle) {
    super(c, attrs, defStyle);
    TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomView);
    mColor = a.getColor(R.styleable.CustomView_barColor, Color.RED);
    a.recycle(); // 必须回收
}
```

> 道：**自定义 View 是一个“组件”，要按组件的标准对外提供接口**：XML 可配、代码可改、状态变化有回调（监听器）。`style` 和 `declare-styleable` 的关系见 `Android attr declare-styleable 和 style 的区别.md`。

---

## 七、生命周期与资源管理：别只盯着绘制

容易被忽略，但线上 bug 常出在这里：

- `onAttachedToWindow` / `onDetachedFromWindow`：**启动/停止动画、注册/反注册监听、申请/释放资源的对称点**。在 attach 里开的动画、起的 Handler，必须在 detach 里停掉，否则内存泄漏。
- `onSizeChanged`：尺寸确定后回调，**适合在这里计算依赖宽高的几何参数**（圆心、半径、Path），比在 onDraw 里反复算好。
- `onSaveInstanceState` / `onRestoreInstanceState`：横竖屏切换、Activity 重建时保存自己的状态（如当前进度）。

完整回调顺序见本仓库 `ViewLifecycle.md`。

> 道：**凡是“申请”的，都要有对称的“释放”**。attach↔detach、动画 start↔cancel、监听 register↔unregister。这是避免泄漏的根本原则，和绘制无关，但更致命。

---

## 八、性能：自定义 View 的几条红线

1. **onDraw 里零分配**——前面强调过，最重要。
2. **避免过度绘制（Overdraw）**——别画一堆看不见的、被盖住的东西；用 `clipRect` 裁剪只画需要的区域。
3. **慎用 requestLayout**——它会向上回溯整棵 View 树。
4. **大面积/复杂效果考虑硬件加速的限制**——少数 API（部分 Xfermode、`clipPath` 老版本）在硬件加速下行为不同，必要时用 `setLayerType` 局部关闭。
5. **能缓存就缓存**——静态内容画一次存成 Bitmap，后续直接 `drawBitmap`。

> 道：**自定义 View 的性能问题，几乎都源于“在高频回调里做了本不该在那做的事”**。把“算一次就够”的东西，从 onDraw（每帧）挪到 onSizeChanged / 构造函数（一次），是最通用的优化手段。

---

## 九、一套可复用的思考清单（Checklist）

下次再遇到任意一个自定义 View 需求，按这个顺序过一遍：

1. **选型**：组合 / 继承 / Drawable / 自绘 / 自定义 ViewGroup，选最轻的。
2. **归类**：这是“画”的活还是“摆”的活？
3. **测量**：怎么处理 `wrap_content`（AT_MOST）？默认尺寸是多少？
4. **几何**：依赖宽高的参数放到 `onSizeChanged` 算。
5. **绘制**：onDraw 里只画、不分配、不计算。
6. **状态**：对外 `setXxx` → 改成员 → `invalidate`。
7. **交互**：要不要处理触摸？有没有滑动冲突？
8. **API**：自定义属性 + setter + 回调监听。
9. **生命周期**：attach/detach 成对管理资源，状态要不要保存。
10. **性能**：复查 onDraw 分配、overdraw、requestLayout 频率。

能把这十条想清楚，再具体的效果都只是“在这套框架里填数学”。

---

## 一句话总结

> **术是“这个效果怎么画”，道是“任何效果都遵循的那套流程与约束”。**
> measure 协商尺寸、layout 摆放位置、draw 纯函数渲染、状态驱动刷新、资源对称管理——这五件事不变，变的只是 onDraw 里那段数学。
