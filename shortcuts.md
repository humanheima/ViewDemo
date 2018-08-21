## App Shortcuts
###  使用静态 shortcuts

1. 在`AndroidManifest.xml`文件中给应用的`MainActivity`添加一个` <meta-data>`元素，这个` <meta-data>`指向了一个定义了`shortcuts`的资源文件

```html
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.example.myapplication">
  <application ... >
    <activity android:name="Main">
      <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
      <meta-data android:name="android.app.shortcuts"
                 android:resource="@xml/shortcuts" />
    </activity>
  </application>
</manifest>
    
```

2. 新建一个资源文件`res/xml/shortcuts.xml`，这个资源文件中，根元素是一个`<shortcuts>`，然后包含一系列的`<shortcut>`元素，每个`<shortcut>`元素就包含一个静态`shortcuts`的信息
```html
<?xml version="1.0" encoding="utf-8"?>
<shortcuts xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <shortcut
        android:enabled="true"
        android:icon="@drawable/scan_qrcode"
        android:shortcutDisabledMessage="@string/can_not_use"
        android:shortcutId="scanQRcode"
        android:shortcutLongLabel="@string/long_label"
        android:shortcutShortLabel="@string/short_label"
        tools:ignore="UnusedAttribute">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.hm.viewdemo.activity.MainActivity"
            android:targetPackage="com.hm.viewdemo" />
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.hm.viewdemo.activity.QrCodeScanActivity"
            android:targetPackage="com.hm.viewdemo" />
        <!-- 如果你的shortcut关联多个intents, 用户启动shortcuts的时候，会启动最后一个intent-->
        <categories android:name="android.shortcut.conversation" />
    </shortcut>
</shortcuts>

```
在上面的例子中，当用户点击快捷键的时候，会启动`QrCodeScanActivity`，然后在`QrCodeScanActivity`点击返回键的时候，会回到`MainActivity`

包含两个shortcuts
```html
<?xml version="1.0" encoding="utf-8"?>
<shortcuts xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <shortcut
        android:enabled="true"
        android:icon="@drawable/scan_qrcode"
        android:shortcutDisabledMessage="@string/can_not_use"
        android:shortcutId="scanQRcode"
        android:shortcutLongLabel="@string/long_label"
        android:shortcutShortLabel="@string/short_label"
        tools:ignore="UnusedAttribute">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.hm.viewdemo.activity.MainActivity"
            android:targetPackage="com.hm.viewdemo" />
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.hm.viewdemo.activity.QrCodeScanActivity"
            android:targetPackage="com.hm.viewdemo" />
        <categories android:name="android.shortcut.conversation" />
    </shortcut>
    <shortcut
        android:enabled="true"
        android:icon="@drawable/ic_star"
        android:shortcutDisabledMessage="@string/can_not_use"
        android:shortcutId="star"
        android:shortcutLongLabel="@string/i_am_a_star"
        android:shortcutShortLabel="@string/a_star"
        tools:ignore="UnusedAttribute">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.hm.viewdemo.activity.QrCodeScanActivity"
            android:targetPackage="com.hm.viewdemo" />
    </shortcut>
</shortcuts>

```

### 使用动态shortcuts
```java
 /**
     * 动态添加快捷方式
     */
    @SuppressLint("NewApi")
    private fun setShortcuts() {
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java!!)
        val shortcut = ShortcutInfo.Builder(this, "firstShortcut")
                .setShortLabel("first shortcut")
                .setLongLabel("long label of first shortcut")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_star))
                .setIntent(Intent("com.hm.viewdemo.QrCodeScan"))
                .build()
        shortcutManager.dynamicShortcuts = Arrays.asList(shortcut)

    }
```

### 使用固定的快捷方式
1. 添加一个新的固定的快捷方式
```kotlin
 /**
     * 添加一个固定的快捷方式
     */
    @SuppressLint("NewApi")
    private fun setPinnedShortcuts() {
        val shortcutManager: ShortcutManager = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
        val secondPinShortcutInfo = ShortcutInfo.Builder(this, "secondShortcut")
                .setShortLabel("second shortcut")
                .setLongLabel("long label of second shortcut")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_gear))
                .setIntent(Intent("com.hm.viewdemo.QrCodeScan"))
                .build()
        if (shortcutManager.isRequestPinShortcutSupported) {
            val pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(secondPinShortcutInfo)
            val successCallback: PendingIntent = PendingIntent.getBroadcast(this, 0,
                    pinnedShortcutCallbackIntent, 0)
            
            shortcutManager.requestPinShortcut(secondPinShortcutInfo, successCallback.intentSender)
        }
    }
```
2. 把一个已存在的快捷方式变成固定的
```kotlin
 /**
     * 添加一个固定的快捷方式
     */
    @SuppressLint("NewApi")
    private fun setPinnedShortcuts() {
        val shortcutManager: ShortcutManager = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
        val pinShortcutInfo: ShortcutInfo = ShortcutInfo.Builder(this, "firstShortcut").build()
        if (shortcutManager.isRequestPinShortcutSupported) {
            val pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo)
            val successCallback: PendingIntent = PendingIntent.getBroadcast(this, 0,
                    pinnedShortcutCallbackIntent, 0)
            
            shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.intentSender)
        }
    }
```
```kotlin
 /**
     * 禁用一个快捷方式，不能禁用一个静态的快捷方式,因为静态的快捷方式，只能通过改变apk的版本来修改
     */
    @SuppressLint("NewApi")
    private fun disableShortcuts() {
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java!!)
        shortcutManager.disableShortcuts(Arrays.asList("firstShortcut"))
    }
```
https://www.jianshu.com/p/ae92476f4183
https://developer.android.com/guide/topics/ui/shortcuts
