###View 基础知识
 **View滑动的三种实现方式**

- scrollTo/scrollBy方式：操作简单，适合对view内容的滑动
- 动画，操作简单，适用于没有交互的view和复杂的动画效果
- 改变布局参数，操作稍微复杂，适用于有交互的view


```
protected void onDraw(Canvas canvas) {

}


```

View在绘制过程中的Canvas是从哪里来的？从ViewRootImpl中来的。

```
private void performDraw() {

    boolean canUseAsync = draw(fullRedrawNeeded);
    
}

```

```
private boolean draw(boolean fullRedrawNeeded) {

    if (!drawSoftware(surface, mAttachInfo, xOffset, yOffset,
                        scalingRequired, dirty, surfaceInsets)) {
                    return false;
                }
}

```

```
private boolean drawSoftware(Surface surface, AttachInfo attachInfo, int xoff, int yoff,
            boolean scalingRequired, Rect dirty, Rect surfaceInsets) {


    final Canvas canvas;
    //获取canvas对象
    canvas = mSurface.lockCanvas(dirty);            
}
```
   