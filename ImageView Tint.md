在布局文件中使用

指定`android:tint`属性
```
<ImageView
    android:id="@+id/ivPng"
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:layout_gravity="center_horizontal"
    android:layout_marginTop="10dp"
    android:src="@drawable/main_ic_back_white"
    android:tint="@color/colorAccent" />
```

注意：图片要使用`android:src`指定，不能使用`android:background`。

在代码中使用

```
val color = ContextCompat.getColor(this, R.color.colorPrimary)
ivPng.setColorFilter(color)
```

