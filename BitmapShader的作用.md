# BitmapShader 的作用

`BitmapShader` 是 Android
提供的一个类，用于在绘制图形时将位图（Bitmap）作为着色器（Shader）应用到画布上。它可以将一张图片的像素映射到绘制区域，常用于实现复杂的图形效果，比如纹理填充、圆形图片裁剪等。下面我详细介绍一下 `BitmapShader`
的用法。

### 基本用法

`BitmapShader` 的构造函数需要三个参数：

1. **Bitmap**: 要用作着色器的位图。
2. **TileMode (X 方向)**: 当绘制区域超出位图大小时，X 方向的平铺模式。
3. **TileMode (Y 方向)**: Y 方向的平铺模式。

平铺模式（`TileMode`）有以下选项：

- `CLAMP`: 在边界外重复边缘颜色。
- `REPEAT`: 在边界外重复整个位图。
- `MIRROR`: 在边界外镜像重复位图。

### 示例代码

以下是一个简单的例子，展示如何使用 `BitmapShader` 在 Canvas 上绘制一个圆形区域，并用位图填充：

```java
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

public class CustomView extends View {
    private Paint paint;
    private Bitmap bitmap;

    public CustomView(Context context) {
        super(context);
        init();
    }

    private void init() {
        // 加载位图
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_image);

        // 创建 BitmapShader，设置平铺模式
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        // 初始化 Paint 并设置 Shader
        paint = new Paint();
        paint.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 使用画笔绘制一个圆，圆内会填充 BitmapShader 的内容
        canvas.drawCircle(200, 200, 150, paint);
    }
}
```

### 代码解析

1. **`BitmapFactory.decodeResource`**: 从资源文件中加载位图（例如 `R.drawable.sample_image`）。
2. **`BitmapShader` 创建**: 将位图传入，并指定平铺模式为 `REPEAT`，表示超出位图范围时会重复绘制。
3. **`Paint.setShader`**: 将 `BitmapShader` 设置到 `Paint` 对象上。
4. **`Canvas.drawCircle`**: 使用带有 Shader 的 Paint 绘制圆形，圆内会显示位图的纹理。

### 常见应用场景

1. **圆形头像**:
    - 将用户的头像图片设置为 `BitmapShader`，然后绘制一个圆形区域，实现圆形裁剪效果。
    - 示例：
      ```java
      BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
      paint.setShader(shader);
      canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
      ```

2. **纹理背景**:
    - 使用 `REPEAT` 模式将小块纹理图片填充到大区域，形成无缝背景。

3. **自定义图形填充**:
    - 可以结合 `drawRect`、`drawPath` 等方法，将位图填充到任意形状中。

### 注意事项

- **性能**: 如果位图很大，频繁使用可能会影响性能，建议根据需要调整位图大小。
- **缩放**: `BitmapShader`
  本身不会缩放位图。如果需要调整显示大小，可以在创建位图时用 `Bitmap.createScaledBitmap` 预处理。
- **边界处理**: 根据实际需求选择合适的 `TileMode(平铺模式)`，以避免意外的填充效果。


* tile 这里是平铺的意思
  英 / taɪl /
  美 / taɪl /
  n. （贴墙或铺地用的）瓷砖，地砖；（铺屋顶的）瓦，瓦片；（拼字游戏、麻将等牌戏中的）（一张）牌；（棋盘游戏的）棋子；（数）平铺
  v. 铺瓦，铺砖；（计算机）平铺（窗口）

# 子类 ComposeShader

```
/**
* ComposeShader 是 Shader 的一个子类，它返回由两个其他着色器组合而成的结果，
* 组合方式由一个 android.graphics.Xfermode 子类决定。
  */
 ```

如果你有具体的应用场景或代码问题，可以告诉我，我会进一步帮你优化或调试！