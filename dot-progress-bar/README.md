### DotProgressBar的源码分析

使用 DotProgressBarUse 进行分析。

比如有3个点，并且控件宽大于高


### 圆心，半径相关
```java

@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());

    if (getMeasuredHeight() > getMeasuredWidth()) {
        dotRadius = getMeasuredWidth() / dotAmount / 4;
    } else {
        //注释1处，
        dotRadius = getMeasuredHeight() / 4;
    }

    //注释2处
    bounceDotRadius = dotRadius + (dotRadius / 3);

    //注释3处，计算圆心的位置
    
    //  dotRadius * (dotAmount - 1) 是圆之间空隙的距离。
    float circlesWidth = (dotAmount * (dotRadius * 2)) + dotRadius * (dotAmount - 1);
    xCoordinate = (getMeasuredWidth() - circlesWidth) / 2 + dotRadius;
}
```

以有3个点进行分析：初始半径为dotRadius

注释1处，控件宽大于高。那么，初始圆半径就是控件高度的四分之一 (dotRadius = height / 4)
。整个圆的直径是高度的1/2。

注释2处，最大的圆是半径是 bounceDotRadius = height / 4 + height / 12 = 4*height / 12 = height /
3。整个圆的直径是高度的2/3。

注释3处，
dotRadius * 2 ：圆的直径

(dotAmount * (dotRadius * 2)) ：所有的圆的宽度 = 6dotRadius

dotRadius * (dotAmount - 1)：圆之间的空隙。比如3个圆点，那么就有2个空隙。每个空隙的宽度定为dotRadius。

(dotAmount * (dotRadius * 2)) + dotRadius * (dotAmount - 1)： = 8dotRadius  = 2*height (高度的两倍)

如果控件的宽是高的2倍的话，那么 xCoordinate 就是 dotRadius。 以 xCoordinate 为圆心画出来的第一个圆的最左边正好是控件的最左边。最后一个圆的最右边正好是控件的最右边。

如果这个时候，绘制的是最大的圆的话。那么最左边圆超过1/3dotRadius，右边的圆超过1/3dotRadius。总共超过 2/3dotRadius。


dotRadius = height / 4

height /4 * 2 /3 = 1/6*height

所以：如果宽度是高度的 （2 + 1/6 = 13/6）的时候，绘制最大的圆，正好左右可以到控件的左右边。

### 绘制动画相关

- [DotProgressBarExample](https://github.com/silvestrpredko/DotProgressBarExample)