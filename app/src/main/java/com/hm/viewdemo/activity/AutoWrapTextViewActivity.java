package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.widget.AutoWrapTextView;

import butterknife.BindView;

public class AutoWrapTextViewActivity extends BaseActivity {

    @BindView(R.id.text_view)
    AutoWrapTextView textView;
    @BindView(R.id.text_normal)
    TextView textNormal;
    private String text = "豫章故郡，洪都新府。星分翼轸（zhěn），地接衡庐。襟三江而带五湖，控蛮荆而引瓯（ōu）越。物华天宝，龙光射牛斗之墟；人杰地灵，徐孺下陈蕃（fān）之榻（tà）。雄州雾列，俊采星驰。台隍（huáng）枕（zhěn）夷夏之交，宾主尽东南之美。都督阎（yán）公之雅望，棨（qǐ）戟（jǐ）遥临；宇文新州之懿（yì）范，襜（chān）帷（wéi）暂驻。十旬休假，胜友如云；千里逢迎，高朋满座。腾蛟（jiāo）起凤，孟学士之词宗；紫电青霜，王将军之武库。家君作宰，路出名区；童子何知，躬逢胜饯（jiàn）。";
    private String text1 = "密码：jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj";


    public static void launch(Context context) {
        Intent starter = new Intent(context, AutoWrapTextViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_auto_wrap_text_view;
    }

    @Override
    protected void initData() {
        textView.setText(text1);
        textNormal.setText(text1);
    }
}
