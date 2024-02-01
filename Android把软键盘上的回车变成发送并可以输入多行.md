参考链接：
[Android EditText 实现多行输入，且将回车键显示成“发送”](https://blog.csdn.net/u014133383/article/details/131962165?spm=1001.2014.3001.5502)


1. 布局文件，设置EditText的imeOptions属性为actionSend，inputType属性为text
```xml
<EditText
            android:id="@+id/et_test_send"
            android:layout_margin="40dp"
            android:layout_width="match_parent"
            android:hint="测试把软键盘的回车变成发送按钮"
            android:inputType="text"
            android:imeOptions="actionSend"
            android:layout_height="wrap_content" />
```

2. 代码里设置禁止EditText横向滚动，设置显示最大的行数。
```kotlin
   et_test_send.setHorizontallyScrolling(false)
 et_test_send.maxLines = 5

```
注意，在 xml 里面设置 android:maxLines="5" 无效，必须在代码里设置。

3. 设置监听
```kotlin
 et_test_send.setOnEditorActionListener(object :
            android.widget.TextView.OnEditorActionListener {
            override fun onEditorAction(
                v: android.widget.TextView?,
                actionId: Int,
                event: android.view.KeyEvent?
            ): Boolean {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                    android.widget.Toast.makeText(
                        this@TestSoftKeyboardActivity,
                        "发送",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    return true
                }
                return false
            }
        })
```

