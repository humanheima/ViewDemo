## 关于子线程更新UI的问题

### 先说下结论

1. 是可以在子线程更新UI的。
2. 只要绕过ViewRootImpl 的检查 checkThread方法 就可以了。
3. 一个TextView，如果开始宽高是固定的，那么在子线程更新UI的时候，不会崩溃。因为不会调用requestLayout方法。


子线程更新UI崩溃时候的异常信息

```java
 E  FATAL EXCEPTION: Thread-14
        Process: com.hm.viewdemo, PID: 11344
        android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        at android.view.ViewRootImpl.checkThread(ViewRootImpl.java:9976)
        at android.view.ViewRootImpl.requestLayout(ViewRootImpl.java:1837)
        at android.view.View.requestLayout(View.java:25085)
        at android.view.View.requestLayout(View.java:25085)
        at android.view.View.requestLayout(View.java:25085)
        at android.view.View.requestLayout(View.java:25085)
        at android.view.View.requestLayout(View.java:25085)
        at android.view.View.requestLayout(View.java:25085)
        at android.view.View.requestLayout(View.java:25085)
        at android.widget.TextView.checkForRelayout(TextView.java:9915)
        at android.widget.TextView.setText(TextView.java:6437)
        at android.widget.TextView.setText(TextView.java:6265)
        at android.widget.TextView.setText(TextView.java:6209)
        at com.hm.viewdemo.activity.NonMainThreadUpdateUIActivity$onCreate$3$1.invoke(NonMainThreadUpdateUIActivity.kt:59)
        at com.hm.viewdemo.activity.NonMainThreadUpdateUIActivity$onCreate$3$1.invoke(NonMainThreadUpdateUIActivity.kt:56)
        at kotlin.concurrent.ThreadsKt$thread$thread$1.run(Thread.kt:30)
```

TextView 的 checkForRelayout 方法
```java
private void checkForRelayout() {
    // If we have a fixed width, we can just swap in a new text layout
    // if the text height stays the same or if the view height is fixed.

    if((mLayoutParams.width != LayoutParams.WRAP_CONTENT || (mMaxWidthMode == mMinWidthMode && mMaxWidth ==
            mMinWidth)) && (mHint == null || mHintLayout != null) && (mRight - mLeft -
            getCompoundPaddingLeft() - getCompoundPaddingRight() > 0)) {
        // Static width, so try making a new text layout.

        int oldht = mLayout.getHeight();
        int want = mLayout.getWidth();
        int hintWant = mHintLayout == null ? 0 : mHintLayout.getWidth();

        /*
         * No need to bring the text into view, since the size is not
         * changing (unless we do the requestLayout(), in which case it
         * will happen at measure).
         */
        makeNewLayout(want, hintWant, UNKNOWN_BORING, UNKNOWN_BORING,
            mRight - mLeft - getCompoundPaddingLeft() - getCompoundPaddingRight(),
            false);

        if(mEllipsize != TextUtils.TruncateAt.MARQUEE) {
            // In a fixed-height view, so use our new text layout.
            if(mLayoutParams.height != LayoutParams.WRAP_CONTENT && mLayoutParams.height != LayoutParams.MATCH_PARENT) {
                //注释1处，宽高都是固定的，不会调用requestLayout方法
                autoSizeText();
                invalidate();
                return;
            }

            // Dynamic height, but height has stayed the same,
            // so use our new text layout.
            if(mLayout.getHeight() == oldht && (mHintLayout == null || mHintLayout.getHeight() == oldht)) {
                //注释2处，宽高都是固定的，不会调用requestLayout方法
                autoSizeText();
                invalidate();
                return;
            }
        }

        //注释3处，高度不固定，会调用requestLayout方法
        // We lose: the height has changed and we have a dynamic height.
        // Request a new view layout using our new text layout.
        requestLayout();
        invalidate();
    } else {
        // Dynamic width, so we have no choice but to request a new
        // view layout with a new text layout.
        //注释4处，宽度不固定，会调用requestLayout方法
        nullLayouts();
        requestLayout();
        invalidate();
    }
}
```

注释1处，宽高都是固定的，不会调用requestLayout方法。
注释2处，宽高都是固定的，不会调用requestLayout方法。

注释3处，高度不固定，会调用requestLayout方法。如果是在子线程更新UI，那么会崩溃。
注释4处，宽度不固定，会调用requestLayout方法。如果是在子线程更新UI，那么会崩溃。
