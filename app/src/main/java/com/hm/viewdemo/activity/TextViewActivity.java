package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TabStopSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.util.ScreenUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import butterknife.BindView;

/**
 * 测试TextView 设置Span
 */
public class TextViewActivity extends BaseActivity {


    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.text_view_8)
    TextView textView8;
    @BindView(R.id.text_view_9)
    TextView textView9;
    @BindView(R.id.text_view_10)
    TextView textView10;
    @BindView(R.id.text_view_11)
    TextView textView11;

    public static void launch(Context context) {
        Intent starter = new Intent(context, TextViewActivity.class);
        context.startActivity(starter);
    }

    @BindView(R.id.text_view_1)
    TextView textView1;
    @BindView(R.id.text_view_2)
    TextView textView2;
    @BindView(R.id.text_view_3)
    TextView textView3;
    @BindView(R.id.text_view_4)
    TextView textView4;
    @BindView(R.id.text_view_5)
    TextView textView5;
    @BindView(R.id.text_view_6)
    TextView textView6;
    @BindView(R.id.text_view_7)
    TextView textView7;

    @Override
    protected int bindLayout() {
        return R.layout.activity_text_view;
    }

    @Override
    protected void initData() {
        setTextView1();
        setTextView2();
        setTextView3();
        setTextView4();
        setTextView5();
        setTextView6();
        setTextView7();
        setTextView8();
        setTextView9();
        setTextView10();
        setTextView11();

        Toast.makeText(this, "hello world", Toast.LENGTH_SHORT).show();

    }

    private void setTextView11() {
        SpannableStringBuilder builder = new SpannableStringBuilder("什么情况save6you3fromanything" +
                "什么情况save6you3fromanything什么情况save6you3fromanything什么情况save6you3" +
                "fromanything什么情况save6you3fromanything什么情况save6you3fromanything");
        LeadingMarginSpan leadingMarginSpan = new LeadingMarginSpan.Standard(96, 36);
        builder.setSpan(leadingMarginSpan, 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView11.setText(builder);
    }

    private void setTextView10() {
        SpannableStringBuilder builder = new SpannableStringBuilder("什么情况save6you3fromanything");
        MaskFilterSpan embossMaskFilterSpan = new MaskFilterSpan(new EmbossMaskFilter(new float[]{3, 3, 3}, 0.5f, 8, 3));
        builder.setSpan(embossMaskFilterSpan, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        MaskFilterSpan blurMaskFilterSpan = new MaskFilterSpan(new BlurMaskFilter(2, BlurMaskFilter.Blur.OUTER));
        builder.setSpan(blurMaskFilterSpan, 6, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView10.setText(builder);
    }

    private void setTextView9() {
        SpannableStringBuilder builder = new SpannableStringBuilder("save6 you3 from anything");
        ScaleXSpan scaleXSpan = new ScaleXSpan(3.0F);
        builder.setSpan(scaleXSpan, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView9.setText(builder);
    }

    private void setTextView8() {
        SpannableStringBuilder builder = new SpannableStringBuilder("save6 you3 from anything");
        Parcel parcel = Parcel.obtain();
        parcel.writeInt(6);
        int pos = builder.toString().indexOf("6");
        int pos1 = builder.toString().indexOf("3");
        SuperscriptSpan superscriptSpan = new SuperscriptSpan(parcel);
        parcel.writeInt(3);
        SubscriptSpan subscriptSpan = new SubscriptSpan(parcel);
        builder.setSpan(superscriptSpan, pos, pos + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(subscriptSpan, pos1, pos1 + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        parcel.recycle();
        textView8.setText(builder);
    }

    private void setTextView7() {
        String string = new String("save you from anything");
        String[] strings = string.split(" ");
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (String s : strings) {
            builder.append("\t").append(s).append(" ");
            builder.append("\n");
        }
        TabStopSpan tabStopSpan = new TabStopSpan.Standard(26);
        builder.setSpan(tabStopSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView7.setText(builder);
    }

    private void setTextView1() {
        SpannableString spannableString = new SpannableString("暗影IV已经开始暴走了");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        spannableString.setSpan(colorSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spannableString1 = new SpannableString(spannableString);
        spannableString1.setSpan(colorSpan, 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView1.setText(spannableString1);
    }

    private void setTextView2() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("暗影IV");
        builder.append("已经开始暴走了");
        builder.append("艰难苦恨繁霜鬓");
        //设置字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        //背景色
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#ff3344"));
        //字体大小
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(ScreenUtil.spToPx(this, 20));
        //设置粗体。斜体
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        builder.setSpan(colorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(backgroundColorSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(sizeSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(styleSpan, builder.length() - 2, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView2.setText(builder);
    }

    private void setTextView3() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("暗影IV");
        builder.append("已经开始暴走了");
        builder.append("艰难苦恨繁霜鬓");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        StrikethroughSpan strikethroughSpan1 = new StrikethroughSpan();
        UnderlineSpan underlineSpan = new UnderlineSpan();
        //ImageSpan imageSpan = new ImageSpan(this, R.mipmap.ic_launcher);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan1 = new ImageSpan(drawable);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(TextViewActivity.this, "点击TextView", Toast.LENGTH_SHORT).show();
            }
        };
        builder.setSpan(strikethroughSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(strikethroughSpan1, 5, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(imageSpan1, 9, 10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(clickableSpan, 12, 15, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        textView3.setText(builder);

        //设置ClickableSpan后要加上这行代码
        textView3.setMovementMethod(LinkMovementMethod.getInstance());

        String agreementStr = "暗影IV 100 已经开始暴走了 1000 艰难苦恨繁霜鬓";
        SpannableString spannableString = new SpannableString(agreementStr);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(android.view.View widget) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(TextViewActivity.this, R.color.colorPrimary));
                ds.setUnderlineText(true);
            }
        }, 6, agreementStr.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView3.setText(spannableString);
        textView3.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setTextView4() {
        String days = "100";
        String mileage = "1000";
        String result = "暗影IV 100 已经开始暴走了 1000 艰难苦恨繁霜鬓";
        SpannableStringBuilder builder = new SpannableStringBuilder(result);
        int dayStart = result.indexOf(days);
        int mileageStart = result.indexOf(mileage);
        Log.d(TAG, "setTextView4: dayStart=" + dayStart + ",mileageStart=" + mileageStart);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(2.0F);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        RelativeSizeSpan sizeSpan1 = new RelativeSizeSpan(2.0F);

        builder.setSpan(colorSpan, dayStart, dayStart + days.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(sizeSpan, dayStart, dayStart + days.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        builder.setSpan(colorSpan1, mileageStart, mileageStart + mileage.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(sizeSpan1, mileageStart, mileageStart + mileage.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView4.setText(builder);
    }
    /*private void setTextView4() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("暗影IV");
        builder.append("已经开始暴走了");
        builder.append("艰难苦恨繁霜鬓");
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(3.0F);
        MaskFilterSpan maskFilterSpan = new MaskFilterSpan(new BlurMaskFilter(2.0F, BlurMaskFilter.Blur.NORMAL));
        //builder.setSpan(sizeSpan, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(sizeSpan, 5, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView4.setData(builder);
    }*/

    private void setTextView5() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("暗影IV已经开始暴走了艰难苦恨繁霜鬓");
        URLSpan urlSpan = new URLSpan("http://blog.csdn.net/leilifengxingmw");
        //MaskFilterSpan maskFilterSpan = new MaskFilterSpan(new BlurMaskFilter(2.0F, BlurMaskFilter.Blur.NORMAL));
        builder.setSpan(urlSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
        textView5.setMovementMethod(LinkMovementMethod.getInstance());
        textView5.setText(builder);
    }

    private void setTextView6() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("什么情况IV已经开始暴走了艰难苦恨繁霜鬓");
        ColorStateList colorStateList = null;
        try {
            colorStateList = ColorStateList.createFromXml(getResources(), getResources().getXml(R.color.color_list));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan("serif", Typeface.BOLD_ITALIC,
                getResources().getDimensionPixelSize(R.dimen.default_size), colorStateList, colorStateList);
        // TextAppearanceSpan textAppearanceSpan1 = new TextAppearanceSpan(this, R.style.MyTextViewStyle);
        builder.setSpan(textAppearanceSpan, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(textAppearanceSpan, 6, 8, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView6.setText(builder);
    }

}
