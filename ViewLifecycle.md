### View的生命周期

在布局文件中加入一个自定义View测试

android:visibility="visible"
```
2019-06-14 15:38:28.211 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: TestLifecycleTextView: 
2019-06-14 15:38:28.213 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onFinishInflate: 
2019-06-14 15:38:28.246 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onAttachedToWindow: 
2019-06-14 15:38:28.246 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onWindowVisibilityChanged: 0
2019-06-14 15:38:28.246 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onVisibilityChanged: 0
2019-06-14 15:38:28.254 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onMeasure: 
2019-06-14 15:38:28.279 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onMeasure: 
2019-06-14 15:38:28.284 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onSizeChanged: 
2019-06-14 15:38:28.285 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onLayout: 
2019-06-14 15:38:28.289 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onDraw: 
2019-06-14 15:38:28.307 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onWindowFocusChanged: true
2019-06-14 15:39:04.011 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onWindowFocusChanged: false
2019-06-14 15:39:04.148 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onWindowVisibilityChanged: 8
2019-06-14 15:39:04.440 4694-4694/com.hm.viewdemo D/TestLifecycleTextView: onDetachedFromWindow: 

```
android:visibility="invisible"
```
2019-06-14 15:43:19.451 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: TestLifecycleTextView: 
2019-06-14 15:43:19.453 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onFinishInflate: 
2019-06-14 15:43:19.480 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onAttachedToWindow: 
2019-06-14 15:43:19.481 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onWindowVisibilityChanged: 0
2019-06-14 15:43:19.481 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onVisibilityChanged: 4
2019-06-14 15:43:19.488 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onMeasure: 
2019-06-14 15:43:19.511 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onMeasure: 
2019-06-14 15:43:19.516 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onSizeChanged: 
2019-06-14 15:43:19.517 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onLayout: 
2019-06-14 15:43:19.552 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onWindowFocusChanged: true
2019-06-14 15:43:58.513 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onWindowFocusChanged: false
2019-06-14 15:43:58.637 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onWindowVisibilityChanged: 8
2019-06-14 15:43:58.945 5225-5225/com.hm.viewdemo D/TestLifecycleTextView: onDetachedFromWindow: 

```
android:visibility="gone"

```
2019-06-14 15:45:43.688 5454-5454/com.hm.viewdemo D/TestLifecycleTextView: TestLifecycleTextView: 
2019-06-14 15:45:43.691 5454-5454/com.hm.viewdemo D/TestLifecycleTextView: onFinishInflate: 
2019-06-14 15:45:43.722 5454-5454/com.hm.viewdemo D/TestLifecycleTextView: onAttachedToWindow: 
2019-06-14 15:45:43.722 5454-5454/com.hm.viewdemo D/TestLifecycleTextView: onWindowVisibilityChanged: 0
2019-06-14 15:45:43.722 5454-5454/com.hm.viewdemo D/TestLifecycleTextView: onVisibilityChanged: 8
2019-06-14 15:45:43.777 5454-5454/com.hm.viewdemo D/TestLifecycleTextView: onWindowFocusChanged: true
2019-06-14 15:46:10.994 5454-5454/com.hm.viewdemo D/TestLifecycleTextView: onWindowFocusChanged: false
2019-06-14 15:46:11.131 5454-5454/com.hm.viewdemo D/TestLifecycleTextView: onWindowVisibilityChanged: 8
2019-06-14 15:46:11.438 5454-5454/com.hm.viewdemo D/TestLifecycleTextView: onDetachedFromWindow: 


```