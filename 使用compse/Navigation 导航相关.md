### 导航

Navigation 组件是为具有一个主 activity 和多个 fragment 目的地的应用设计的。
主Activity和一个导航图关联。 并且包含一个NavHostFragment，该 NavHostFragment 负责根据需要交换目的地。
在具有多个 activity 的 app中，每个 activity 都有它自己的导航图。

简单一句话就是：Navigation 组件适用于 单activity + 多fragment的app。是用来在一个 activity 内切换 fragment 用的。



Navigation 的 3 个主要部分是 NavController、NavGraph 和 NavHost。
NavController 始终与一个 NavHost 可组合项相关联。
NavHost 充当容器，负责显示导航图的当前目的地。当您在可组合项之间进行导航时，NavHost 的内容会自动进行重组。
此外，它还会将 NavController 与导航图 (NavGraph) 相关联，
NavGraph用于标出能够在其间进行导航的可组合目的地。它本质上是一组可到达的目的地。


NavController

NavHost 

NavGraph