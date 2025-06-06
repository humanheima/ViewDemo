package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.EmbossMaskFilter
import android.graphics.Typeface
import android.os.Build
import android.os.Parcel
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.DynamicDrawableSpan
import android.text.style.DynamicDrawableSpan.ALIGN_CENTER
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.LeadingMarginSpan
import android.text.style.MaskFilterSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ScaleXSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TabStopSpan
import android.text.style.TextAppearanceSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityDynamicLayoutTestBinding
import com.hm.viewdemo.dp2px
import com.hm.viewdemo.util.FontsUtil
import com.hm.viewdemo.util.MyUtils
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.util.TextViewSpanUtil
import com.hm.viewdemo.widget.span.SpaceSpan
import com.hm.viewdemo.widget.span.VerticalAlignTextSpan
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.regex.Pattern


/**
 * 测试TextView 设置Span
 */
class DynamicLayoutTestActivity : BaseActivity<ActivityDynamicLayoutTestBinding>() {

    var yourText: String =
        "    我所认识的中国，http://www.baidu.com 强大、友好 --习大大。@奥特曼 “一带一路”经济带带动了沿线国家的经济发展，促进我国与他国的友好往来和贸易发展，可谓“双赢”，Github地址。 自古以来，中国以和平、友好的面孔示人。汉武帝派张骞出使西域，开辟丝绸之路，增进与西域各国的友好往来。http://www.baidu.com 胡麻、胡豆、香料等食材也随之传入中国，汇集于中华美食。@RNG 漠漠古道，驼铃阵阵，这条路奠定了“一带一路”的基础，让世界认识了中国。"

    override fun createViewBinding(): ActivityDynamicLayoutTestBinding {
        return ActivityDynamicLayoutTestBinding.inflate(layoutInflater)
    }

