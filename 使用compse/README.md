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
