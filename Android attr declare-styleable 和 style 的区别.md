### Android attr declare-style style theme

### attr 

定义的属性

### declare-styleable

将自定义属性聚合起来，方便使用。在自定义View的时候，方便获取自定义的属性。

### style 

应用于指定的View，统一指定View的属性值集合。比如定义一个Style指定TextView的颜色，和字体大小等等。


> A style is a collection of attributes that specifies the appearance for a single View. A style can specify attributes such as font color, font size, background color, and much more.

style 应用于指定的View，例如：

```xml
<TextView
    style="@style/GreenText"
    ... />
```

### theme

> A theme is a collection of attributes that's applied to an entire app, activity, or view hierarchy—not just an individual view. When you apply a theme, every view in the app or activity applies each of the theme's attributes that it supports. Themes can also apply styles to non-view elements, such as the status bar and window background.

theme 应用于 AndroidManifest.xml 文件中的 `application` 标记或 `activity`，例如：

<!--应用于Application-->

```xml
<manifest ... >
    <application android:theme="@style/Theme.AppCompat" ... >
    </application>
</manifest>

```

<!--应用于Activity-->

```xml
<manifest ... >
    <application ... >
        <activity android:theme="@style/Theme.AppCompat.Light" ... >
        </activity>
    </application>
</manifest>
```


Styles and themes are declared in a style resource file in `res/values/`, usually named `styles.xml`.

参考链接：
* [](https://developer.android.com/guide/topics/ui/look-and-feel/themes?hl=zh-cn)