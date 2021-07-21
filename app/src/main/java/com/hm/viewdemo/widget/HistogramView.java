package com.hm.viewdemo.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/7/11.
 * 直方图,就用两个ImageView来显示
 */
public class HistogramView extends FrameLayout {

    private final String TAG = getClass().getSimpleName();
    private View viewForgound;
    private int totalHeight;

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_histogram_view, this);
        viewForgound = view.findViewById(R.id.img_forground);
    }

    /**
     * 设置前景色的高度
     */
    public void setForgroundHeight(final float percent) {
        post(new Runnable() {
            @Override
            public void run() {
                if (totalHeight <= 0) {
                    totalHeight = getHeight();
                    Log.e(TAG, "totalHeight=" + totalHeight);
                    int height = (int) (totalHeight * percent);
                    if (height > 0) {
                        ViewGroup.LayoutParams params = viewForgound.getLayoutParams();
                        params.height = height;
                        viewForgound.setLayoutParams(params);
                    }
                }else {
                    Log.e(TAG, "totalHeight!=0,=" + totalHeight);
                }
            }
        });

    }

    public void funx(View.OnClickListener clickListener){
        View view=new ViewDragLayout(getContext());
        view.setOnClickListener(clickListener);
    }

}
