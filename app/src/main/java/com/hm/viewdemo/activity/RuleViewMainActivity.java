package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hm.viewdemo.R;
import com.zkk.view.rulerview.RulerView;

/**
 * Created by dumingwei on 2021/7/26
 * <p>
 * Desc:
 */
public class RuleViewMainActivity extends AppCompatActivity {

    private RulerView ruler_height;   //身高的view
    private RulerView ruler_weight;  //体重的view
    private TextView tv_register_info_height_value, tv_register_info_weight_value;


    public static void launch(Context context) {
        Intent starter = new Intent(context, RuleViewMainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_view_main);

        ruler_height = (RulerView) findViewById(R.id.ruler_height);
        ruler_weight = (RulerView) findViewById(R.id.ruler_weight);

        tv_register_info_height_value = (TextView) findViewById(R.id.tv_register_info_height_value);
        tv_register_info_weight_value = (TextView) findViewById(R.id.tv_register_info_weight_value);


        ruler_height.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tv_register_info_height_value.setText(value + "");
            }
        });


        ruler_weight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                tv_register_info_weight_value.setText(value + "");
            }
        });

        ruler_height.setValue(165, 80, 250, 1);

        ruler_weight.setValue(55, 20, 200, 0.1f);

    }
}
