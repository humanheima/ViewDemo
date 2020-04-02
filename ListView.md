万万没想到，有一天还得回来使用ListView

### 使用android:entries 为ListView准备数据

```
 <ListView
        android:id="@+id/listView"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:entries="@array/citys"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```
这样就可以了，一行代码都不用写，也是美滋滋。

