# WaveProgressView 水波进度球 —— 实现思路

> 一个圆形容器，里面的水面随进度上涨，水面上有两层持续流动的正弦波。
> 本文按「拿到需求后脑子里该怎么想」的顺序拆解，而不是贴完代码讲一遍。
> 配套的通用方法论见 [自定义View的通用的道.md](自定义View的通用的道.md)。

---

## 一、先把效果拆成几个独立问题

看到这个效果，别想着「水波进度条怎么写」，而要拆成几个**互不相关、可以各个击破**的子问题：

| 子问题 | 本质 | 用什么解决 |
| --- | --- | --- |
| 怎么画一条波浪线 | 一个正弦函数 | `sin` + `Path` 逐点连线 |
| 波浪怎么流动 | 正弦函数整体左右平移 | 给 sin 加一个随时间变化的相位 `phase` |
| 水面怎么随进度升降 | 波浪线的基准高度上移 | progress → 一个 y 坐标 `waterLine` |
| 怎么只显示圆里的水 | 把方形的波浪裁成圆 | 离屏图层 + `PorterDuff.DST_IN` |
| 波浪有层次感 | 两层波叠加 | 画两条不同相位/振幅的波 |

拆完会发现：**没有一个是「水波进度条」专有的难题**，全是「正弦曲线」「平移动画」「圆形裁剪」这些已知套路的组合。这就是拆解的意义。

---

## 二、核心：一条会动的正弦波

整个 View 的地基是这一行公式（写在类注释里）：

```
y = amplitude * sin(2π/λ * x + phase) + waterLine
```

逐项对应到代码：

| 数学符号 | 含义 | 代码 |
| --- | --- | --- |
| `amplitude` | 振幅，波有多高 | `amp = radius * 2f * amplitudeRatio` |
| `λ`（波长） | 一个完整波占多少像素 | `waveLen = diameter / waveCount` |
| `2π/λ` | 角频率 `k`，喂给 sin 的频率 | `k = 2 * Math.PI / waveLen` |
| `x` | 当前横坐标 | 循环里从 `left` 走到 `right` |
| `phase` | 相位，决定波左右平移多少 | `phase1` / `phase2`，动画驱动 |
| `waterLine` | 水面基准高度 | 由 progress 算出 |

### 2.1 为什么要 `k = 2π / waveLen`

`sin` 的天然周期是 `2π`：x 每增加 `2π`，波形重复一次。但我们想让波形每隔 **`waveLen` 像素**重复一次，所以要把 x「压缩」一下——乘上 `k = 2π / waveLen`。

这样当 x 走过 `waveLen` 像素时，`k*x` 正好累加 `2π`，sin 走完一个完整周期 → 画出一个完整的波。

### 2.2 waveCount → waveLen → k 这条换算链

为什么不直接让用户填 `waveLen`？因为像素值不直观（不同尺寸的球，同一个像素波长看起来疏密完全不同）。

所以对外暴露的是**与尺寸无关的密度** `waveCount`（「整个球宽里有几个波」），内部再换算成像素：

```
waveCount（我要几个波，直观）
   → waveLen = diameter / waveCount（每个波多少像素）
   → k = 2π / waveLen（喂给 sin 的角频率）
```

这是「用户友好参数」到「数学需要参数」的桥梁。这种「对外给直观比例、对内换算成绝对值」的设计在自定义 View 里非常通用（振幅 `amplitudeRatio` 同理，用的是占半径的比例而非绝对像素）。

### 2.3 怎么把公式变成一条 Path

`Canvas` 不能直接画函数曲线，只能画线段。所以沿 x 方向**每隔 2px 采样一个点**，用 `lineTo` 连起来，点足够密就看着是平滑曲线：

```kotlin
wavePath.moveTo(left, bottom)          // 从左下角起笔
var x = left
while (x <= right) {
    val y = amp * sin(k * (x - left) + phase) + waterLine
    wavePath.lineTo(x, y)              // 沿曲线走
    x += 2f                            // 步长 2px：平滑且不费
}
wavePath.lineTo(right, bottom)         // 落到右下角
wavePath.close()                       // 闭合 → 得到“曲线以下的实心填充”
```

