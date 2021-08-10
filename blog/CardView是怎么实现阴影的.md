### Build.VERSION.SDK_INT >= 21实现原理


`Build.VERSION.SDK_INT >= 21`，也就是Android版本5.0及以上采用了 Material Design 设计语言，引入了 Z 轴的概念，也就是垂直于屏幕的轴，Z 轴会让 View 产生阴影的效果。[Android Material Design 阴影实现](https://www.jianshu.com/p/bcbd0769e7ac)

所以在Android版本5.0及以上很简单，就是Z轴实现的阴影。

但是有一点需要注意，使用CardView的时候，CardView要距离父布局有一定的margin，不然阴影显示会很奇怪。


CardView距离父布局没有margin。

```xml
<FrameLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    android:layout_marginTop="100dp">

    <androidx.cardview.widget.CardView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:cardCornerRadius="12dp"
        app:cardElevation="16dp" >

        <ImageView
            android:layout_width="278dp"
            android:layout_height="150dp"
            android:background="@mipmap/ballon" />

    </androidx.cardview.widget.CardView>

</FrameLayout>

```
![CardView_5.jpg](https://upload-images.jianshu.io/upload_images/3611193-52a188ecdc82565e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



CardView距离父布局有margin，margin=16dp

```xml
<FrameLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    android:layout_marginTop="100dp">

    <androidx.cardview.widget.CardView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:cardCornerRadius="12dp"
        app:cardElevation="16dp" >

        <ImageView
            android:layout_width="278dp"
            android:layout_height="150dp"
            android:background="@mipmap/ballon" />

    </androidx.cardview.widget.CardView>

</FrameLayout>

```

![CardView_6.jpg](https://upload-images.jianshu.io/upload_images/3611193-0030b7e1c33b0850.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### Build.VERSION.SDK_INT >= 21版本以下实现原理

测试机：Pixel2，Build.VERSION.SDK_INT = 19

实现原理很简单：

1. CardView设置了一定的padding，CardView的圆角矩形区域小于CardView控件大小。
2. CardView的子View的最大尺寸总是会小于等于这个圆角矩形。
3. 在CardView四周绘制阴影。阴影绘制分为8个步骤。
3.1. 四个角绘制渐变扇形。扇形阴影使用RadialGradient来绘制。
3.2. 四条边绘制渐变矩形。矩形阴影使用LinearGradient来绘制。

**先拉到文章后面看看那几张图片就明白了。代码细节可以自己去看。**


设置padding的过程可以参考上一篇文章[CardView是怎么实现圆角的？](https://www.jianshu.com/p/eddfa28ac00e)。

在21版本以下，CardView使用`RoundRectDrawableWithShadow`作为背景，我们将`RoundRectDrawableWithShadow`拷贝一份出来，然后使用FrameLayout来模拟CardView。

```xml

<FrameLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="60dp">

        <androidx.cardview.widget.CardView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="16dp"
            app:cardCornerRadius="12dp"
            app:cardElevation="16dp">

            <ImageView
                android:layout_width="278dp"
                android:layout_height="150dp"
                android:background="@mipmap/ballon" />

        </androidx.cardview.widget.CardView>

    </FrameLayout>


    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="@color/colorAccent" />

    <!--使用FrameLayout，手动设置RoundRectDrawableWithShadow-->
    <FrameLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal">

        <FrameLayout
            android:id="@+id/flManualSetBg"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="16dp">

            <ImageView
                android:layout_width="278dp"
                android:visibility="invisible"
                android:layout_height="150dp"
                android:background="@mipmap/ballon" />

        </FrameLayout>

    </FrameLayout>

```

![CardView_10.jpg](https://upload-images.jianshu.io/upload_images/3611193-b78881561ba56c71.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

红线上面使用的是CardView。红线下面使用的就是FrameLayout，然后自己设置的padding和RoundRectDrawableWithShadow。两者的效果是一样的。

```kotlin
class CardViewActivity : AppCompatActivity() {


    private val TAG: String = "CardViewActivity"

    private val SHADOW_MULTIPLIER = 1.5f

    private val COS_45 = Math.cos(Math.toRadians(45.0))

    private lateinit var flManualSetBg: FrameLayout

    //圆角12dp，对应app:cardCornerRadius="12dp"
    private var mRadius: Int = 12
    //阴影和最大阴影都设为16dp，对应app:cardElevation="16dp"
    private var mElevation: Int = 16
    private var mMaxElevation: Int = 16

    //5.0版本以下，CardView设置的竖直方向上和水平方向上的padding
    private var verticalPadding: Int = 0
    private var horizontalPadding: Int = 0

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, CardViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_view)
        Log.i(TAG, "onCreate: COS_45 = $COS_45")

        flManualSetBg = findViewById(R.id.flManualSetBg)

        mRadius = ScreenUtil.dpToPx(this, 12)
        mElevation = ScreenUtil.dpToPx(this, 16)
        mMaxElevation = ScreenUtil.dpToPx(this, 16)

        verticalPadding = ceil((SHADOW_MULTIPLIER * mMaxElevation + (1 - COS_45) * mRadius)).toInt()
        horizontalPadding = ceil((mMaxElevation + (1 - COS_45) * mRadius)).toInt()

        RoundRectDrawableWithShadow.sRoundRectHelper = RoundRectDrawableWithShadow.RoundRectHelper { canvas, bounds, cornerRadius, paint -> canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint) }

        var backgroundColor: ColorStateList = ColorStateList.valueOf(resources.getColor(androidx.cardview.R.color.cardview_light_background))

        val drawable = RoundRectDrawableWithShadow(resources, backgroundColor, mRadius.toFloat(), mElevation.toFloat(), mMaxElevation.toFloat())
        //注释1处
        flManualSetBg.setBackgroundDrawable(drawable)

        //注释2处，手动给FrameLayout设置padding，模拟CardView设置的padding
        flManualSetBg.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

    }
    //...
}
```

注释1处，给FrameLayout设置RoundRectDrawableWithShadow作为背景。
注释2处，手动给FrameLayout设置padding，模拟CardView设置的padding。

接下来我们就把RoundRectDrawableWithShadow中的绘制代码一部分一部分的看。


RoundRectDrawableWithShadow的draw方法。

```java
@Override
public void draw(Canvas canvas) {
    if (mDirty) {
        //注释1处
        buildComponents(getBounds());
        mDirty = false;
    }
    //注释2处
    canvas.translate(0, mRawShadowSize / 2);
    //注释3处，绘制阴影
    drawShadow(canvas);
    canvas.translate(0, -mRawShadowSize / 2);
    //注释4处，绘制圆角矩形
    // sRoundRectHelper.drawRoundRect(canvas, mCardBounds, mCornerRadius, mPaint);
}
```

注释2处，画布向上偏移`mRawShadowSize / 2`。

注释3处，绘制阴影。

我们先把注释4处的绘制圆角矩形的代码注释掉，这样关于圆角的绘制看的更加清楚。

```java
private void drawShadow(Canvas canvas) {
    //阴影上边界
    final float edgeShadowTop = -mCornerRadius - mShadowSize;
    final float inset = mCornerRadius + mInsetShadow + mRawShadowSize / 2;
    //正常情况下应该成立
    final boolean drawHorizontalEdges = mCardBounds.width() - 2 * inset > 0;
    final boolean drawVerticalEdges = mCardBounds.height() - 2 * inset > 0;
    // LT，注释1处，绘制左上角的阴影
    int saved = canvas.save();
    canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset);
    canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
    //注释2处
    if (drawHorizontalEdges) {
        canvas.drawRect(0, edgeShadowTop,
                mCardBounds.width() - 2 * inset, -mCornerRadius,
                mEdgeShadowPaint);
    }
    canvas.restoreToCount(saved);
    // RB，右上角，注释3处
    saved = canvas.save();
    canvas.translate(mCardBounds.right - inset, mCardBounds.bottom - inset);
    canvas.rotate(180f);
    canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
    //注释4处
    if (drawHorizontalEdges) {
        canvas.drawRect(0, edgeShadowTop,
                mCardBounds.width() - 2 * inset, -mCornerRadius + mShadowSize,
                mEdgeShadowPaint);
    }
    canvas.restoreToCount(saved);
    // LB，注释5处
    saved = canvas.save();
    canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - inset);
    canvas.rotate(270f);
    canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
    //注释6处
    if (drawVerticalEdges) {
        canvas.drawRect(0, edgeShadowTop,
                mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
    }
    canvas.restoreToCount(saved);
    // RT，注释7处，
    saved = canvas.save();
    canvas.translate(mCardBounds.right - inset, mCardBounds.top + inset);
    canvas.rotate(90f);
    canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
   注释8处
    if (drawVerticalEdges) {
        canvas.drawRect(0, edgeShadowTop,
                mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
    }
    canvas.restoreToCount(saved);
}
```

注释1处，绘制左上角的阴影

```java
   //LT，注释1处，绘制左上角的阴影
    int saved = canvas.save();
    canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset);
    canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
    //...
    canvas.restoreToCount(saved);
```

效果：

![shadow_1.jpg](https://upload-images.jianshu.io/upload_images/3611193-0449362d755733a5.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

注释1，2处

```
    // LT，注释1处，绘制左上角的阴影
    int saved = canvas.save();
    canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset);
    canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
    //注释2处
    if (drawHorizontalEdges) {
        canvas.drawRect(0, edgeShadowTop,
                mCardBounds.width() - 2 * inset, -mCornerRadius,
                mEdgeShadowPaint);
    }
    canvas.restoreToCount(saved);
```

效果：

![shadow_12.jpg](https://upload-images.jianshu.io/upload_images/3611193-5f1dea5775dc7aa7.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


注释1，2，3处效果：
![shadow_123.jpg](https://upload-images.jianshu.io/upload_images/3611193-8290cad817ff8c69.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


注释1，2，3，4处效果：

![shadow_1234.jpg](https://upload-images.jianshu.io/upload_images/3611193-10042fa4f5b4acd4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


注释1，2，3，4，5处效果：

![shadow_12345.jpg](https://upload-images.jianshu.io/upload_images/3611193-a1af2d212599bb37.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


注释1，2，3，4，5，6处效果：

![shadow_123456.jpg](https://upload-images.jianshu.io/upload_images/3611193-68e76619b1639a53.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


注释1，2，3，4，5，6，7处效果：

![shadow_1234567.jpg](https://upload-images.jianshu.io/upload_images/3611193-b913e7ad2e2814b6.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


注释1，2，3，4，5，6，7，8处效果：

![shadow12345678.jpg](https://upload-images.jianshu.io/upload_images/3611193-742478b3ab7fadc9.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

再将draw方法中，注释4处代码打开。效果如下：

![shadow_round_rect.jpg](https://upload-images.jianshu.io/upload_images/3611193-b1e1561978e836fc.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果FrameLayout有子View的话，显示的最终效果如下：

![FrameLayoutOneChild.jpg](https://upload-images.jianshu.io/upload_images/3611193-6f22d1bd995245e8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

参考链接：

* [Android Material Design 阴影实现](https://www.jianshu.com/p/bcbd0769e7ac)
* [CardView是怎么实现圆角的？](https://www.jianshu.com/p/eddfa28ac00e)。