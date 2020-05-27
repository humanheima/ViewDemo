1. 系统SDK版本大于等于26，直接使用TextView就可以。

2. 系统SDK版本小于26，需要使用support包，support包的版本要大于等于`26.0`。support包支持Android 4.0 (API level 14)及以上版本。
 2.1 如果Activity继承自AppCompatActivity，直接使用TextView就可以。
 2.2 否则需要使用AppCompatTextView。

## 设置TextView自动调整字体大小

有三种方式可以设置TextView支持自动调整字体大小。

* 默认设置
* 控制调整范围
* 预设大小

注意：如果在XML文件中设置自动调整字体大小，不建议将TextView的宽高设置为`wrap_content`，不然可能会有意想不到的问题。

## 默认设置

默认设置允许TextView在水平和垂直轴上均匀调整字体大小。

### 在代码中使用

根据SDK版本调用不同的方法
```java
//SDK版本大于等于26
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    tvDynamicSet.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
} else {
    TextViewCompat.setAutoSizeTextTypeWithDefaults(tvDynamicSet,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
    )
}
```

注意：默认情况下缩放的最小字体是12sp，最大的字体是112sp，每次增加或减少的粒度是1px。

#### 在xml文件中使用

如果系统SDK版本大于等于26，使用`android`命名空间并设置autoSizeTextType属性。该属性取值也有两个`none`和`uniform`，对应代码中的两个值。

```
<TextView
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:autoSizeTextType="uniform" />

```
如果系统SDK版本小于26，因为我们的Activity是继承自AppCompatActivity的，所以我们可以直接使用TextView，但是注意要使用`app`命名空间。

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

  <TextView
      android:layout_width="match_parent"
      android:layout_height="200dp"
      app:autoSizeTextType="uniform" />

</LinearLayout>
```

## 控制调整范围

你可以定义字体大小调整的一个范围并指定一个渐变值表示每次增加或减少的值。

### 在代码中使用

```
//sdk版本大于等于26
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    tvDynamicSet.setAutoSizeTextTypeUniformWithConfiguration(16, 40, 1, TypedValue.COMPLEX_UNIT_SP
    )
} else {
    TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                    tvDynamicSet, 16, 40, 1, TypedValue.COMPLEX_UNIT_SP
   )
}
```

以TextView的setAutoSizeTextTypeUniformWithConfiguration方法为例，3个参数的含义如下：

```
/**
 *
 * @param autoSizeMinTextSize 最小字体
 * @param autoSizeMaxTextSize 最大字体
 * @param autoSizeStepGranularity 渐变值
 * @param unit the 尺寸单位 px，sp，dp
 */
```

### 在xml中使用

如果系统SDK版本大于等于26，使用`android`命名空间并设置autoSizeTextType属性为`uniform`。然后设置autoSizeMinTextSize，autoSizeMaxTextSize和autoSizeStepGranularity这个三个属性。

```
<TextView
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:autoSizeTextType="uniform"
    android:autoSizeMinTextSize="12sp"
    android:autoSizeMaxTextSize="100sp"
    android:autoSizeStepGranularity="2sp" />
```

如果系统SDK版本小于26，使用`app`命名空间并设置autoSizeTextType属性为`uniform`。然后设置autoSizeMinTextSize，autoSizeMaxTextSize和autoSizeStepGranularity这个三个属性。

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

  <TextView
      android:layout_width="match_parent"
      android:layout_height="200dp"
      app:autoSizeTextType="uniform"
      app:autoSizeMinTextSize="12sp"
      app:autoSizeMaxTextSize="100sp"
      app:autoSizeStepGranularity="2sp" />

</LinearLayout>

```

## 预设大小

你可以指定TextView在自动调整字体大小的时候所有可取的值。

#### 在代码中使用

```
//获取预设的字体大小数字
val intArray = resources.getIntArray(R.array.autosize_text_sizes)
//sdk版本大于等于26
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    tvDynamicSet.setAutoSizeTextTypeUniformWithPresetSizes(intArray, TypedValue.COMPLEX_UNIT_SP
    )
} else {
    TextViewCompat.setAutoSizeTextTypeUniformWithPresetSizes(
            tvDynamicSet, intArray, TypedValue.COMPLEX_UNIT_SP
    )
}
```
### 在xml中使用

如果系统SDK版本大于等于26，使用`android`命名空间。设置autoSizeTextType属性和autoSizePresetSizes属性。

```
<TextView
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:autoSizeTextType="uniform"
    android:autoSizePresetSizes="@array/autosize_text_sizes" />

```

在`res/values/arrays.xml`文件中定义需要的数组。

```
<resources>
  <array name="autosize_text_sizes">
    <item>10sp</item>
    <item>12sp</item>
    <item>20sp</item>
    <item>40sp</item>
    <item>100sp</item>
  </array>
</resources>

```

如果系统SDK版本小于26，使用`app`命名空间并设置autoSizeTextType属性和autoSizePresetSizes属性。

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

  <TextView
      android:layout_width="match_parent"
      android:layout_height="200dp"
      app:autoSizeTextType="uniform"
      app:autoSizePresetSizes="@array/autosize_text_sizes" />
</LinearLayout>
```

### PS：监听用户是否调整了字体大小

可以在Application的重写onConfigurationChanged方法，然后看fontScale是否改变。标准值是1f。大于1f表示用户调大了字体。

```
@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Logger.d(TAG, "fontScale = " + newConfig.fontScale);
}
```

参考链接：

* [Autosizing TextViews](https://developer.android.com/guide/topics/ui/look-and-feel/autosizing-textview)
* [聊聊 Android 中的字体大小适配](https://www.jianshu.com/p/2fdc97ae74a8)








