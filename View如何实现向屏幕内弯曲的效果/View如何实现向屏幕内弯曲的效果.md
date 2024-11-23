# View如何实现向屏幕内弯曲的效果?

## 1. 通过设置View的 rotationY ，来实现弯曲的效果

但是这种效果，是一整个平面向内弯曲，可以看 TestRotateYActivity 。

## 如何实现，像是一个圆柱体表面向内弯曲的效果呢？

先参考，还在研究。

对应类 CurveImageViewOrigin， [示意图](示意图.jpg)


### 计算半径 r

角度 mAngle ：转化成弧度  mAngle * Math.PI / 180

图片一半的宽度： hf =  mBitmap.getWidth() / 2;

假定圆角半径为：r

一半的弧度 θ = mAngle / 2 * Math.PI / 180

sin(θ) = hf / r

计算出来：r = hf / sin(θ) =  mBitmap.getWidth() / 2 / Math.sin(mAngle / 2 * Math.PI / 180)

### 计算在 z 轴上的坐标


```java
//水平距离的绝对值，距离bitmap中心的距离是 d
//bw 是 bitmap 的宽度
float d = Math.abs(fx - bw / 2);

//此时 sin(θ) = d / r

//弧度 θ = asin(d / r)
double asin = Math.asin(d / r);

//邻边的长度 = r * Math.cos(asin)

//在 z 轴上的坐标 = r * (1 - Math.cos(asin))
double offsetZ = r * (1 - Math.cos(asin));

```


### 搞明白了 CurveImageView 到底是怎么弯曲的。

```java

/**
 * 这种移动方式，是让每一个点都以中心点进行变换。这种方式是对的。
 */
//m3DMatrix.preTranslate(-bw / 2, -bh / 2);
//m3DMatrix.postTranslate(bw / 2, bh / 2);
//m3DMatrix.mapPoints(point);


/**
 * 这种方式，是让每一个点都以水平中心点，竖直方向上以[水平中心，0]点进行变换。视觉上有更好弯曲效果
 */
m3DMatrix.preTranslate(-bw / 2, bh / 2);
m3DMatrix.postTranslate(bw / 2, -bh / 2);
m3DMatrix.mapPoints(point);
```



[Android——弧形弯曲显示的ImageView](https://juejin.cn/post/6844903543946625031)

[CurveImageView](https://github.com/yifantao/CurveImageView)
