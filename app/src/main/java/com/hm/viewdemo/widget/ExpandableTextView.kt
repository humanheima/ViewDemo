package com.hm.viewdemo.widget;

import android.content.Context;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

public class ExpandableTextView extends androidx.appcompat.widget.AppCompatTextView {

    private static final int MAX_LINES = 3;
    private static final String MORE_TEXT = "详情 > ";
    private boolean isTruncated = false;

    public ExpandableTextView(Context context) {
        super(context);
        init();
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setMaxLines(MAX_LINES);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Layout layout = getLayout();
        if (layout != null) {
            int lineCount = layout.getLineCount();
            if (lineCount > MAX_LINES) {
                isTruncated = true;
                truncateText();
            } else {
                isTruncated = false;
                setText(getText()); // 恢复原始文本
            }
        }
    }

    private void truncateText() {
        Layout layout = getLayout();
        if (layout == null) return;

        // 获取第三行的起始字符索引
        int thirdLineStart = layout.getLineStart(MAX_LINES - 1);
        int thirdLineEnd = layout.getLineEnd(MAX_LINES - 1);

        // 获取原始文本
        String originalText = getText().toString();
        if (thirdLineEnd >= originalText.length()) return;

        // 计算第三行能显示的最大字符数（留出“详情”空间）
        String thirdLineText = originalText.substring(thirdLineStart, thirdLineEnd);
        int maxLength = thirdLineText.length() - MORE_TEXT.length() - 1; // 预留“详情”和省略号

        if (maxLength <= 0) {
            maxLength = 0;
        }

        // 截断文本
        String truncatedText = originalText.substring(0, thirdLineStart + maxLength) + "..." + MORE_TEXT;

        // 创建 SpannableString 以设置“详情”的颜色（可选）
        SpannableString spannable = new SpannableString(truncatedText);
        spannable.setSpan(
                new ForegroundColorSpan(0xFF0000FF), // 蓝色
                truncatedText.length() - MORE_TEXT.length(),
                truncatedText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        setText(spannable);
    }

    public boolean isTruncated() {
        return isTruncated;
    }
}