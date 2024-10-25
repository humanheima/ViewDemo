# Paint 设置 shader 的相关




1. Paint 必须得设置颜色。Paint 默认颜色是黑色。如果是一个透明的颜色，Shader 不起作用。自定义View的时候，好像是这样的。 TextView没有问题。

```kotlin
val paint = Paint()
paint.color = Color.RED
val shader = LinearGradient(0f, 0f, 0f, 100f, Color.RED, Color.BLUE, Shader.TileMode.CLAMP)
paint.shader = shader
```