注意闭合方式：起点和终点都落在 `bottom`（圆的最低处），中间沿曲线走。`close()` 后得到的是**波浪线下方的整块填充**——这才是「水」，不是一条线。

> 步长 2px 是性能与精度的平衡点。1px 更平滑但点数翻倍；5px 在大球上会看出折线。

---

## 三、水面高度：progress 怎么变成一个 y 坐标

```kotlin
val waterLine = cy + radius - (progress / 100f) * diameter
```

理解它要记住 Android 坐标系 **y 轴向下为正**：

- `cy + radius` = 圆的**最低点**（progress=0，空）。
- 减去 `(progress/100) * diameter` = 进度越大，水面越往上（y 越小）。
- progress=100 时 `waterLine = cy + radius - diameter = cy - radius` = 圆的**最高点**（满）。

`waterLine` 是 sin 公式里的基准线，波就围绕它上下 `±amp` 摆动。

---

## 四、两层波叠加：层次感从哪来

```kotlin
wavePaint.alpha = 255
drawWave(..., amp,        waveLen, phase1)   // 第一层：实心
wavePaint.alpha = 120
drawWave(..., amp * 0.8f, waveLen, phase2)   // 第二层：半透明、振幅略小
```

两层用**不同相位**（`phase1` / `phase2`）、**不同振幅**，且第二层半透明。两条波交错叠加，重叠区颜色更深，分离区露出底层——视觉上就有了「水在翻涌」的纵深感。单层波会显得很假、很平。

---

## 五、动画：相位平移 + 一个连续性陷阱 ⚠️

让波流动，本质是让 `phase` 随时间线性增加——波形整体左右平移。

```kotlin
animator = ValueAnimator.ofFloat(0f, (2 * Math.PI).toFloat()).apply {
    duration = 1500
    interpolator = LinearInterpolator()        // 匀速，必须线性
    repeatCount = INFINITE
    addUpdateListener {
        val base = it.animatedValue as Float    // base: 0 → 2π 循环
        phase1 = base
        phase2 = base * -2f
        invalidate()
    }
}
```

### 这里有个隐蔽的坑：第二层波「突然消失重来」

动画每轮让 `base` 从 `0` 走到 `2π` 再跳回 `0`。要让波在回绕瞬间**不跳变**，回绕前后的 `phase` 必须落在 sin 的同一相位（相差 `2π` 的整数倍）：

| 写法 | 回绕前 phase | 对 2π 取模 | 结果 |
| --- | --- | --- | --- |
| `phase = base`（1 倍） | `2π` | `0` | ✅ 连续 |
| `phase = base * -2`（整数倍） | `-4π` | `0` | ✅ 连续 |
| `phase = base * 1.4`（非整数倍） | `2.8π` | `0.8π ≠ 0` | ❌ 跳变 |

**结论：第二层的速度倍数必须是整数**（正负皆可，负号代表反向流动）。非整数倍会在动画回绕点产生一个 `0.8π` 的相位突跳，肉眼看就是「波突然消失又重来」。

### 如果就想要任意速度比（比如 1.4 倍慢速差）

那就别把相位绑在「会回绕的 base」上，改成**每帧自增、各自对 2π 取模**，这样和动画回不回绕无关：

```kotlin
phase1 = (phase1 + 0.08f) % TWO_PI
phase2 = (phase2 + 0.08f * 1.4f) % TWO_PI   // 1.4 倍速也不跳
```

当前实现用的是「整数倍」方案（最省事）；要自由调速就换成「增量+取模」方案。

---

## 六、圆形裁剪：为什么用 PorterDuff 而不是 clipPath

前面画的波浪填充是**方形**的，要把它裁成圆。两种做法：

| 方案 | 问题 |
| --- | --- |
| `canvas.clipPath(circlePath)` | 老版本不抗锯齿，圆边缘有锯齿（毛刺） |
| **离屏图层 + `DST_IN`** | 边缘平滑，本项目采用 |

