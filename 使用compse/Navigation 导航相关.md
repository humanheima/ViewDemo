### Navigation 导航相关

Navigation 组件用于管理应用内的页面跳转和返回栈。传统 View 项目最常见的结构是“单 Activity + 多个 Fragment”：Activity 只提供容器，Navigation 负责决定当前容器显示哪个 Fragment。

本项目中的可运行示例入口是主页面的 **Navigation + Fragment 导航示例**，对应代码如下：

| 文件 | 作用 |
| --- | --- |
| `app/src/main/java/com/hm/viewdemo/navigation/NavigationFragmentExampleActivity.kt` | 设置 Activity 布局，布局中声明 `NavHostFragment` |
| `app/src/main/res/layout/activity_navigation_fragment_example.xml` | `FragmentContainerView` 容器，绑定 `app:navGraph` 和 `app:defaultNavHost` |
| `app/src/main/res/navigation/navigation_fragment_graph.xml` | 声明 destination、action 和参数 |
| `NavigationHomeFragment.kt` | 首页，点击按钮导航到详情页并传参 |
| `NavigationDetailFragment.kt` | 读取参数，继续导航到确认页 |
| `NavigationConfirmationFragment.kt` | 使用 `popBackStack` 返回首页并清理中间页面 |

#### 三个核心对象

- **NavHostFragment**：Fragment 容器。它根据 NavGraph 创建、替换当前 destination。
- **NavGraph**：导航图，描述页面（`fragment`）、页面之间的连接（`action`）和参数（`argument`）。
- **NavController**：导航控制器。Fragment 中通常通过 `findNavController()` 获取，然后调用 `navigate()` 或 `popBackStack()`。

#### 示例的导航流程

```text
NavigationHomeFragment
        | navigate(action, item_id = "android-navigation")
        v
NavigationDetailFragment
        | navigate(action, item_id)
        v
NavigationConfirmationFragment
        | popBackStack(navigationHomeFragment, inclusive = false)
        v
NavigationHomeFragment
```

系统返回键在详情页会回到首页，在确认页会回到详情页，这是 NavController 默认维护的返回栈行为。确认页的“返回首页并清理返回栈”按钮则显式回退到首页，`inclusive = false` 表示保留首页本身。

#### 1. 在 XML 中声明 NavHost

```xml
<androidx.fragment.app.FragmentContainerView
    android:name="androidx.navigation.fragment.NavHostFragment"
    app:defaultNavHost="true"
    app:navGraph="@navigation/navigation_fragment_graph" />
```

这三个属性的作用和必要性不同：

- `android:name`：在这种 XML 写法中是必须的。`FragmentContainerView` 本身只是普通容器，`android:name` 告诉系统创建 `NavHostFragment`。去掉后不会自动创建 NavHost，`app:navGraph` 也无法正常工作。
- `app:navGraph`：指定启动时使用的导航图。本示例指向 `res/navigation/navigation_fragment_graph.xml`。也可以不在 XML 中配置，改为代码调用 `NavHostFragment.create()` 时传入导航图。
- `app:defaultNavHost`：不是 Navigation 正常导航的硬性要求，但建议设置为 `true`。它会让该 NavHost 成为 Activity 的默认返回处理者，使系统返回键自动交给 NavController。一个 Activity 可以有多个 NavHost，但通常只设置一个默认 NavHost。

如果不使用 `android:name`，也可以在代码中手动创建 NavHost。此时需要自己把它放入容器，并设置为主导航 Fragment：

```kotlin
val navHost = NavHostFragment.create(
    R.navigation.navigation_fragment_graph
)

supportFragmentManager.beginTransaction()
    .replace(R.id.navigation_host, navHost)
    .setPrimaryNavigationFragment(navHost)
    .commit()
```

因此，`android:name` 不是 Navigation 组件本身绝对不可缺少的属性，而是当前“通过 XML 自动创建 NavHostFragment”方案中的必要属性。使用代码创建方案时，可以移除它，但必须补上对应的 Fragment 创建和事务提交逻辑。

#### 2. 在 navigation graph 中声明目的地和 action

```xml
<fragment
    android:id="@+id/navigationHomeFragment"
    android:name="com.hm.viewdemo.navigation.NavigationHomeFragment">
    <action
        android:id="@+id/action_navigationHomeFragment_to_navigationDetailFragment"
        app:destination="@id/navigationDetailFragment" />
</fragment>
```

`action` 的目的地必须是 graph 中已声明的 destination。使用 action id 导航比直接写 Fragment class 更容易集中管理页面关系，也能在 XML 中配置动画、返回栈策略等属性。

#### 3. Fragment 中导航和传参

本示例没有启用 Safe Args Gradle 插件，因此使用 `Bundle` 传递参数：

```kotlin
val args = bundleOf(NavigationRoutes.ITEM_ID to "android-navigation")
findNavController().navigate(
    R.id.action_navigationHomeFragment_to_navigationDetailFragment,
    args
)
```

接收方从 `requireArguments()` 读取参数：

```kotlin
val itemId = requireArguments()
    .getString(NavigationRoutes.ITEM_ID)
    .orEmpty()
```

参数名必须和 graph 中 `<argument android:name="item_id" ... />` 完全一致。生产项目建议启用 Safe Args，让参数类型和路由代码由插件生成，减少字符串 key 和类型转换错误；本示例保留 Bundle 写法，是为了展示 Navigation 最小依赖和运行机制。

#### 4. 返回栈操作

```kotlin
// 返回上一个 destination
findNavController().popBackStack()

// 返回指定 destination，并保留指定 destination
findNavController().popBackStack(
    R.id.navigationHomeFragment,
    inclusive = false
)
```

`inclusive = true` 会连指定 destination 一起移除，使用前需要确认用户是否还能回到该页面。登录成功、提交完成、向导结束等流程通常适合使用“回退到指定页面并清理中间页面”。

#### 5. 常见注意事项

1. `NavHostFragment` 使用的 `navGraph` 必须存在于 `res/navigation`，且 graph 中的 Fragment class 必须是可实例化的 public 类。
2. Fragment 的 View 可能被销毁后重建，不要把 View 引用保存到 Fragment 长生命周期对象中；示例只在 `onViewCreated` 中绑定点击事件。
3. 不要在同一次点击中连续调用多个 `navigate()`；快速重复点击可能产生重复 destination。需要时可以在 action 上使用 `launchSingleTop`，或在点击层增加防抖。
4. 如果 Fragment 内嵌套子 NavHost，要使用正确的 NavController；`findNavController()` 获取的是当前 Fragment 所属的 NavHost。
5. Navigation 只负责页面切换和返回栈，不负责业务数据持久化。跨页面共享数据应使用 Activity 级 ViewModel、SavedStateHandle 或仓库层。
