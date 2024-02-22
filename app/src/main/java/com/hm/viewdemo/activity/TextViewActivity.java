package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TabStopSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityTextViewBinding;
import com.hm.viewdemo.util.FontsUtil;
import com.hm.viewdemo.util.MyUtils;
import com.hm.viewdemo.util.ScreenUtil;
import com.hm.viewdemo.util.TextViewSpanUtil;
import com.hm.viewdemo.widget.MyTypefaceSpan;
import com.hm.viewdemo.widget.span.SpaceSpan;
import com.hm.viewdemo.widget.span.VerticalAlignTextSpan;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParserException;

/**
 * 测试TextView 设置Span
 */
public class TextViewActivity extends BaseActivity<ActivityTextViewBinding> {

    private final String TAG = getClass().getSimpleName();

    public static void launch(Context context) {
        Intent starter = new Intent(context, TextViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityTextViewBinding createViewBinding() {
        return ActivityTextViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        binding.flContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TextViewActivity.this, "点击了fl_container", Toast.LENGTH_SHORT).show();
            }
        });

        Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "DroidSansMono_1_subfont.ttf");

        if (typeface != null) {
            Log.d(TAG, "initData: typeface!=null");
            binding.tvTestFont.setTypeface(typeface);
        }

        SpannableStringBuilder builder = new SpannableStringBuilder("1234512345=古今山河");
        MyTypefaceSpan myTypefaceSpan = FontsUtil.getInstance(this).getMyNumTypefaceSpan();
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.5f);

        builder.setSpan(myTypefaceSpan, 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(relativeSizeSpan, 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        binding.btnSendInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String charSequence = binding.etInput.getText().toString();
                //tvWithSuffix.setText(charSequence);
                binding.sdtv1.setText(charSequence);
//                TextViewSpanUtil.toggleEllipsize(TextViewActivity.this, tvWithSuffix, 3,
//                        charSequence,
//                        "  详情", R.color.colorAccent, false);
            }
        });

        setTextView1();
        setTextView2();
        setTextView3();
        setTextView4();
        setTextView5();
        setTextView6();
        setTextView7();
        //setTextView8();
        setTextView8_2();
        setTextView9();
        setTextView10();
        setTextView11();

        setTextViewRegex();

        TextViewSpanUtil.toggleEllipsize(this, binding.tvWithSuffix, 3,
                "豫章故郡，洪都新府，襟三江而带五湖，控蛮荆而引瓯越，豫章故郡，豫章故郡，洪都新府，襟三江而带，控蛮荆而引瓯越，",
                "  详情", R.color.colorAccent, false);

        Toast.makeText(this, "hello world", Toast.LENGTH_SHORT).show();

        setTextView12();
        setTextView13();
        setTextView14();
        setTextView15();

        //setEtOne();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnTestLimitTextLength) {

            showLimitResult();
        }
    }

    private void showLimitResult() {
        String s1 = "abcde";
        String s2 = "abcdef";
        String result1 = MyUtils.getTextLimitMaxLength(s1, 5);
        String result2 = MyUtils.getTextLimitMaxLength(s2, 5);

        String s3 = "古道";
        String s4 = "古道西风瘦马";
        String result3 = MyUtils.getTextLimitMaxLength(s3, 5);
        String result4 = MyUtils.getTextLimitMaxLength(s4, 5);

        String s5 = "古道西";
        String s6 = "古道西风瘦马";
        String result5 = MyUtils.getTextLimitMaxLength(s5, 6);
        String result6 = MyUtils.getTextLimitMaxLength(s6, 6);

        String s7 = "a古道";
        String s8 = "a古道西风瘦马";
        String result7 = MyUtils.getTextLimitMaxLength(s7, 5);
        String result8 = MyUtils.getTextLimitMaxLength(s8, 5);

        String s9 = "a古道";
        String s10 = "a古道西风瘦马";
        String result9 = MyUtils.getTextLimitMaxLength(s9, 6);
        String result10 = MyUtils.getTextLimitMaxLength(s10, 6);

        SpannableStringBuilder builder = new SpannableStringBuilder(result1);
        builder.append("\n");
        builder.append(result2);
        builder.append("\n");
        builder.append(result3);
        builder.append("\n");
        builder.append(result4);
        builder.append("\n");
        builder.append(result5);
        builder.append("\n");
        builder.append(result6);
        builder.append("\n");
        builder.append(result7);
        builder.append("\n");
        builder.append(result8);
        builder.append("\n");
        builder.append(result9);
        builder.append("\n");
        builder.append(result10);

        binding.tvLimitTextLengthResult.setText(builder.toString());

    }


    private void setTextView11() {
        SpannableStringBuilder builder = new SpannableStringBuilder("什么情况save6you3fromanything" +
                "什么情况save6you3fromanything什么情况save6you3fromanything什么情况save6you3" +
                "fromanything什么情况save6you3fromanything什么情况save6you3fromanything");
        LeadingMarginSpan leadingMarginSpan = new LeadingMarginSpan.Standard(96, 36);
        builder.setSpan(leadingMarginSpan, 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.textView11.setText(builder);
    }

    private void setTextView10() {
        SpannableStringBuilder builder = new SpannableStringBuilder("什么情况save6you3fromanything");
        MaskFilterSpan embossMaskFilterSpan = new MaskFilterSpan(
                new EmbossMaskFilter(new float[]{3, 3, 3}, 0.5f, 8, 3));
        builder.setSpan(embossMaskFilterSpan, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        MaskFilterSpan blurMaskFilterSpan = new MaskFilterSpan(new BlurMaskFilter(2, BlurMaskFilter.Blur.OUTER));
        builder.setSpan(blurMaskFilterSpan, 6, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        binding.textView10.setText(builder);
    }

    private void setTextView9() {
        SpannableStringBuilder builder = new SpannableStringBuilder("save6 you3 from anything");
        ScaleXSpan scaleXSpan = new ScaleXSpan(3.0F);
        builder.setSpan(scaleXSpan, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        binding.textView9.setText(builder);
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
        binding.textView8.setText(builder);
    }

    private void setTextView8_2() {
        SpannableStringBuilder builder = new SpannableStringBuilder("save8 you6 from anything");
        int pos = builder.toString().indexOf("8");
        int pos1 = builder.toString().indexOf("6");
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        SubscriptSpan subscriptSpan = new SubscriptSpan();
        builder.setSpan(superscriptSpan, pos, pos + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(subscriptSpan, pos1, pos1 + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        binding.textView8.setText(builder);
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
        binding.textView7.setText(builder);
    }

    private void setTextView1() {
        String spannableString = "那边就可以\uD83D\uDE0C\uD83C\uDDF7\uD83C\uDDEA\uD83C"
                + "\uDDE7\uD83C\uDDE9\uD83C\uDDF7\uD83C\uDDEA\uD83C\uDDF1\uD83C\uDDFA\uD83C\uDDF2\uD83C"
                + "\uDDE8\uD83E\uDD79\uD83E\uDD79\uD83D\uDE1C\uD83D\uDE05\\uD83E\\uDD2A\\uD83D\\uDE02\\uD83D"
                + "\\uDE02\\uD83E\\uDD2A\\uD83D\\uDE0E\\uD83E\\uDD78\\uD83D\\uDE02\\uD83E\\uDD29\\uD83D\\uDE02\\uD83E"
                + "\\uDD29\\uD83D\\uDE02\\uD83D\\uDE07\\uD83E\\uDD29\\uD83D\\uDE07\\uD83E\\uDD78\\uD83D\\uDE05\\uD83E"
                + "\\uDD78\\uD83D\\uDE05\\uD83E\\uDD29\\uD83E\\uDD29\\uD83D\\uDE0A\\uD83D\\uDE07\\uD83D\\uDE07\\uD83E"
                + "\\uDD29\\uD83D\\uDE05\\uD83E\\uDD28\\uD83E\\uDD28\\uD83D\\uDE05\\uD83E\\uDD28\\uD83D\\uDE05\\uD83E"
                + "\\uDD28\\uD83D\\uDE05\\uD83E\\uDD79\\uD83E\\uDD28\\uD83E\\uDD79\",\n"
                + "\t\t";
        //ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        //spannableString.setSpan(colorSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //SpannableString spannableString1 = new SpannableString(spannableString);
        //spannableString1.setSpan(colorSpan, 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.textView1.setText(spannableString);
    }

    private void setTextView2() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("暗影IV");
        builder.append("已经开始暴走了");
        builder.append("艰难苦恨繁霜鬓");
        //设置字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        //背景色
        // BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#ff3344"));
        //字体大小
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(ScreenUtil.spToPx(this, 20));
        //设置粗体。斜体
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);

        builder.setSpan(colorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //builder.setSpan(backgroundColorSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(sizeSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(styleSpan, builder.length() - 2, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setSpan(styleSpan1, builder.length() - 5, builder.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.textView2.setText(builder);
    }

    private void setTextView3() {
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        builder.append("11hello暗影IV");
//        builder.append("已经开始暴走了");
//        builder.append("艰难苦恨繁霜鬓,已经开始暴走了艰难苦恨繁霜鬓,已经开始暴走了艰难苦恨繁霜鬓,已经开始暴走了艰难苦恨繁霜鬓,已经开始暴走了艰难苦恨繁霜鬓,已经开始暴走了");
//        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
//        StrikethroughSpan strikethroughSpan1 = new StrikethroughSpan();
//        UnderlineSpan underlineSpan = new UnderlineSpan();
//        //ImageSpan imageSpan = new ImageSpan(this, R.mipmap.ic_launcher);
//        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
//        drawable.setBounds(0, 0, 150, 150);
//        //图片和文字基线对齐
//        ImageSpan imageSpan1 = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BASELINE);
//
//        SpannableString spannableString = new SpannableString(builder);
//
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                Toast.makeText(TextViewActivity.this, "HAAHH 点击TextView", Toast.LENGTH_SHORT).show();
//            }
//        };
//        spannableString.setSpan(strikethroughSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(strikethroughSpan1, 5, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(imageSpan1, 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        spannableString.setSpan(clickableSpan, 9, 19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        //builder.setSpan(clickableSpan, 12, 15, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        textView3.setText(spannableString);
//
//        //设置ClickableSpan后要加上这行代码
//        textView3.setMovementMethod(LinkMovementMethod.getInstance());

        String agreementStr = "暗影IV 100 已经开始暴走了       1000 艰难苦恨繁霜鬓";
        SpannableString spannableString = new SpannableString(agreementStr);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(android.view.View widget) {//
                //这里的判断是为了去掉在点击后字体出现的背景色
                if (widget instanceof TextView) {
                    ((TextView) widget).setHighlightColor(Color.TRANSPARENT);
                }

                Toast.makeText(TextViewActivity.this, "haha", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(TextViewActivity.this, R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        }, 6, agreementStr.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        binding.textView3.setText(spannableString);
        binding.textView3.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setTextView4() {
        String days = "100";
        String mileage = "1000";
        String result = "暗影IV100已经开始暴走了1000艰难苦恨繁霜鬓";
        SpannableStringBuilder builder = new SpannableStringBuilder(result);
        int dayStart = result.indexOf(days);
        int mileageStart = result.indexOf(mileage);
        Log.i(TAG, "setTextView4: dayStart=" + dayStart + ",mileageStart=" + mileageStart);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(2.0F);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        RelativeSizeSpan sizeSpan1 = new RelativeSizeSpan(2.0F);

        builder.setSpan(colorSpan, dayStart, dayStart + days.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(sizeSpan, dayStart, dayStart + days.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        builder.setSpan(colorSpan1, mileageStart, mileageStart + mileage.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(sizeSpan1, mileageStart, mileageStart + mileage.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        binding.textView4.setText(builder);
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
        binding.textView5.setMovementMethod(LinkMovementMethod.getInstance());
        binding.textView5.setText(builder);
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
        binding.textView6.setText(builder);
    }

    private void setTextViewRegex() {
        String s = "再听1000秒，至少可领30金币，";

        Pattern patternSecond = Pattern.compile("[0-9]+秒");
        Pattern patternCoin = Pattern.compile("[0-9]+金币");

        Matcher matcherSecond = patternSecond.matcher(s);
        Matcher matcherCoin = patternCoin.matcher(s);

        String secondText = null;
        String coinText = null;

        if (matcherSecond.find()) {
            secondText = matcherSecond.group();
            Log.i(TAG, "setTextViewRegex: " + secondText);
        }

        if (matcherCoin.find()) {
            coinText = matcherCoin.group();
            Log.i(TAG, "setTextViewRegex: " + coinText);
        }

        int secondTextStartIndex = -1;
        int coinTextStartIndex = -1;

        if (!TextUtils.isEmpty(secondText)) {
            secondTextStartIndex = s.indexOf(secondText);
        }
        if (!TextUtils.isEmpty(coinText)) {
            coinTextStartIndex = s.indexOf(coinText);
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(s);

        //设置字体颜色
        ForegroundColorSpan secondColorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        //字体大小
        AbsoluteSizeSpan secondSizeSpan = new AbsoluteSizeSpan(ScreenUtil.spToPx(this, 16));

        //设置字体颜色
        ForegroundColorSpan coinColorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        //字体大小
        AbsoluteSizeSpan coinSizeSpan = new AbsoluteSizeSpan(ScreenUtil.spToPx(this, 16));

        if (secondTextStartIndex != -1) {
            builder.setSpan(secondColorSpan, secondTextStartIndex, secondTextStartIndex + secondText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //去掉秒字
            builder.setSpan(secondSizeSpan, secondTextStartIndex, secondTextStartIndex + secondText.length() - 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (coinTextStartIndex != -1) {
            builder.setSpan(coinColorSpan, coinTextStartIndex, coinTextStartIndex + coinText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //去掉金币两个字
            builder.setSpan(coinSizeSpan, coinTextStartIndex, coinTextStartIndex + coinText.length() - 2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //设置粗体。斜体
        //StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        //StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);

        //builder.setSpan(coinColorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //builder.setSpan(backgroundColorSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //builder.setSpan(coinSizeSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //builder.setSpan(styleSpan, builder.length() - 2, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //builder.setSpan(styleSpan1, builder.length() - 5, builder.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvRegex.setText(builder);
    }

    private void setTextView12() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("念去去 · 千里烟波暮霭沉沉楚天阔");
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2.0f);
        builder.setSpan(relativeSizeSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SubscriptSpan subscriptSpan = new SubscriptSpan();
        builder.setSpan(subscriptSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        binding.textView12.setText(builder);
    }

    private void setTextView13() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("念去去 · 千里烟波暮霭沉沉楚天阔");

        SubscriptSpan subscriptSpan = new SubscriptSpan();
        builder.setSpan(subscriptSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2.0f);
        builder.setSpan(relativeSizeSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        binding.textView13.setText(builder);
    }

    private void setTextView14() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("念去去 · 千里烟波暮霭沉沉楚天阔");

        //RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.3f);
        //builder.setSpan(relativeSizeSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        VerticalAlignTextSpan verticalAlignTextSpan = new VerticalAlignTextSpan(ScreenUtil.spToPx(this, 24));
        builder.setSpan(verticalAlignTextSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        binding.textView14.setText(builder);
    }

    private void setTextView15() {
        String days = "100";
        String mileage = "1000";
        //把这三个字符替换成10dp的空白
        String beReplace = "被替代";

        String result = "暗影IV100已经开始暴走了1000艰难苦恨繁霜鬓被替代哈哈";
        SpannableStringBuilder builder = new SpannableStringBuilder(result);
        int dayStart = result.indexOf(days);
        int mileageStart = result.indexOf(mileage);
        int replaceStart = result.indexOf(beReplace);
        Log.i(TAG, "setTextView4: dayStart=" + dayStart + ",mileageStart=" + mileageStart);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(2.0F);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        RelativeSizeSpan sizeSpan1 = new RelativeSizeSpan(2.0F);

        SpaceSpan spaceSpan = new SpaceSpan(ScreenUtil.dpToPx(this, 10));

        builder.setSpan(spaceSpan, replaceStart, replaceStart + beReplace.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        //builder.setSpan(colorSpan, dayStart, dayStart + days.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //builder.setSpan(sizeSpan, dayStart, dayStart + days.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        //builder.setSpan(colorSpan1, mileageStart, mileageStart + mileage.length(), Spannable
        // .SPAN_INCLUSIVE_INCLUSIVE);
        //builder.setSpan(sizeSpan1, mileageStart, mileageStart + mileage.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        binding.textView15.setText(builder);
    }

    /**
     * 测试EditText 设置 text 长度超限的问题。
     */
    private void setEtOne() {
        String text = "1234567890123";//长度是13，在xml文件中限制了etOne的maxLength为12
        if (text.length() > 12) {
            text = text.substring(0, 12);
        }
        binding.etOne.setText(text);
        binding.etOne.setSelection(text.length());

    }

}
