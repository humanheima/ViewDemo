[CSDN同步发布](https://blog.csdn.net/leilifengxingmw/article/details/119581164)
源码版本：`androidx1.0.0`


最基本的使用方式，添加了`app:cardCornerRadius`属性，就可以实现圆角了。`app:cardElevation`是用来实现阴影效果的，我们暂时不管阴影。


### Build.VERSION.SDK_INT >= 21实现原理

我们先看`Build.VERSION.SDK_INT >= 21`,也就是Android版本5.0及以上的是如何实现圆角的。先说一下5.0及以上的结论：

1. 给CardView设置一个圆角矩形的背景。
2. 使用该背景作为轮廓剪裁CardView，限制绘制区域。
3. CardView的子View的绘制区域不会超过CardView限制的绘制区域，从而实现圆角。
4. 完了。

```xml
<androidx.cardview.widget.CardView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_gravity="center_horizontal"
       android:layout_margin="24dp"
       app:cardCornerRadius="8dp"
       app:cardElevation="8dp">
   
       <ImageView
           android:layout_width="match_parent"
           android:layout_height="180dp"
           android:background="@mipmap/ballon" />
   
</androidx.cardview.widget.CardView>

```

CardView继承自FrameLayout。

```java
public class CardView extends FrameLayout {

    private static final int[] COLOR_BACKGROUND_ATTR = {android.R.attr.colorBackground};
    private static final CardViewImpl IMPL;

    //注释1处，根据不同的版本初始化IMPL。IMPL是实现圆角的关键类。
    static {
        if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new CardViewApi21Impl();
        } else if (Build.VERSION.SDK_INT >= 17) {
            IMPL = new CardViewApi17Impl();
        } else {
            IMPL = new CardViewBaseImpl();
        }
        //注释2处，调用IMPL的initStatic方法
        IMPL.initStatic();
    }
    //...

}
```

注释1处，根据不同的版本初始化IMPL。IMPL是实现圆角的关键类。


CardView在构造函数中获取在xml中定义的属性值。

```java
public CardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardView, defStyleAttr,
            R.style.CardView);
    ColorStateList backgroundColor;
    //设置CardView的背景，要使用app:cardBackgroundColor属性
    if (a.hasValue(R.styleable.CardView_cardBackgroundColor)) {
        backgroundColor = a.getColorStateList(R.styleable.CardView_cardBackgroundColor);
    } else {
        // There isn't one set, so we'll compute one based on the theme
        final TypedArray aa = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
        final int themeColorBackground = aa.getColor(0, 0);
        aa.recycle();

        //如果没有设置CardView的背景，根据当前的主题取不同的色值赋值给backgroundColor。
        final float[] hsv = new float[3];
        Color.colorToHSV(themeColorBackground, hsv);
        backgroundColor = ColorStateList.valueOf(hsv[2] > 0.5f
                ? getResources().getColor(R.color.cardview_light_background)
                : getResources().getColor(R.color.cardview_dark_background));
    }
    //获取设置的圆角度数
    float radius = a.getDimension(R.styleable.CardView_cardCornerRadius, 0);
    //获取设置的阴影度数
    float elevation = a.getDimension(R.styleable.CardView_cardElevation, 0);
    float maxElevation = a.getDimension(R.styleable.CardView_cardMaxElevation, 0);
    //是否不区分版本，使用统一的padding，默认是false
    mCompatPadding = a.getBoolean(R.styleable.CardView_cardUseCompatPadding, false);
    //是否阻止圆角被覆盖，默认是true
    mPreventCornerOverlap = a.getBoolean(R.styleable.CardView_cardPreventCornerOverlap, true);
    int defaultPadding = a.getDimensionPixelSize(R.styleable.CardView_contentPadding, 0);
    mContentPadding.left = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingLeft,
                defaultPadding);
    mContentPadding.top = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingTop,
                defaultPadding);
    mContentPadding.right = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingRight,
                defaultPadding);
    mContentPadding.bottom = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingBottom,
                defaultPadding);
    if (elevation > maxElevation) {
        maxElevation = elevation;
    }
    mUserSetMinWidth = a.getDimensionPixelSize(R.styleable.CardView_android_minWidth, 0);
    mUserSetMinHeight = a.getDimensionPixelSize(R.styleable.CardView_android_minHeight, 0);
    a.recycle();

    //注释3处，初始化IMPL
    IMPL.initialize(mCardViewDelegate, context, backgroundColor, radius,
            elevation, maxElevation);
}
```

注释3处，初始化IMPL，传入的第一个参数是一个CardViewDelegate对象。

```java
private final CardViewDelegate mCardViewDelegate = new CardViewDelegate() {

    private Drawable mCardBackground;

    @Override
    public void setCardBackground(Drawable drawable) {
        mCardBackground = drawable;
        setBackgroundDrawable(drawable);
    }

    @Override
    public boolean getUseCompatPadding() {
        return CardView.this.getUseCompatPadding();
    }

    @Override
    public boolean getPreventCornerOverlap() {
        return CardView.this.getPreventCornerOverlap();
    }

    @Override
    public void setShadowPadding(int left, int top, int right, int bottom) {
        mShadowBounds.set(left, top, right, bottom);
        CardView.super.setPadding(left + mContentPadding.left, top + mContentPadding.top,
                right + mContentPadding.right, bottom + mContentPadding.bottom);
    }

    @Override
    public void setMinWidthHeightInternal(int width, int height) {
        if (width > mUserSetMinWidth) {
            CardView.super.setMinimumWidth(width);
        }
        if (height > mUserSetMinHeight) {
            CardView.super.setMinimumHeight(height);
        }
    }

    @Override
    public Drawable getCardBackground() {
        return mCardBackground;
    }

    @Override
    public View getCardView() {
        return CardView.this;
    }
};

```

接下来我们先看下`CardViewApi21Impl的initialize方法`。

```java
@Override
public void initialize(CardViewDelegate cardView, Context context, ColorStateList 
             backgroundColor, float radius, float elevation, float maxElevation) {
    
    //注释1处，创建一个RoundRectDrawable并设置给CardViewDelegate
    final RoundRectDrawable background = new RoundRectDrawable(backgroundColor, radius);
    //调用CardViewDelegate的setCardBackground方法
    cardView.setCardBackground(background);
   
    //通过CardView的代理类获取CardView对象
    View view = cardView.getCardView();
    //注释2处，设置是否应使用视图的轮廓剪裁视图的内容。
    view.setClipToOutline(true);
    //设置阴影
    view.setElevation(elevation);
    //设置最大的阴影
    setMaxElevation(cardView, maxElevation);
}
```

注释1处，创建一个RoundRectDrawable并设置给CardViewDelegate。这里就是根据传入的背景色和radius创建了一个绘制圆角矩形的RoundRectDrawable，然后调用CardViewDelegate的setCardBackground方法，会将这个`RoundRectDrawable`设置为CardView的背景。这是在`Build.VERSION.SDK_INT >= 21`版本实现圆角的关键点之一。

注释2处，设置是否应使用视图的轮廓剪裁视图的内容。这是`Build.VERSION.SDK_INT >= 21`版本实现圆角的关键点之一。

```java
/**
 * 设置是否应使用视图的轮廓剪裁视图的内容。
 * <p>
 * 任何时候都只能在视图上应用单个非矩形的轮廓来剪裁视图的内容。
 *{@link ViewAnimationUtils#createCircularReveal(View, int, int, float, float)的圆形剪裁优先级高于轮廓剪裁，并且子View的轮廓剪裁的优先级高于父View的轮廓剪裁。
 * <p>
 * 注意：只有View的Outline的{@link Outline#canClip()}方法返回true的时候，设置才会起作用。
 * {@link Outline#canClip()}.
 *
 * @see #setOutlineProvider(ViewOutlineProvider)
 * @see #getClipToOutline()
 */
public void setClipToOutline(boolean clipToOutline) {
    damageInParent();
    if (getClipToOutline() != clipToOutline) {
        mRenderNode.setClipToOutline(clipToOutline);
    }
}
```

我们给CardView设置了一个圆角的RoundRectDrawable做背景，所以CardView会被剪裁成圆角矩形。

**注意：这就完了！！！**上面就是在5.0及以上CardView实现圆角的所有原理。

**这就完了？？？**那我自定义一个View，给它设置一个圆角的图片做背景，然后也调用`view.setClipToOutline(true);`，是不是就可以实现圆角呢？试一试。

第一步，先定义第一个类似RoundRectDrawable的圆角Drawable

```kotlin
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log

class TestOutlineRoundDrawable : Drawable() {

    private val TAG: String = "TestOutlineRoundDrawabl"

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mBoundsF: RectF = RectF()
    private var mBoundsI: Rect = Rect()
    //圆角暂时写死为16f
    private var radius = 16f

    init {
        //给画笔默认设置一个颜色
        paint.color=Color.CYAN
    }

    override fun onBoundsChange(bounds: Rect?) {
        bounds?.let {
            mBoundsF.set(it)
            mBoundsI.set(it)
        }
        Log.i(TAG, "onBoundsChange: ${bounds?.toString()}")
    }

    /**
     * 覆盖了Drawable的getOutline方法
     */
    override fun getOutline(outline: Outline) {
        Log.i(TAG, "getOutline:")
        //注释1处，只有大于等于Build.VERSION_CODES.LOLLIPOP(21)版本以上才能调用Outline的setRoundRect方法。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outline.setRoundRect(mBoundsI, radius)
        } else {
            super.getOutline(outline)
        }
    }

    override fun draw(canvas: Canvas) {
        Log.i(TAG, "draw: $mBoundsF")
        canvas.drawRoundRect(mBoundsF, radius, radius, paint)
    }
    
    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

}
```
* 首先我们覆盖了`onBoundsChange`方法中，保存了Drawable的边界信息。

* 然后覆盖了`getOutline`方法，注释1处，只有大于等于`Build.VERSION_CODES.LOLLIPOP(21)`版本以上才能调用Outline的setRoundRect方法。

* 然后在`draw`方法中，将整个圆角矩形绘制出来。

这里稍微看一下Drawable的getOutline方法。

```java
/**
 * Called to get the drawable to populate the Outline that defines its drawing area.
 * <p>
 * This method is called by the default {@link android.view.ViewOutlineProvider} to define
 * the outline of the View.
 * <p>
 * The default behavior defines the outline to be the bounding rectangle of 0 alpha.
 * Subclasses that wish to convey a different shape or alpha value must override this method.
 *
 * @see android.view.View#setOutlineProvider(android.view.ViewOutlineProvider)
 */
public void getOutline(@NonNull Outline outline) {
    outline.setRect(getBounds());
    outline.setAlpha(0);
}
```

这个方法就是来获取View绘制的区域。这个方法是由View类中默认的`ViewOutlineProvider`调用的，ViewOutlineProvider定义了View的轮廓。默认的轮廓就是边界矩形(这里我理解就是和控件大小一样的矩形)，透明度为0。

子类如果希望使用不同的形状或透明度可以覆盖这个方法。


View类中声明的ViewOutlineProvider对象。

```java
 ViewOutlineProvider mOutlineProvider = ViewOutlineProvider.BACKGROUND;
```

ViewOutlineProvider.BACKGROUND对象。

```java
public static final ViewOutlineProvider BACKGROUND = new ViewOutlineProvider() {
    @Override
    public void getOutline(View view, Outline outline) {
        //背景不为null，使用背景的轮廓
        Drawable background = view.getBackground();
        if (background != null) {
            background.getOutline(outline);
        } else {
            //注释1处，背景为null，默认的轮廓就是和控件一样大的矩形
            outline.setRect(0, 0, view.getWidth(), view.getHeight());
            outline.setAlpha(0.0f);
        }
    }
};
```

注释1处，背景为null，默认的轮廓就是和控件一样大的矩形。

View的`rebuildOutline`方法，内部调用了`mOutlineProvider`的`getOutline`方法。来获取轮廓。`rebuildOutline`方法什么时候被调用，我们就不去细究了，但是可以猜测，在控件设置背景图，添加到窗口，或者控件大小发生变化的时候会调用该方法来获取正确的轮廓。

```java
private void rebuildOutline() {
    //Unattached views ignore this signal, and outline is recomputed in onAttachedToWindow()
    if (mAttachInfo == null) return;

    if (mOutlineProvider == null) {
        // no provider, remove outline
        mRenderNode.setOutline(null);
    } else {
        final Outline outline = mAttachInfo.mTmpOutline;
        outline.setEmpty();
        outline.setAlpha(1.0f);

        mOutlineProvider.getOutline(this, outline);
        mRenderNode.setOutline(outline);
    }
}
```

第二步，使用TestOutlineRoundDrawable

我们在xml中声明一个FrameLayout，没有任何子View。

```xml
<FrameLayout
    android:id="@+id/flTestOutline"
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:layout_marginTop="24dp"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent">

</FrameLayout>

```

```kotlin
private lateinit var flTestOutline: FrameLayout

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_outline_test)
    flTestOutline = findViewById(R.id.flTestOutline)
    //设置TestOutlineRoundDrawable作为背景
    flTestOutline.background = TestOutlineRoundDrawable()
    //大于等于`Build.VERSION_CODES.LOLLIPOP(21)`版本调用View的setClipToOutline方法，并传入true
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        flTestOutline.clipToOutline = true
    }

}
```

运行效果如下所示。

![CardView_1.jpg](https://upload-images.jianshu.io/upload_images/3611193-94ac5e259bde09d3.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


修改xml文件，在FrameLayout中包裹一个ImageView。

```xml
<FrameLayout
    android:id="@+id/flTestOutline"
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:layout_marginTop="24dp"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent">

    <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:scaleType="centerCrop"
            android:src="@mipmap/ballon" />

</FrameLayout>

```

在大于等于`Build.VERSION_CODES.LOLLIPOP(21)`版本上的运行效果如下所示。


![CardView_2.jpg](https://upload-images.jianshu.io/upload_images/3611193-eab74897b33a1cfb.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

图片被剪裁成了圆形。

在**小于**`Build.VERSION_CODES.LOLLIPOP(21)`版本上的运行效果如下所示。

![CardView_3.jpg](https://upload-images.jianshu.io/upload_images/3611193-7f90ddaf2b67a6ef.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

图片还是直角，不起作用。


### Build.VERSION.SDK_INT < 21实现原理

先说结论：
1. 在`Build.VERSION.SDK_INT < 21`的情况下，CardView是没有实现圆角的，图片还是直角。
2. CardView设置了一个带阴影圆角矩形的`RoundRectDrawableWithShadow`做背景。
3. 如果cardCornerRadius或cardElevation不为0，这个圆角矩形(不包括圆角矩形周围的阴影)的尺寸小于CardView控件的大小。
4. 如果cardCornerRadius或cardElevation不为0，CardView会设置padding，CardView
包裹的子View的尺寸会变小。子View的最大尺寸<=圆角矩形的大小。当我们指定`app:cardPreventCornerOverlap="false"`的时候，子View的最大尺寸=圆角矩形的大小。

看个例子，我们在布局中使用CardView和FrameLayout分别包裹一个ImageView。当设置了cardCornerRadius和cardElevation的时候，明显可以看出ImageView的尺寸变小了。如下图所示：

```xml
<androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_margin="24dp"
        app:cardCornerRadius="16dp"
        app:cardElevation="16dp">

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="180dp"
            android:background="@mipmap/ballon" />

    </androidx.cardview.widget.CardView>

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_margin="24dp">

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="180dp"
            android:background="@mipmap/ballon" />

    </FrameLayout>

```
![CardView_4.jpg](https://upload-images.jianshu.io/upload_images/3611193-5aafc5aedf45546d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


结论的第3点：如果cardCornerRadius或cardElevation不为0，这个圆角矩形的尺寸小于CardView控件的大小。


RoundRectDrawableWithShadow的draw方法。

```java
@Override
public void draw(Canvas canvas) {
    if (mDirty) {
        //注释1处
        buildComponents(getBounds());
        mDirty = false;
    }
    canvas.translate(0, mRawShadowSize / 2);
    drawShadow(canvas);
    canvas.translate(0, -mRawShadowSize / 2);
    //注释2处，绘制圆角矩形，在浅色主题下，通常是一个白色的圆角矩形
    sRoundRectHelper.drawRoundRect(canvas, mCardBounds, mCornerRadius, mPaint);
}
```

注释1处，调用buildComponents(getBounds())方法。


```java

private static final float SHADOW_MULTIPLIER = 1.5f;

private void buildComponents(Rect bounds) {
    //注释1处，这里的mRawMaxShadowSize就是我们设置的app:cardMaxElevation， 
    //这里给drawable设置了一定的padding，所以绘制出来的圆角矩形小于控件的大小。
    
    final float verticalOffset = mRawMaxShadowSize * SHADOW_MULTIPLIER;
    mCardBounds.set(bounds.left + mRawMaxShadowSize, bounds.top + verticalOffset,
            bounds.right - mRawMaxShadowSize, bounds.bottom - verticalOffset);
    buildShadowCorners();
}
```

注释1处，这里的mRawMaxShadowSize就是我们设置的app:cardMaxElevation，这里给drawable设置了一定的padding。

左右的padding是`mRawMaxShadowSize`，上下的padding`1.5*mRawMaxShadowSize`。

所以在draw方法中的注释2处，最终绘制圆角矩形的时候，绘制出来的圆角矩形小于控件的大小。

结论第4点： 如果cardCornerRadius或cardElevation不为0，CardView会设置padding，CardView包裹的子View的尺寸会变小。子View的最大尺寸<=圆角矩形的大小。当我们指定`app:cardPreventCornerOverlap="false"`的时候，子View的最大尺寸=圆角矩形的大小。


CardViewBaseImpl的initialize方法。

```java
@Override
public void initialize(CardViewDelegate cardView, Context context,
        ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
    RoundRectDrawableWithShadow background = createBackground(context, backgroundColor, radius,
            elevation, maxElevation);
    background.setAddPaddingForCorners(cardView.getPreventCornerOverlap());
    cardView.setCardBackground(background);
    //注释1处，
    updatePadding(cardView);
}
```

注释1处，调用updatePadding方法。

```java
@Override
public void updatePadding(CardViewDelegate cardView) {
    Rect shadowPadding = new Rect();
    //注释1处，获取边界大小
    getShadowBackground(cardView).getMaxShadowAndCornerPadding(shadowPadding);
    //设置CardView的最小尺寸
    cardView.setMinWidthHeightInternal((int) Math.ceil(getMinWidth(cardView)),
            (int) Math.ceil(getMinHeight(cardView)));
    //注释3处，最终给CardView设置padding
    cardView.setShadowPadding(shadowPadding.left, shadowPadding.top,
            shadowPadding.right, shadowPadding.bottom);
}
```

注释1处，调用`RoundRectDrawableWithShadow`的`getMaxShadowAndCornerPadding`方法获取边界大小。


```java
void getMaxShadowAndCornerPadding(Rect into) {
    getPadding(into);
}
```

```java
@Override
public boolean getPadding(Rect padding) {
    //获取竖直方向上的padding
    int vOffset = (int) Math.ceil(calculateVerticalPadding(mRawMaxShadowSize, mCornerRadius,
            mAddPaddingForCorners));
    //获取水平方向上的padding
    int hOffset = (int) Math.ceil(calculateHorizontalPadding(mRawMaxShadowSize, mCornerRadius,
            mAddPaddingForCorners));
    padding.set(hOffset, vOffset, hOffset, vOffset);
    return true;
}
```

获取竖直方向上的padding

```java
static float calculateVerticalPadding(float maxShadowSize, float cornerRadius,
        boolean addPaddingForCorners) {
    //注释1处，addPaddingForCorners等价于CardView的属性app:cardPreventCornerOverlap，默认为true
    if (addPaddingForCorners) {
        //COS_45 = 0.7071067811865476，最后结果约等于1.5 * maxShadowSize + 0.3 * cornerRadius
        return (float) (maxShadowSize * SHADOW_MULTIPLIER + (1 - COS_45) * cornerRadius);
    } else {
        return maxShadowSize * SHADOW_MULTIPLIER;
    }
}

```
注释1处，addPaddingForCorners等价于CardView的属性app:cardPreventCornerOverlap，默认为true。计算出来的padding约等于`1.5 * maxShadowSize + 0.3 * cornerRadius`，是大于结论3中竖直方向上的padding的。结论3中圆角矩形的竖直方向上的padding是`1.5 * maxShadowSize`。

如果addPaddingForCorners为false，则计算出来的padding等于结论3中竖直方向上的padding。


获取水平方向上的padding

```java
static float calculateHorizontalPadding(float maxShadowSize, float cornerRadius,
        boolean addPaddingForCorners) {
    if (addPaddingForCorners) {
        return (float) (maxShadowSize + (1 - COS_45) * cornerRadius);
    } else {
        return maxShadowSize;
    }
}
```
如果addPaddingForCorners为true，计算出来的结果约等于`maxShadowSize + 0.3 * cornerRadius`，是大于结论3中水平padding的，结论3中圆角矩形的水平方向上的padding是`maxShadowSize`。
如果addPaddingForCorners为false，计算出来的结果等于结论3中水平padding，`maxShadowSize`。

updatePadding方法的注释3处，最终给CardView设置padding。

CardViewDelegate的setShadowPadding方法。
```java

 @Override
public void setShadowPadding(int left, int top, int right, int bottom) {
    mShadowBounds.set(left, top, right, bottom);
    //给CardView设置padding。
    CardView.super.setPadding(left + mContentPadding.left, top + mContentPadding.top,
                right + mContentPadding.right, bottom + mContentPadding.bottom);
}

```

所以最终CardView的子View的最大尺寸总是会小于等于这个圆角矩形。


参考链接：
* [Android CardView源码分析](https://blog.csdn.net/chennai1101/article/details/83150554)