    override fun initData() {
        val typeface = Typeface.createFromAsset(resources.assets, "DroidSansMono_1_subfont.ttf")

        if (typeface != null) {
            Log.d(TAG, "initData: typeface!=null")
            binding.tvTestFont.typeface = typeface
        }

        val builder = SpannableStringBuilder("1234512345=古今山河")
        val myTypefaceSpan = FontsUtil.getInstance(this).myNumTypefaceSpan
        val relativeSizeSpan = RelativeSizeSpan(1.5f)

        builder.setSpan(myTypefaceSpan, 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        builder.setSpan(relativeSizeSpan, 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        binding.btnSendInput.setOnClickListener {
            val charSequence = binding.etInput.text.toString()
            //tvWithSuffix.setText(charSequence);
            //binding.expandTextView.setText(charSequence)
            //binding.sdtv1.setText(charSequence)
            setTextWithEllipsize(
                binding.tvWithSuffix,
                3,
                charSequence,
                "详情",
                R.color.colorAccent,
                R.drawable.ic_text_right_arrow
            )
            binding.expandTextView.text = charSequence
        }

        binding.ep09.setContent(yourText)
        binding.ep09.isNeedAlwaysShowRight = false

        setTextView1()
        setTextView2()
        setTextView3()
        setTextView4()
        setTextView5()
        setTextView6()
        setTextView7()
        //setTextView8();
        setTextView8_2()
        setTextView9()
        setTextView10()
        setTextView11()

        setTextViewRegex()

        TextViewSpanUtil.toggleEllipsize(
            this, binding.tvWithSuffix, 3,
            "在干嘛呢豫章故郡，洪都新府，襟三江而带五湖，控蛮荆而引瓯越，豫章故郡，豫章故郡，洪都新府，襟三江而带，控蛮荆而引瓯越，",
            "  详情", R.color.colorAccent, false
        )

        Toast.makeText(this, "hello world", Toast.LENGTH_SHORT).show()

        setTextView12()
        setTextView13()
        setTextView14()
        setTextView15()

        //setEtOne();

        setBracketTextColor(
            binding.tvTestRegularExpression,
            "（正在思考）测(）试限制（)文字长度，(什么情况)英文算一个字符，中文算两个字符（可以的）"
        )
    }

    /**
     * @param textView textview
     * @param minLines 最少的行数
     * @param endText  结尾文字
     * @param endColor 结尾文字颜色id
     * @param endResId 结尾图标
     */
    private fun setTextWithEllipsize(
        textView: TextView, minLines: Int, originContent: String?,
        endText: String, endColor: Int, endResId: Int
    ) {
        if (originContent.isNullOrEmpty()) {
            return
        }
        val availableWidth = textView.width - textView.paddingLeft - textView.paddingRight
        //留出一段距离
        val threeEmptyChar = "   "
        val paint = textView.paint
        Log.e(
            TAG,
            "setTextWithEllipsize:threeEmptyChar width =  ${paint.measureText(threeEmptyChar)}"
        )
        val moreTextWidth = paint.measureText(endText)
        Log.d(
            TAG,
            "onGlobalLayout: moreTextWidth = $moreTextWidth"
        )
        //三行完整高度，如果正好能展示下，就展示下。
        val availableTextWidth: Float = (availableWidth * minLines).toFloat()

        val originTextWidth = paint.measureText(originContent)

        Log.e(
            TAG,
            "setTextWithEllipsize: availableTextWidth = $availableTextWidth  originTextWidth = $originTextWidth"
        )
        if (originTextWidth <= availableTextWidth) {
            textView.text = originContent
        } else {
            Log.e(TAG, "setTextWithEllipsize: 三行展示不下")
            /** 三行展示不下
             * * 获取endText + 三个空格的宽度： endTextWidth
             * * 图标的宽度：dp14
             * * 剩余展示文字的宽度： availableTextWidth - endTextWidth - dp14
             */
            val endTextWidth = paint.measureText("$threeEmptyChar$endText")

            //图标宽度
            val dp14 = 14.dp2px(this)

            val dp8 = 8.dp2px(this)

            val drawable = ContextCompat.getDrawable(this, endResId)

            var imageSpan: ImageSpan? = null
            drawable?.let {
                it.setBounds(0, 0, dp14, dp14)
                // 创建 ImageSpan
                imageSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ImageSpan(drawable, ALIGN_CENTER)
                } else {
                    ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                }
            }

            val displayWidth = availableTextWidth - endTextWidth - dp14 - dp8

            Log.e(
                TAG,
                "setTextWithEllipsize: endTextWidth = $endTextWidth  dp14 = $dp14 availableTextWidth = $availableTextWidth  displayWidth = $displayWidth"
            )

            val ellipsizeStr = TextUtils.ellipsize(
                originContent,
                paint,
                displayWidth,
                TextUtils.TruncateAt.END
            )
            //用来ImageSpan占位
            val imageSpanPlaceHolderString = " "
            val finalText = "$ellipsizeStr$endText$imageSpanPlaceHolderString"

            val spannableString = SpannableString(finalText)

            val endTextIndex = finalText.length - 1
            val start = endTextIndex - endText.length
            spannableString.setSpan(
                ForegroundColorSpan(endColor), start,
                endTextIndex,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )

            spannableString.setSpan(
                imageSpan,
                endTextIndex,
                finalText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            textView.movementMethod = LinkMovementMethod.getInstance()
            textView.text = spannableString
        }
    }


    fun onClick(v: View) {
        if (v.id == R.id.btnTestLimitTextLength) {
            showLimitResult()
        }
    }

    private fun showLimitResult() {
        val s1 = "abcde"
        val s2 = "abcdef"
        val result1 = MyUtils.getTextLimitMaxLength(s1, 5)
        val result2 = MyUtils.getTextLimitMaxLength(s2, 5)

        val s3 = "古道"
        val s4 = "古道西风瘦马"
        val result3 = MyUtils.getTextLimitMaxLength(s3, 5)
        val result4 = MyUtils.getTextLimitMaxLength(s4, 5)

        val s5 = "古道西"
        val s6 = "古道西风瘦马"
        val result5 = MyUtils.getTextLimitMaxLength(s5, 6)
        val result6 = MyUtils.getTextLimitMaxLength(s6, 6)

        val s7 = "a古道"
        val s8 = "a古道西风瘦马"
        val result7 = MyUtils.getTextLimitMaxLength(s7, 5)
        val result8 = MyUtils.getTextLimitMaxLength(s8, 5)

        val s9 = "a古道"
        val s10 = "a古道西风瘦马"
        val result9 = MyUtils.getTextLimitMaxLength(s9, 6)
        val result10 = MyUtils.getTextLimitMaxLength(s10, 6)

        val builder = SpannableStringBuilder(result1)
        builder.append("\n")
        builder.append(result2)
        builder.append("\n")
        builder.append(result3)
        builder.append("\n")
        builder.append(result4)
        builder.append("\n")
        builder.append(result5)
        builder.append("\n")
        builder.append(result6)
        builder.append("\n")
        builder.append(result7)
        builder.append("\n")
        builder.append(result8)
        builder.append("\n")
        builder.append(result9)
        builder.append("\n")
        builder.append(result10)

        binding!!.tvLimitTextLengthResult.text = builder.toString()
    }


    private fun setTextView11() {
        val builder = SpannableStringBuilder(
            "什么情况save6you3fromanything" +
                    "什么情况save6you3fromanything什么情况save6you3fromanything什么情况save6you3" +
                    "fromanything什么情况save6you3fromanything什么情况save6you3fromanything"
        )
        val leadingMarginSpan: LeadingMarginSpan = LeadingMarginSpan.Standard(96, 36)
        builder.setSpan(leadingMarginSpan, 0, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding!!.textView11.text = builder
    }

    private fun setTextView10() {
        val builder = SpannableStringBuilder("什么情况save6you3fromanything")
        val embossMaskFilterSpan = MaskFilterSpan(
            EmbossMaskFilter(floatArrayOf(3f, 3f, 3f), 0.5f, 8f, 3f)
        )
        builder.setSpan(embossMaskFilterSpan, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        val blurMaskFilterSpan = MaskFilterSpan(BlurMaskFilter(2f, BlurMaskFilter.Blur.OUTER))
        builder.setSpan(blurMaskFilterSpan, 6, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding!!.textView10.text = builder
    }

    private fun setTextView9() {
        val builder = SpannableStringBuilder("save6 you3 from anything")
        val scaleXSpan = ScaleXSpan(3.0f)
        builder.setSpan(scaleXSpan, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding!!.textView9.text = builder
    }

    private fun setTextView8() {
        val builder = SpannableStringBuilder("save6 you3 from anything")
        val parcel = Parcel.obtain()
        parcel.writeInt(6)
        val pos = builder.toString().indexOf("6")
        val pos1 = builder.toString().indexOf("3")
        val superscriptSpan = SuperscriptSpan(parcel)
        parcel.writeInt(3)
        val subscriptSpan = SubscriptSpan(parcel)
        builder.setSpan(superscriptSpan, pos, pos + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        builder.setSpan(subscriptSpan, pos1, pos1 + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        parcel.recycle()
        binding!!.textView8.text = builder
    }

    private fun setTextView8_2() {
        val builder = SpannableStringBuilder("save8 you6 from anything")
        val pos = builder.toString().indexOf("8")
        val pos1 = builder.toString().indexOf("6")
        val superscriptSpan = SuperscriptSpan()
        val subscriptSpan = SubscriptSpan()
        builder.setSpan(superscriptSpan, pos, pos + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        builder.setSpan(subscriptSpan, pos1, pos1 + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding!!.textView8.text = builder
    }

    private fun setTextView7() {
        val string = "save you from anything"
        val strings = string.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val builder = SpannableStringBuilder()
        for (s in strings) {
            builder.append("\t").append(s).append(" ")
            builder.append("\n")
        }
        val tabStopSpan: TabStopSpan = TabStopSpan.Standard(26)
        builder.setSpan(tabStopSpan, 0, builder.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        binding!!.textView7.text = builder
    }

    /**
     * 正则表达式，匹配括号包括的内容设置不同的颜色。支持中文括号和英文括号
     * 1.支持全英文括号 ()
     * 2.支持全中文括号 ()
     * 3.支持中英文混合，左英文右中文 (），左中文右英文（)
     */
    private fun setBracketTextColor(textView: TextView, text: String) {
        val spannableString = SpannableString(text)
        // 定义正则表达式
        val pattern = "(?:\\([^()]*\\)|\\（[^（）]*\\）|\\([^（）]*\\）|\\（[^（）]*\\))".toRegex()

        // 查找所有匹配的括号内容
        pattern.findAll(text).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1
            // 设置括号及其内容的颜色（这里使用蓝色作为示例）
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        textView.context,
                        android.R.color.holo_blue_dark
                    )
                ),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // 应用到TextView
        textView.text = spannableString
    }

    private fun setTextView1() {
        val stringBuilder = SpannableStringBuilder("那边就可以")
        val colorSpan = ForegroundColorSpan(Color.parseColor("#009ad6"));
        //stringBuilder.setSpan(colorSpan, 0, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(colorSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.insert(0, "可以")
        //SpannableString spannableString1 = new SpannableString(spannableString);
        //spannableString1.setSpan(colorSpan, 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.textView1.text = stringBuilder
    }

    private fun setTextView2() {
        val builder = SpannableStringBuilder()
        builder.append("暗影IV")
        builder.append("已经开始暴走了")
        builder.append("艰难苦恨繁霜鬓")
        //设置字体颜色
        val colorSpan = ForegroundColorSpan(Color.parseColor("#009ad6"))
        //背景色
        // BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#ff3344"));
        //字体大小
        val sizeSpan = AbsoluteSizeSpan(ScreenUtil.spToPx(this, 20))
        //设置粗体。斜体
        val styleSpan = StyleSpan(Typeface.BOLD_ITALIC)
        val styleSpan1 = StyleSpan(Typeface.BOLD)

        builder.setSpan(colorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        //builder.setSpan(backgroundColorSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(sizeSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(
            styleSpan,
            builder.length - 2,
            builder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        builder.setSpan(
            styleSpan1,
            builder.length - 5,
            builder.length - 3,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.textView2.text = builder
    }

    private fun setTextView3() {
        val builder = SpannableStringBuilder()
        //builder.append("11hello暗影IV");
        builder.append("已经开始暴走了")
        builder.append("艰难苦恨繁霜鬓,已经开始暴走了艰难苦恨繁霜鬓,已经开始暴走了艰难苦恨繁霜鬓,已经开始暴走了艰难苦恨繁霜鬓,已经开始暴走了艰难苦恨繁霜鬓,已经开始暴走了")
        //        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
//        StrikethroughSpan strikethroughSpan1 = new StrikethroughSpan();
//        UnderlineSpan underlineSpan = new UnderlineSpan();
        //ImageSpan imageSpan = new ImageSpan(this, R.mipmap.ic_launcher);
        val drawable = resources.getDrawable(R.mipmap.ic_launcher)
        //drawable.setBounds(0, 0, 42, 42);
        //图片和文字基线对齐
        val imageSpan1 = ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BASELINE)

        val spannableString = SpannableString(builder)

        //        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                Toast.makeText(TextViewActivity.this, "HAAHH 点击TextView", Toast.LENGTH_SHORT).show();
//            }
//        };
        //spannableString.setSpan(strikethroughSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(strikethroughSpan1, 5, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(imageSpan1, 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        //spannableString.setSpan(clickableSpan, 9, 19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        //builder.setSpan(clickableSpan, 12, 15, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        binding!!.textView3.text = spannableString

        //设置ClickableSpan后要加上这行代码
        //binding.textView3.setMovementMethod(LinkMovementMethod.getInstance());

//        String agreementStr = "暗影IV 100 已经开始暴走了       1000 艰难苦恨繁霜鬓";
//        SpannableString spannableString = new SpannableString(agreementStr);
//        spannableString.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(android.view.View widget) {//
//                //这里的判断是为了去掉在点击后字体出现的背景色
//                if (widget instanceof TextView) {
//                    ((TextView) widget).setHighlightColor(Color.TRANSPARENT);
//                }
//
//                Toast.makeText(TextViewActivity.this, "haha", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                ds.setColor(ContextCompat.getColor(TextViewActivity.this, R.color.colorPrimary));
//                ds.setUnderlineText(false);
//            }
//        }, 6, agreementStr.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//        binding.textView3.setText(spannableString);
//        binding.textView3.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private fun setTextView4() {
        val days = "100"
        val mileage = "1000"
        val result = "暗影IV100已经开始暴走了1000艰难苦恨繁霜鬓"
        val builder = SpannableStringBuilder(result)
        val dayStart = result.indexOf(days)
        val mileageStart = result.indexOf(mileage)
        Log.i(TAG, "setTextView4: dayStart=$dayStart,mileageStart=$mileageStart")

        val colorSpan = ForegroundColorSpan(Color.parseColor("#009ad6"))
        val sizeSpan = RelativeSizeSpan(2.0f)
        val colorSpan1 = ForegroundColorSpan(Color.parseColor("#009ad6"))
        val sizeSpan1 = RelativeSizeSpan(2.0f)

        builder.setSpan(
            colorSpan,
            dayStart,
            dayStart + days.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        builder.setSpan(
            sizeSpan,
            dayStart,
            dayStart + days.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        builder.setSpan(
            colorSpan1,
            mileageStart,
            mileageStart + mileage.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        builder.setSpan(
            sizeSpan1,
            mileageStart,
            mileageStart + mileage.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        binding!!.textView4.text = builder
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
    private fun setTextView5() {
        val builder = SpannableStringBuilder()
        builder.append("暗影IV已经开始暴走了艰难苦恨繁霜鬓")
        val urlSpan = URLSpan("http://blog.csdn.net/leilifengxingmw")
        //MaskFilterSpan maskFilterSpan = new MaskFilterSpan(new BlurMaskFilter(2.0F, BlurMaskFilter.Blur.NORMAL));
        builder.setSpan(urlSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
        binding!!.textView5.movementMethod = LinkMovementMethod.getInstance()
        binding!!.textView5.text = builder
    }

    private fun setTextView6() {
        val builder = SpannableStringBuilder()
        builder.append("什么情况IV已经开始暴走了艰难苦恨繁霜鬓")
        var colorStateList: ColorStateList? = null
        try {
            colorStateList =
                ColorStateList.createFromXml(resources, resources.getXml(R.color.color_list))
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val textAppearanceSpan = TextAppearanceSpan(
            "serif", Typeface.BOLD_ITALIC,
            resources.getDimensionPixelSize(R.dimen.default_size), colorStateList, colorStateList
        )
        // TextAppearanceSpan textAppearanceSpan1 = new TextAppearanceSpan(this, R.style.MyTextViewStyle);
        builder.setSpan(textAppearanceSpan, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        builder.setSpan(textAppearanceSpan, 6, 8, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        binding!!.textView6.text = builder
    }

    private fun setTextViewRegex() {
        val s = "再听1000秒，至少可领30金币，"

        val patternSecond = Pattern.compile("[0-9]+秒")
        val patternCoin = Pattern.compile("[0-9]+金币")

        val matcherSecond = patternSecond.matcher(s)
        val matcherCoin = patternCoin.matcher(s)

        var secondText: String? = null
        var coinText: String? = null

        if (matcherSecond.find()) {
            secondText = matcherSecond.group()
            Log.i(TAG, "setTextViewRegex: $secondText")
        }

        if (matcherCoin.find()) {
            coinText = matcherCoin.group()
            Log.i(TAG, "setTextViewRegex: $coinText")
        }

        var secondTextStartIndex = -1
        var coinTextStartIndex = -1

        if (!TextUtils.isEmpty(secondText)) {
            secondTextStartIndex = s.indexOf(secondText!!)
        }
        if (!TextUtils.isEmpty(coinText)) {
            coinTextStartIndex = s.indexOf(coinText!!)
        }

        val builder = SpannableStringBuilder(s)

        //设置字体颜色
        val secondColorSpan = ForegroundColorSpan(Color.parseColor("#009ad6"))
        //字体大小
        val secondSizeSpan = AbsoluteSizeSpan(ScreenUtil.spToPx(this, 16))

        //设置字体颜色
        val coinColorSpan = ForegroundColorSpan(Color.parseColor("#009ad6"))
        //字体大小
        val coinSizeSpan = AbsoluteSizeSpan(ScreenUtil.spToPx(this, 16))

        if (secondTextStartIndex != -1) {
            builder.setSpan(
                secondColorSpan, secondTextStartIndex, secondTextStartIndex + secondText!!.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            //去掉秒字
            builder.setSpan(
                secondSizeSpan, secondTextStartIndex, secondTextStartIndex + secondText.length - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (coinTextStartIndex != -1) {
            builder.setSpan(
                coinColorSpan, coinTextStartIndex, coinTextStartIndex + coinText!!.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            //去掉金币两个字
            builder.setSpan(
                coinSizeSpan, coinTextStartIndex, coinTextStartIndex + coinText.length - 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        //设置粗体。斜体
        //StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        //StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);

        //builder.setSpan(coinColorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //builder.setSpan(backgroundColorSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //builder.setSpan(coinSizeSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //builder.setSpan(styleSpan, builder.length() - 2, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //builder.setSpan(styleSpan1, builder.length() - 5, builder.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding!!.tvRegex.text = builder
    }

    private fun setTextView12() {
        val builder = SpannableStringBuilder()
        builder.append("念去去 · 千里烟波暮霭沉沉楚天阔")
        val relativeSizeSpan = RelativeSizeSpan(2.0f)
        builder.setSpan(relativeSizeSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        val subscriptSpan = SubscriptSpan()
        builder.setSpan(subscriptSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        binding!!.textView12.text = builder
    }

    private fun setTextView13() {
        val builder = SpannableStringBuilder()
        builder.append("念去去 · 千里烟波暮霭沉沉楚天阔")

        val subscriptSpan = SubscriptSpan()
        builder.setSpan(subscriptSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        val relativeSizeSpan = RelativeSizeSpan(2.0f)
        builder.setSpan(relativeSizeSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        binding!!.textView13.text = builder
    }

    private fun setTextView14() {
        val builder = SpannableStringBuilder()
        builder.append("念去去 · 千里烟波暮霭沉沉楚天阔")

        //RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.3f);
        //builder.setSpan(relativeSizeSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        val verticalAlignTextSpan = VerticalAlignTextSpan(
            ScreenUtil.spToPx(
                this, 24
            )
        )
        builder.setSpan(verticalAlignTextSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        binding!!.textView14.text = builder
    }

    private fun setTextView15() {
        val days = "100"
        val mileage = "1000"
        //把这三个字符替换成10dp的空白
        val beReplace = "被替代"

        val result = "暗影IV100已经开始暴走了1000艰难苦恨繁霜鬓被替代哈哈"
        val builder = SpannableStringBuilder(result)
        val dayStart = result.indexOf(days)
        val mileageStart = result.indexOf(mileage)
        val replaceStart = result.indexOf(beReplace)
        Log.i(TAG, "setTextView4: dayStart=$dayStart,mileageStart=$mileageStart")

        val colorSpan = ForegroundColorSpan(Color.parseColor("#009ad6"))
        val sizeSpan = RelativeSizeSpan(2.0f)
        val colorSpan1 = ForegroundColorSpan(Color.parseColor("#009ad6"))
        val sizeSpan1 = RelativeSizeSpan(2.0f)

        val spaceSpan = SpaceSpan(ScreenUtil.dpToPx(this, 10))

        builder.setSpan(
            spaceSpan,
            replaceStart,
            replaceStart + beReplace.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        //builder.setSpan(colorSpan, dayStart, dayStart + days.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //builder.setSpan(sizeSpan, dayStart, dayStart + days.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        //builder.setSpan(colorSpan1, mileageStart, mileageStart + mileage.length(), Spannable
        // .SPAN_INCLUSIVE_INCLUSIVE);
        //builder.setSpan(sizeSpan1, mileageStart, mileageStart + mileage.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        binding!!.textView15.text = builder
    }

    /**
     * 测试EditText 设置 text 长度超限的问题。
     */
    private fun setEtOne() {
        var text = "1234567890123" //长度是13，在xml文件中限制了etOne的maxLength为12
        if (text.length > 12) {
            text = text.substring(0, 12)
        }
        binding!!.etOne.setText(text)
        binding!!.etOne.setSelection(text.length)
    }

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, DynamicLayoutTestActivity::class.java)
            context.startActivity(starter)
        }
    }
}
