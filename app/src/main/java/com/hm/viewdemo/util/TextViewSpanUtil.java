package com.hm.viewdemo.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

public class TextViewSpanUtil {


    private static final String TAG = "TextViewSpanUtil";

    /**
     * 设置textView结尾...后面显示的文字和颜色
     *
     * @param context 上下文
     * @param textView textview
     * @param minLines 最少的行数
     * @param originText 原文本
     * @param endText 结尾文字
     * @param endColorID 结尾文字颜色id
     * @param isExpand 当前是否是展开状态
     */
    public static void toggleEllipsize(final Context context,
            final TextView textView,
            final int minLines,
            final String originText,
            final String endText,
            final int endColorID,
            final boolean isExpand) {
        if (TextUtils.isEmpty(originText)) {
            return;
        }
        textView.requestLayout();
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isExpand) {
                    textView.setText(originText);
                } else {
                    int paddingLeft = textView.getPaddingLeft();
                    int paddingRight = textView.getPaddingRight();
                    TextPaint paint = textView.getPaint();

                    //String dot = "三个字";
                    //float dotWidth = paint.measureText(dot);
                    //Log.i(TAG, "onGlobalLayout: dotWidth = " + dotWidth);
                    float moreTextWidth = textView.getTextSize() * endText.length();
                    float moreTextWidth2 = paint.measureText(endText);
                    Log.i(TAG, "onGlobalLayout: moreTextWidth = " + moreTextWidth);
                    Log.i(TAG, "onGlobalLayout: moreTextWidth2 measure = " + moreTextWidth2);
                    float availableTextWidth =
                            (textView.getWidth() - paddingLeft - paddingRight) * minLines - moreTextWidth;
                    CharSequence ellipsizeStr = TextUtils.ellipsize(originText + endText, paint, availableTextWidth,
                            TextUtils.TruncateAt.END);
                    String finalText;
                    if (ellipsizeStr.length() < originText.length() + endText.length()) {
                        int length = ellipsizeStr.length();
                        String temp = ellipsizeStr.subSequence(0, length - 3) + "…" + endText;
                        finalText = temp;
                    } else {
                        finalText = originText + endText;
                    }
                    SpannableStringBuilder ssb = new SpannableStringBuilder(finalText);
                    int end = finalText.length();
                    int start = end - endText.length();
                    ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor
                                    (endColorID)),
                            start, end,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            if (widget instanceof TextView) {
                                ((TextView) widget).setHighlightColor(Color.TRANSPARENT);
                            }
                            Toast.makeText(context, "haha", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void updateDrawState(@NonNull TextPaint ds) {
                            //super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    textView.setText(ssb);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                }
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}