package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hm.viewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmw on 2018/12/2.
 * Desc:简单流式布局
 * 只显示一行，多余的不显示,有空可以拓展一下可以控制显示多少行。
 */
public class SimpleFlowLayout extends RelativeLayout {

    private final String TAG = getClass().getSimpleName();

    //水平间距，单位是dp
    private int horizontalSpacing;
    //竖直间距，单位是dp
    private int verticalSpacing;

    private List<Line> lines = new ArrayList<>();

    //当前行
    private Line line;

    //当前行使用的空间
    private int lineSize;

    private int textSize;

    private int textColor;

    private int backgroundResource = R.drawable.bg_flow_layout_item;

    //文字水平方向上的padding
    private int textPaddingH;

    //文字竖直方向上的padding
    private int textPaddingV;

    /**
     * 是否只显示一行
     */
    private boolean onlyShowOneLine;

    /**
     * 只显示一行的时候 子view的个数
     */
    private int onlyOneLineChildCount;

    public SimpleFlowLayout(Context context) {
        this(context, null);
    }

    public SimpleFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleFlowLayout);
        horizontalSpacing = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_horizontalSpacing, dp2px(12));
        verticalSpacing = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_verticalSpacing, dp2px(12));
        textSize = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_itemSize, sp2px(14));
        textColor = ta.getColor(R.styleable.SimpleFlowLayout_itemColor, Color.BLACK);
        backgroundResource = ta.getResourceId(R.styleable.SimpleFlowLayout_backgroundResource, R.drawable.bg_flow_layout_item);
        textPaddingH = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_textPaddingH, dp2px(12));
        textPaddingV = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_textPaddingV, dp2px(8));
        onlyShowOneLine = ta.getBoolean(R.styleable.SimpleFlowLayout_onlyOneLine, false);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //实际可用宽高
        int width = MeasureSpec.getSize(widthMeasureSpec - getPaddingLeft() - getPaddingRight());
        int height = MeasureSpec.getSize(heightMeasureSpec - getPaddingTop() - getPaddingBottom());
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        restoreLine();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //测量所有的child
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            if (line == null) {
                line = new Line();
            }

            //计算当前行已使用的高度
            int measureWidth = child.getMeasuredWidth();
            lineSize += measureWidth;
            // 如果使用的宽度小于可用的宽度，这时候childView能够添加到当前的行上
            if (lineSize <= width) {
                line.addChild(child);
                lineSize += horizontalSpacing;
                onlyOneLineChildCount = i + 1;
            } else {
                if (onlyShowOneLine) {
                    /**
                     * 如果只显示一行的话，就直接退出循环。并把多余的view 移除掉。
                     */
                    Log.d(TAG, "onMeasure: onlyShowOneLine break.");
                    for (int j = onlyOneLineChildCount; j < getChildCount(); j++) {
                        removeViewAt(j);
                        removeViews(onlyOneLineChildCount, getChildCount() - onlyOneLineChildCount);
                    }
                    break;
                }
                Log.d(TAG, "onMeasure: multi lines.");
                //换行
                newLine();
                line.addChild(child);
                lineSize += child.getMeasuredWidth();
                lineSize += horizontalSpacing;
            }
        }

        Log.d(TAG, "onMeasure: childCount=" + getChildCount());

        // 把最后一行记录到集合中
        if (line != null && !lines.contains(line)) {
            lines.add(line);
        }

        int totalHeight = 0;
        // 把所有行的高度加上
        for (int i = 0; i < lines.size(); i++) {
            totalHeight += lines.get(i).getHeight();
        }
        // 加上行的竖直间距

        totalHeight += verticalSpacing * (lines.size() - 1);

        totalHeight += getPaddingTop();
        totalHeight += getPaddingBottom();

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.layout(left, top);
            top = top + line.getHeight() + verticalSpacing;
        }
    }

    private void newLine() {
        if (line != null) {
            lines.add(line);
        }
        line = new Line();
        lineSize = 0;
    }

    private void restoreLine() {
        lines.clear();
        line = new Line();
        lineSize = 0;
    }

    /**
     * 设置关键字标签
     *
     * @param str                 关键字
     * @param onItemClickListener 点击监听
     */
    public void setView(String str, final OnItemClickListener onItemClickListener) {
        List<String> list = new ArrayList<>();
        list.add(str);
        setViews(list, onItemClickListener);
    }

    /**
     * 设置关键字标签
     *
     * @param list                关键字
     * @param onItemClickListener 点击监听
     */
    public void setViews(List<String> list, final OnItemClickListener onItemClickListener) {
        removeAllViews();
        addViews(list, onItemClickListener);
    }

    /**
     * 增加关键字标签
     *
     * @param str                 关键字
     * @param onItemClickListener 点击监听
     */
    public void addView(String str, final OnItemClickListener onItemClickListener) {
        List<String> list = new ArrayList<>();
        list.add(str);
        addViews(list, onItemClickListener);
    }

    /**
     * 增加关键字标签
     *
     * @param list                关键字
     * @param onItemClickListener 点击监听
     */
    public void addViews(List<String> list, final OnItemClickListener onItemClickListener) {
        for (int i = 0; i < list.size(); i++) {
            final TextView tv = new TextView(getContext());

            // 设置TextView属性
            tv.setText(list.get(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv.setTextColor(textColor);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);

            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setClickable(true);
            tv.setBackgroundResource(backgroundResource);
            addView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            if (onItemClickListener != null) {
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(tv.getText().toString());
                    }
                });
            }
        }
    }

    /**
     * 设置文字水平间距
     *
     * @param horizontalSpacing 间距/dp
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = dp2px(horizontalSpacing);
    }

    /**
     * 设置文字垂直间距
     *
     * @param verticalSpacing 间距/dp
     */
    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = dp2px(verticalSpacing);
    }

    /**
     * 设置文字大小
     *
     * @param textSize 文字大小/sp
     */
    public void setTextSize(int textSize) {
        this.textSize = sp2px(textSize);
    }

    /**
     * 设置文字颜色
     *
     * @param textColor 文字颜色
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /**
     * 设置文字背景
     *
     * @param backgroundResource 文字背景
     */
    public void setBackgroundResource(int backgroundResource) {
        this.backgroundResource = backgroundResource;
    }

    /**
     * 设置文字水平padding
     *
     * @param textPaddingH padding/dp
     */
    public void setTextPaddingH(int textPaddingH) {
        this.textPaddingH = dp2px(textPaddingH);
    }

    /**
     * 设置文字垂直padding
     *
     * @param textPaddingV padding/dp
     */
    public void setTextPaddingV(int textPaddingV) {
        this.textPaddingV = dp2px(textPaddingV);
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * 点击事件监听
     */
    public interface OnItemClickListener {
        void onItemClick(String content);
    }

    class Line {

        //行高
        int height;
        private List<View> children = new ArrayList<>();

        public void addChild(View childView) {
            children.add(childView);
            if (height < childView.getMeasuredHeight()) {
                height = childView.getMeasuredHeight();
            }
        }

        public void layout(int left, int top) {
            // 当前childView的左上角x轴坐标
            int currentLeft = left;

            for (int i = 0; i < children.size(); i++) {
                View view = children.get(i);
                view.layout(currentLeft, top, currentLeft + view.getMeasuredWidth(), top + view.getMeasuredHeight());
                currentLeft = currentLeft + view.getMeasuredWidth() + horizontalSpacing;
            }
        }

        public int getChildCount() {
            return children.size();
        }

        public int getHeight() {
            return height;
        }

    }

}
