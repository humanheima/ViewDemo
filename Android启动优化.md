
adb shell am start -S -W com.jingyao.easybike/com.hellobike.atlas.business.splash.SplashActivity

```
Stopping: com.jingyao.easybike
Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.jingyao.easybike/com.hellobike.atlas.business.splash.SplashActivity }
Status: ok
Activity: com.jingyao.easybike/com.hellobike.atlas.business.portal.PortalActivity
ThisTime: 2224
TotalTime: 6388
WaitTime: 6441
Complete

```

---

ViewDemo 中的 `App Startup` 冷启动优化演示：

1. 在 `AndroidManifest.xml` 注册 `CriticalServicesInitializer`，仅保留冷启动必需初始化为 eager（并通过 `dependencies()` 串联多个关键初始化）。
2. `DeferredAnalyticsInitializer` 不注册到 manifest，改为首帧后通过 `AppInitializer.initializeComponent()` 手动触发。
3. 在 `MainActivity.initData()` 中调用 `StartupDemo.triggerDeferredInit(this)`，把非关键初始化移出冷启动关键路径。

关键代码位置：

- `app/src/main/java/com/hm/viewdemo/startup/CriticalServicesInitializer.kt`
- `app/src/main/java/com/hm/viewdemo/startup/DeferredAnalyticsInitializer.kt`
- `app/src/main/java/com/hm/viewdemo/startup/StartupDemo.kt`
- `app/src/main/java/com/hm/viewdemo/startup/StartupTrace.kt`

观察日志（`tag = StartupDemo`）可看到：

- `CriticalServicesInitializer.create` 在应用早期执行。
- `First frame rendered` 之后才出现 `Deferred init start` 和 `DeferredAnalyticsInitializer.create`。
