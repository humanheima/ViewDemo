### LinearGradient

LinearGradient 比如斜着渐变的时候，如果宽高比例不是 1:1，会出现可能渐变不好使的问题。

这个文字换行，看起来效果还可以

```xml

<com.hm.viewdemo.widget.GradientTextView android:id="@+id/tv_gradient_5"
    android:layout_width="wrap_content" android:layout_height="wrap_content"
    android:gravity="center"
    android:text="从左上到右下渐变\n从左上到右下渐变\n从左上到右下渐变\n从左上到右下渐变\n从左上到右下渐变\n从左上到右下渐变\n从左上到右下渐变"
    android:textSize="20sp" />

```

如果文字不换行，看起来就有问题。看起来像是从做到右变化的。比如从左上角到右下角渐变。

```xml

<com.hm.viewdemo.widget.GradientTextView android:id="@+id/tv_gradient_5"
    android:layout_width="wrap_content" android:layout_height="wrap_content"
    android:gravity="center"
    android:text="从左上到右下渐变从左上到右下渐变从左上到右下渐变从左上到右下渐变从左上到右下渐变从左上到右下渐变从左上到右下渐变"
    android:textSize="20sp" />

```

有一个想法，比如从左上角到右下角渐变的时候，起点是[0,0]，终点是，可以取控件宽和高之间小的那个值。 end =
min(width, height) ，终点就是[end,end]

