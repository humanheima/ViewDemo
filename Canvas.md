
### save方法和restore方法

```
    /**
     * Saves the current matrix and clip onto a private stack。
     * <p>
     * Subsequent calls to translate,scale,rotate,skew,concat or clipRect,
     * clipPath will all operate as usual, but when the balancing call to
     * restore() is made, those calls will be forgotten, and the settings that
     * existed before the save() will be reinstated.
     *
     * @return The value to pass to restoreToCount() to balance this save()
     */
    public int save() {
        return nSave(mNativeCanvasWrapper, MATRIX_SAVE_FLAG | CLIP_SAVE_FLAG);
    }

```

当我们调用save方法的时候，会将当前矩阵和剪辑保存到专用堆栈。然后调用translate,scale,rotate,skew,concat or clipRect, clipPath这些改变矩阵和剪辑的方法和平常一样。但是当我们调用restore方法以后，这些调用这些改变矩阵和剪辑的方法的结果会被丢弃。矩阵和剪辑会恢复到save方法之前。

举个例子

```
    override fun onDraw(canvas: Canvas) {
        //将画布移动到控件中心
        canvas.translate(width / 2f, height / 2f)
        canvas.save()
        for (i in 0..5) {
            canvas.drawLine(0f, 0f, 100f, 0f, mPaint)
            //每次旋转60度
            canvas.rotate(60f)
            
        }
        canvas.restore()

    }

```
在这个例子中，我们画了6条线，每条线之间的夹角是60度。

总结：save方法之后改变画布的矩阵和剪辑信息，在restore方法会全部被丢弃，画布的矩阵和剪辑信息恢复到调用sava方法之前。






