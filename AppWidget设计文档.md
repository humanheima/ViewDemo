# 桌面小组件（AppWidget）设计文档

> 项目：ViewDemo  
> 包名：`com.hm.viewdemo`  
> 最后更新：2026-05-12

---

## 目录

1. [功能概述](#1-功能概述)
2. [整体架构](#2-整体架构)
3. [文件清单](#3-文件清单)
4. [数据模型](#4-数据模型)
5. [核心组件详解](#5-核心组件详解)
   - 5.1 [DreamChatWidgetProvider](#51-dreamchatwidgetprovider)
   - 5.2 [AppWidgetDemoActivity](#52-appwidgetdemoactivity)
6. [图片加载机制](#6-图片加载机制)
7. [刷新流程](#7-刷新流程)
8. [MIUI / HyperOS 兼容性](#8-miui--hyperos-兼容性)
9. [AndroidManifest 配置](#9-androidmanifest-配置)
10. [关键设计决策](#10-关键设计决策)

---

## 1. 功能概述

实现一个「筑梦岛」主题的 Android 桌面小组件，展示 4 名角色头像和名称，支持：

| 功能 | 触发方式 |
|---|---|
| 添加小组件到桌面 | `AppWidgetDemoActivity` → 按钮 |
| 点击小组件区域打开 App | 小组件整体点击 |
| 点击刷新按钮拉取新数据 | 小组件右上角刷新图标 |
| App 内模拟网络刷新 | `AppWidgetDemoActivity` → 按钮 |

---

## 2. 整体架构

```
┌─────────────────────────────────────────┐
│              MainActivity               │
│   btn_widget_demo ──► AppWidgetDemoActivity.launch()
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│          AppWidgetDemoActivity          │
│                                         │
│  [添加到桌面]                           │
│    └─► AppWidgetManager.requestPinAppWidget()
│         └─► (MIUI兜底) 系统选择器 Intent
│                                         │
│  [模拟网络刷新]                          │
│    └─► Executor(后台线程, sleep 1.5s)   │
│         └─► DreamChatWidgetProvider     │
│              .refreshWithData(ctx, data)│
└─────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│              DreamChatWidgetProvider                │
│                    (BroadcastReceiver)              │
│                                                     │
│  onUpdate()                                         │
│    └─► updateSingleWidget(... defaultData())        │
│                                                     │
│  onReceive(ACTION_REFRESH)                          │
│    └─► IMAGE_EXECUTOR 后台线程                       │
│         ├─ sleep 1s (模拟网络)                       │
│         └─► refreshWithData(ctx, newData(REFRESH_URL))
│                                                     │
│  companion object                                   │
│    ├─ refreshWithData()  ◄── Activity 也会调用       │
│    ├─ updateSingleWidget()                          │
│    ├─ fetchImageAsync()  ── LruCache + 线程池        │
│    └─ downloadBitmap()   ── HttpURLConnection        │
└─────────────────────────────────────────────────────┘

         ▼ RemoteViews.partiallyUpdateAppWidget()
┌─────────────────────────────┐
│   widget_dream_chat.xml     │  桌面渲染层
│   (RemoteViews Layout)      │
└─────────────────────────────┘
```

---

## 3. 文件清单

| 类型 | 路径 | 说明 |
|---|---|---|
| Kotlin | `widget/DreamChatWidgetProvider.kt` | AppWidget 核心逻辑 |
| Kotlin | `widget/WidgetData.kt` | 数据模型 |
| Kotlin | `activity/AppWidgetDemoActivity.kt` | 演示控制页 |
| XML Layout | `res/layout/widget_dream_chat.xml` | 小组件布局（RemoteViews） |
| XML Layout | `res/layout/activity_app_widget_demo.xml` | 演示页布局 |
| XML | `res/xml/dream_chat_widget_info.xml` | AppWidget 元数据 |
| Drawable | `res/drawable/bg_dream_widget.xml` | 小组件主背景（圆角深色卡片） |
| Drawable | `res/drawable/bg_dream_widget_item.xml` | 角色项背景 |
| Drawable | `res/drawable/bg_dream_refresh.xml` | 刷新按钮背景 |
| Drawable | `res/drawable/preview_dream_chat_widget.xml` | MIUI 预览图（layer-list） |

---

## 4. 数据模型

```kotlin
// widget/WidgetData.kt
data class WidgetData(
    val title: String,          // 主标题，如 "跟你的梦中人聊聊"
    val brandName: String,      // 品牌名，如 "筑梦岛"
    val characters: List<Character>
) {
    data class Character(
        val name: String,       // 角色名
        val avatarUrl: String   // 头像网络 URL
    )
}
```

**默认数据（首次渲染 / 系统触发更新）**

```
title     = "跟你的梦中人聊聊"
brandName = "筑梦岛"
characters: 红羽 / 君山 / 摄羽 / 鹤白  (均使用 DEFAULT_URL)
```

---

## 5. 核心组件详解

### 5.1 DreamChatWidgetProvider

继承自 `AppWidgetProvider`（本质是 `BroadcastReceiver`）。

#### 生命周期回调

| 回调 | 触发时机 | 处理 |
|---|---|---|
| `onUpdate()` | 系统首次添加或周期更新 | 用 `defaultData()` 渲染所有 Widget ID |
| `onReceive(ACTION_REFRESH)` | 小组件刷新按钮点击 | 模拟网络请求，加载 `REFRESH_URL` 后调 `refreshWithData()` |

#### companion object 关键方法

```kotlin
// 外部入口：Activity 或其他组件刷新所有已添加的小组件
fun refreshWithData(context: Context, data: WidgetData)

// 渲染单个 Widget（文字同步 + 图片异步）
fun updateSingleWidget(context, appWidgetManager, appWidgetId, data: WidgetData)
```

#### URL 常量

| 常量 | 值 | 用途 |
|---|---|---|
| `DEFAULT_URL` | `.../6db4fb37.u34k62.png` | 首次渲染默认头像 |
| `REFRESH_URL` | `.../163fb33f.clpj4j.png` | 点击小组件刷新按钮后下发的新图 |

#### 线程模型

```
主线程
  └─ onReceive(ACTION_REFRESH)
       └─ IMAGE_EXECUTOR (FixedThreadPool 2)
            ├─ sleep 1000ms（模拟网络）
            └─ refreshWithData()
                 └─ updateSingleWidget()
                      └─ fetchImageAsync()
                           └─ IMAGE_EXECUTOR
                                └─ LruCache / downloadBitmap()
                                     └─ partiallyUpdateAppWidget()  ✓ 系统 API，可跨进程安全调用
```

> **注意**：`partiallyUpdateAppWidget()` 是 AppWidgetManager 提供的系统 API，内部通过 Binder IPC 将图片传递给桌面进程，不需要回到主线程。

### 5.2 AppWidgetDemoActivity

演示用控制页，继承自 `BaseActivity<ActivityAppWidgetDemoBinding>`。

#### 布局元素

| View ID | 类型 | 说明 |
|---|---|---|
| `tv_status` | TextView | 当前操作状态描述 |
| `btn_pin_widget` | Button | 请求将小组件固定到桌面 |
| `btn_simulate_refresh` | Button | 模拟网络请求并刷新小组件 |
| `progress_bar` | ProgressBar (horizontal) | 请求进行中时显示 |
| `tv_log` | TextView | 操作日志（monospace） |

#### 模拟刷新流程

```
点击 btn_simulate_refresh
  │
  ├─ UI: setLoading(true), appendLog("⏳ 发起网络请求...")
  │
  └─ executor.execute {
       Thread.sleep(1500)        // 模拟网络延迟 1.5s
       构造 newData(NEW_IMAGE_URL)
       mainHandler.post { appendLog("✅ 数据返回") }
       DreamChatWidgetProvider.refreshWithData(ctx, newData)
       mainHandler.post { setLoading(false), updateStatus("✅ 更新完成") }
     }
```

> `NEW_IMAGE_URL` = `.../5c9f9dde.96zd2r.png`（Activity 触发刷新使用的 URL，与小组件按钮使用的 `REFRESH_URL` 不同，便于区分两条路径）

---

## 6. 图片加载机制

AppWidget 的 `RemoteViews` 运行在桌面进程，**不能使用 Glide / Picasso 等第三方库**，必须手动实现。

### 实现方案

```
fetchImageAsync(url, callback)
  │
  ├─ 命中 LruCache → 直接 callback(bitmap)
  │
  └─ 未命中 → IMAGE_EXECUTOR 后台线程
                 downloadBitmap(url)
                   └─ HttpURLConnection
                        connectTimeout = 10s
                        readTimeout    = 10s
                 LruCache.put(url, bitmap)
                 callback(bitmap)
                   └─ RemoteViews.setImageViewBitmap(viewId, bitmap)
                        └─ partiallyUpdateAppWidget(appWidgetId, partial)
```

### LruCache 配置

```kotlin
private val IMAGE_CACHE = LruCache<String, Bitmap>(8)  // 最多缓存 8 张
```

每次刷新同一 URL 时命中缓存，避免重复下载。若需要强制重新下载（如服务端同 URL 换图），可在刷新前调 `IMAGE_CACHE.remove(url)`。

---

## 7. 刷新流程

### 路径 A：小组件刷新按钮

```
用户点击小组件刷新图标
  │
  └─ BroadcastReceiver.onReceive(ACTION_REFRESH)
       │
       └─ IMAGE_EXECUTOR 后台线程
            ├─ sleep 1000ms
            ├─ 构造 WidgetData(REFRESH_URL)
            └─ refreshWithData(context, data)
                 └─ 遍历所有 Widget ID
                      └─ updateSingleWidget()
                           ├─ RemoteViews 文字/按钮同步更新
                           └─ 异步图片局部更新
```

### 路径 B：App 内模拟刷新

```
AppWidgetDemoActivity → btn_simulate_refresh
  │
  └─ executor.execute (单线程)
       ├─ sleep 1500ms
       ├─ 构造 WidgetData(NEW_IMAGE_URL)
       └─ DreamChatWidgetProvider.refreshWithData(ctx, data)
            └─ 同路径 A 的 updateSingleWidget()
```

---

## 8. MIUI / HyperOS 兼容性

### 问题

MIUI 的桌面（Launcher）是独立进程，默认不接收第三方 App 的 Widget 广播，且 `isRequestPinAppWidgetSupported` 返回 `false`。

### 解决方案

**① Manifest 必须声明 `exported="true"`**

```xml
<receiver android:name=".widget.DreamChatWidgetProvider" android:exported="true">
```

桌面进程需要向此 Receiver 发送 `APPWIDGET_UPDATE` 广播，`exported=false` 会导致小组件点击无响应。

**② 必须提供静态预览图**

```xml
<!-- res/xml/dream_chat_widget_info.xml -->
android:previewImage="@drawable/preview_dream_chat_widget"
```

MIUI 的小组件选择器会过滤掉没有 `previewImage` 的小组件，导致用户根本找不到。

**③ 固定到桌面兜底**

```kotlin
// AppWidgetDemoActivity / AppWidgetDemoActivity.tryLaunchMiuiWidgetPicker()
val candidates = listOf(
    "com.miui.home.launcher.action.APPWIDGET_PICKER",
    "miui.intent.action.ADD_SHORTCUT"
)
// 依次尝试，能 resolveActivity 则启动
```

当 `isRequestPinAppWidgetSupported == false` 时（MIUI 常见），尝试用系统专属 Action 打开小组件选择器。

---

## 9. AndroidManifest 配置

```xml
<!-- 小组件 Receiver：exported 必须为 true -->
<receiver
    android:name=".widget.DreamChatWidgetProvider"
    android:exported="true">
    <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
        <!-- 自定义刷新广播 -->
        <action android:name="com.hm.viewdemo.action.DREAM_WIDGET_REFRESH" />
    </intent-filter>
    <meta-data
        android:name="android.appwidget.provider"
        android:resource="@xml/dream_chat_widget_info" />
</receiver>

<!-- 演示控制页 -->
<activity
    android:name=".activity.AppWidgetDemoActivity"
    android:exported="false" />
```

**Widget 元数据（`dream_chat_widget_info.xml`）关键属性**

| 属性 | 值 | 说明 |
|---|---|---|
| `minWidth` | 280dp | 最小宽度（约 4 列） |
| `minHeight` | 140dp | 最小高度（约 2 行） |
| `targetCellWidth` | 4 | 目标格子列数 |
| `targetCellHeight` | 2 | 目标格子行数 |
| `updatePeriodMillis` | 0 | 禁用系统周期更新（手动控制） |
| `previewImage` | `@drawable/preview_dream_chat_widget` | MIUI 必须 |
| `previewLayout` | `@layout/widget_dream_chat` | Android 12+ 动态预览 |

---

## 10. 关键设计决策

### Q1：为什么不用 Glide 加载图片？

`RemoteViews` 跑在桌面（Launcher）进程，Glide 无法跨进程加载图片到 `RemoteViews` 的 `ImageView`。必须在**本 App 进程**内完成下载，得到 `Bitmap` 对象，再通过 `RemoteViews.setImageViewBitmap()` + `partiallyUpdateAppWidget()` 经 Binder IPC 传递。

### Q2：为什么用 `partiallyUpdateAppWidget()` 而非 `updateAppWidget()`？

| 方法 | 行为 |
|---|---|
| `updateAppWidget()` | 替换整个 RemoteViews，会触发整体重绘，有闪烁感 |
| `partiallyUpdateAppWidget()` | 仅合并指定字段，图片加载完成后单独更新，体验更流畅 |

图片是异步加载的，先用 `updateAppWidget()` 同步更新文字和按钮，再对每张图片用 `partiallyUpdateAppWidget()` 局部刷新，避免"图片还没加载完就把文字也清掉了"的问题。

### Q3：为什么 `companion object` 持有 Executor 和 LruCache？

`AppWidgetProvider` 是 `BroadcastReceiver`，每次广播都会实例化一次，生命周期极短。若将线程池和缓存放在实例字段，每次收到广播都会创建新的线程池，导致资源浪费甚至内存泄漏。放在 `companion object` 中则随类加载器存活，在 App 进程内复用。

### Q4：刷新操作为什么需要 sleep 模拟延迟？

本项目是 Demo 工程，网络请求部分用 `Thread.sleep()` 替代真实 API 调用，以便观察：

- ProgressBar 的显示/隐藏
- 日志的追加过程
- 小组件头像从旧图切换到新图的视觉效果

正式接入后，将 `Thread.sleep()` 替换为实际的网络请求（OkHttp / Retrofit），在回调中调用 `refreshWithData()` 即可。
