### Activity进入和退出动画

测试Activity  `ActivityAnimTestActivity.kt`

注意：

进入和退出动画的时间设置要一样，不然会有黑屏效果。

例如：如果打开从一个ActivityA打开一个另外一个ActivityB，如果进入界面动画`alpha_in.xml`慢(2500毫秒)， 退出界面动画快`alpha_out.xml`(200毫秒)，进入动画还没有执行完，退出动画已执行完,ActivityA会变成黑屏。

alpha_in.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
        android:duration="2500"
        android:fromAlpha="0.0"
        android:toAlpha="1.0"/>
```
alpha_out.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
        android:duration="200"
        android:fromAlpha="1.0"
        android:toAlpha="0.0" />
```

参考链接

* [Android动画之Activity切换动画overridePendingTransition实现和Theme Xml方式实现](https://blog.csdn.net/u010126792/article/details/85766747)