`DST_IN` 的含义：**只保留「目标(已画的波) 和 源(新画的圆) 都不透明」的像素**，相当于用圆形当遮罩。流程：

```kotlin
val layer = canvas.saveLayer(...)        // ① 开离屏图层
drawWave(...)                            // ② 在图层里画方形的波（DST，目标）
circlePaint.xfermode = dstIn
canvas.drawCircle(cx, cy, radius, ...)   // ③ 画圆（SRC，源）→ 只留交集
circlePaint.xfermode = null
canvas.restoreToCount(layer)            // ④ 合成回主画布 → 圆里的水
```

> 关键点：必须用 `saveLayer` 开**独立图层**。Xfermode 是「当前图层内已有内容」和「新画内容」之间的运算，不开图层会和屏幕上的其它东西错误地混合。
> PorterDuff 各模式的详解见 [Android 混合模式之 PorterDuffXfermode.md](Android%20混合模式之%20PorterDuffXfermode.md)。

---

## 七、完整绘制顺序（onDraw 一帧做的事）

绘制是**有先后的图层叠加**，顺序错了就互相盖住：

```
1. 算几何量：直径、圆心、半径、振幅、水面高度、波长
2. saveLayer 开离屏图层
   2a. 画第一层波（实心）
   2b. 画第二层波（半透明）
   2c. DST_IN 叠圆 → 裁成“圆里的水”
   restoreToCount 合回主画布
3. 画圆环边框（描边，盖在水上面）
4. 画中间百分比文字（最上层）
```

文字放最后画，保证它永远不被水盖住。

### 文字垂直居中的小细节

```kotlin
val baseline = cy - (fm.ascent + fm.descent) / 2f
```

`drawText` 的 y 是**基线(baseline)**不是中心。要让文字视觉居中，得用 `ascent`（基线到顶，负值）和 `descent`（基线到底，正值）把基线从中心往下偏半个字高。这是文字绘制的通用套路。

---

## 八、性能与生命周期

- **无内存分配**：`Paint` / `Path` / `RectF` 全部在构造时创建成成员变量，`onDraw` 里只复用不 new。动画 60fps 下 onDraw 每秒调 60 次，这里绝不能创建对象，否则频繁 GC 卡顿。
- **采样步长 2px**：onDraw 里的点数 ≈ `diameter / 2`，几百个点，CPU 毫无压力。
- **防泄漏**：`onDetachedFromWindow` 里 `stopAnimation()` 取消动画。无限动画不取消会持有 View 引用导致 Activity 泄漏。
- **saveLayer 有成本**：离屏图层是额外的一块 bitmap，但这里只有一个、范围是 View 大小，可接受。如果一个屏幕里有几十个这种球，要考虑换方案。

---

## 九、对外可调参数一览

| 属性 | 作用 | 设计要点 |
| --- | --- | --- |
| `progress` | 进度 0~100 → 水面高度 | `coerceIn` 钳制范围 |
| `amplitudeRatio` | 振幅占半径比例 | 用比例不用像素，与尺寸解耦；上限 0.3 防飞出圆 |
| `waveCount` | 直径内波个数 | 直观密度参数，内部换算成 waveLen |
| `waveColor` / `ringColor` / `ringWidth` | 颜色、环宽 | —— |

每个 setter 里都调 `invalidate()` 触发重绘——属性一改，画面立刻更新。

---

## 十、一句话总结这套思路

> **把「水波进度球」拆成「正弦曲线 + 相位平移 + 进度映射 + 圆形裁剪 + 图层叠加」五个已知问题，逐个用通用套路解决，最后按正确顺序叠起来。**

整个过程没有任何「水波专有」的黑魔法，难点只有两个值得记住：

1. **相位连续性**——动画回绕点要求相位倍数是整数，否则波会跳变。
2. **圆形裁剪用 DST_IN 而非 clipPath**——边缘更平滑。

其余全是正弦函数和绘制顺序的常规操作。
