点击改变了itemList，如何同时更改原始数据 items: List<Item> 的选中状态。

要同时更改原始数据 `items: List<Item>` 的选中状态，需要确保 `items` 是可变的（例如使用 `MutableList<Item>`），或者通过回调将更新反馈到原始数据源。以下是两种常见方案的实现，假设 `items` 是外部传入的 `List<Item>`，且你希望同步更新其 `selected` 状态。

### 方案 1：原始数据为 `MutableList<Item>`
如果 `items` 是 `MutableList<Item>`，可以直接修改其元素。以下是实现：

```kotlin
@Composable
fun ItemListScreen(items: MutableList<Item>) {
    // 使用 mutableStateListOf 跟踪 UI 状态
    val itemList = remember { mutableStateListOf<Item>().apply { addAll(items) } }

    LazyColumn {
        items(itemList) { item ->
            ItemRow(
                item = item,
                onItemClick = {
                    // 更新 UI 状态
                    val index = itemList.indexOf(item)
                    itemList[index] = item.copy(selected = !item.selected)
                    // 同步更新原始数据
                    items[index] = items[index].copy(selected = !items[index].selected)
                }
            )
        }
    }
}

@Composable
fun ItemRow(item: Item, onItemClick: () -> Unit) {
    val backgroundColor = if (item.selected) Color.LightGray else Color.White
    val textStyle = if (item.selected) TextStyle(fontWeight = FontWeight.Bold) else TextStyle.Default

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onItemClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            style = textStyle
        )
    }
}
```

#### 使用示例
```kotlin
val sampleItems = mutableListOf(
    Item(1, "Item 1", false),
    Item(2, "Item 2", true),
    Item(3, "Item 3", false)
)

@Composable
fun MyApp() {
    ItemListScreen(items = sampleItems)
}
```

#### 说明
- **同步更新**：点击时，`items[index]` 的 `selected` 状态与 `itemList` 同步更新。
- **注意**：确保 `items` 是 `MutableList<Item>`，否则无法直接修改其元素。

### 方案 2：原始数据为不可变 `List<Item>`，通过回调更新
如果 `items` 是不可变的 `List<Item>`，可以通过回调函数将更新通知给上层，由上层负责更新数据源。这种方式更符合 Compose 的单向数据流。

```kotlin
@Composable
fun ItemListScreen(
    items: List<Item>,
    onItemSelected: (Int, Boolean) -> Unit // 回调：传递 item 的 id 和新的 selected 状态
) {
    // 使用 mutableStateListOf 跟踪 UI 状态
    val itemList = remember { mutableStateListOf<Item>().apply { addAll(items) } }

    LazyColumn {
        items(itemList) { item ->
            ItemRow(
                item = item,
                onItemClick = {
                    // 更新 UI 状态
                    val index = itemList.indexOf(item)
                    itemList[index] = item.copy(selected = !item.selected)
                    // 通知上层更新原始数据
                    onItemSelected(item.id, itemList[index].selected)
                }
            )
        }
    }
}

@Composable
fun ItemRow(item: Item, onItemClick: () -> Unit) {
    val backgroundColor = if (item.selected) Color.LightGray else Color.White
    val textStyle = if (item.selected) TextStyle(fontWeight = FontWeight.Bold) else TextStyle.Default

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onItemClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            style = textStyle
        )
    }
}
```

#### 使用示例（结合 ViewModel）
```kotlin
class ItemViewModel : ViewModel() {
    private val _items = mutableStateListOf(
        Item(1, "Item 1", false),
        Item(2, "Item 2", true),
        Item(3, "Item 3", false)
    )
    val items: List<Item> get() = _items

    fun updateItemSelected(id: Int, selected: Boolean) {
        val index = _items.indexOfFirst { it.id == id }
        if (index != -1) {
            _items[index] = _items[index].copy(selected = selected)
        }
    }
}

@Composable
fun MyApp(viewModel: ItemViewModel = viewModel()) {
    ItemListScreen(
        items = viewModel.items,
        onItemSelected = { id, selected ->
            viewModel.updateItemSelected(id, selected)
        }
    )
}
```

#### 说明
- **回调机制**：`onItemSelected` 将 `item.id` 和新的 `selected` 状态传递给上层，`ViewModel` 负责更新原始数据。
- **单向数据流**：UI 只反映状态，数据更新由 `ViewModel` 管理，适合复杂场景。
- **初始化同步**：`itemList` 从 `items` 初始化，确保 UI 和数据一致。如果 `items` 在上层更新，需确保 `itemList` 同步（例如通过 `remember(items) { ... }`）。

### 注意事项
- **方案选择**：
    - 如果 `items` 可变且由当前 Composable 管理，使用方案 1。
    - 如果 `items` 由外部（如 `ViewModel` 或数据库）管理，使用方案 2。
- **性能**：避免频繁复制大列表。如果列表很大，考虑只更新受影响的元素。
- **一致性**：确保 `itemList` 和 `items` 的顺序一致（通过 `id` 匹配更安全）。

### 扩展
如果需要持久化 `selected` 状态（例如保存到数据库），可以在 `ViewModel` 的 `updateItemSelected` 中添加数据库操作。需要具体示例或更复杂场景的实现吗？