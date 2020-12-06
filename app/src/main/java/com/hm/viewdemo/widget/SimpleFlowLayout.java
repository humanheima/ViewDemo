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
import android.widget.TextView;

import com.hm.viewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmw on 2018/12/2.
 * Desc:简单流式布局
 * 1. 控制显示的最大行数
 * 2. 控制显示标签的最大个数
 * 3. 如果只限制一行，但是没限制item的数量，那么这个时候应该只添加一行
 * <p>
 * 测试数据
 * maxLines=1 && maxCount=1
 * <p>
 * maxLines=2 && maxCount=1
 * <p>
 * maxLines=1 && maxCount=2
 * <p>
 * maxLines=2 && maxCount=2
 * <p>
 * maxLines=20 && maxCount=20
 * <p>
 * <p>
 * 参考链接：https://blog.csdn.net/kong_gu_you_lan/article/details/52786219
 */
public class SimpleFlowLayout extends ViewGroup {

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
     * 最大可以显示多少行
     */
    private int maxLines;

    /**
     * 限制最大的标签数量,
     */
    private int maxCount;

    public SimpleFlowLayout(Context context) {
        this(context, null);
    }

    public SimpleFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleFlowLayout);
        horizontalSpacing = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_sfl_horizontal_spacing, dp2px(12));
        verticalSpacing = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_sfl_vertical_spacing, dp2px(12));
        textSize = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_sfl_text_size, sp2px(14));
        textColor = ta.getColor(R.styleable.SimpleFlowLayout_sfl_text_color, Color.BLACK);
        backgroundResource = ta.getResourceId(R.styleable.SimpleFlowLayout_sfl_background_resource, R.drawable.bg_flow_layout_item);
        textPaddingH = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_sfl_text_horizontal_padding, dp2px(12));
        textPaddingV = ta.getDimensionPixelSize(R.styleable.SimpleFlowLayout_sfl_text_vertical_padding, dp2px(8));
        maxLines = ta.getInteger(R.styleable.SimpleFlowLayout_sfl_max_lines, Integer.MAX_VALUE);
        //最少一行
        if (maxLines < 1) {
            maxLines = 1;
        }
        maxCount = ta.getInteger(R.styleable.SimpleFlowLayout_sfl_max_count, Integer.MAX_VALUE);
        if (maxCount < 1) {
            maxCount = 1;
        }
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (lines.size() > maxLines) {
            return;
        }
        int addedChildCount = 0;
        restoreLine();

        //实际可用宽高
        int width = MeasureSpec.getSize(widthMeasureSpec - getPaddingLeft() - getPaddingRight());
        int height = MeasureSpec.getSize(heightMeasureSpec - getPaddingTop() - getPaddingBottom());
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //测量所有的child
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            int measureWidth = child.getMeasuredWidth();
            lineSize += measureWidth;
            // 如果使用的宽度小于可用的宽度，这时候child能够添加到当前的行上
            if (lineSize <= width) {
                line.addChild(child);
                lineSize += horizontalSpacing;
                addedChildCount++;
            } else {
                addLine();

                /**
                 * 如果没有达到限制的最大行数，则换行继续添加
                 */
                if (lines.size() < maxLines) {
                    newLine();
                    line.addChild(child);
                    lineSize += child.getMeasuredWidth();
                    lineSize += horizontalSpacing;
                    addedChildCount++;
                } else {
                    break;
                }
            }
        }

        Log.i(TAG, "onMeasure: addedChildCount=" + addedChildCount + ",getChildCount()=" + getChildCount());

        // 把最后一行记录到集合中
        addLine();

        int totalHeight = 0;
        // 把所有行的高度加上
        for (int i = 0; i < lines.size(); i++) {
            totalHeight += lines.get(i).getHeight();
        }
        Log.e(TAG, "lines.size()=" + lines.size());
        // 加上行的竖直间距
        totalHeight += verticalSpacing * (lines.size() - 1);

        totalHeight += getPaddingTop();
        totalHeight += getPaddingBottom();

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.makeMeasureSpec(totalHeight, heightMeasureSpec));
    }

    private void addLine() {
        if (line != null && !lines.contains(line) && lines.size() < maxLines) {
            lines.add(line);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.layout(left, top);
            top = top + line.getHeight() + verticalSpacing;
        }
    }

    private void newLine() {
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
        List<String> list = new ArrayList<>(1);
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
        List<String> list = new ArrayList<>(1);
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
            Log.i(TAG, "addViews: " + getChildCount() + "," + lines.size());
            if (getChildCount() >= maxCount) {
                break;
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
            Log.i(TAG, "layout: children.size()=" + children.size());
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
