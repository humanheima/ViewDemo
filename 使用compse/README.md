
### 可以在 View 中使用 Compose ，也可以在 Compose 中使用 View

* [Interoperability API](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis?hl=zh-cn)

### 在Compose 组件中如何获取 Context

```kotlin

@Composable
fun ChipPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试Compose Card", onBackClick) }
    ) { padding ->

        //获取Context
        val context = LocalContext.current

        Column(modifier = modifier.padding(padding)) {

            InputChipExample(text = "Input chip") {
                //使用Context
                Log.d("Input chip", "hello world context = $context")
                Toast.makeText(context, "Hello world", Toast.LENGTH_SHORT).show()
            }

        }
    }
}


```

### 在 View 中使用 Compose

* [在 View 中使用 Compose](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/compose-in-views?hl=zh-cn)

### 在 Compose 中使用 View

* [在 Compose 中使用 View](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/views-in-compose?hl=zh-cn)

### Compose 获取系统资源

* [Resources in Compose](https://developer.android.com/develop/ui/compose/resources)