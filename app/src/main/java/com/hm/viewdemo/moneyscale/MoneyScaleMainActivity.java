package com.hm.viewdemo.moneyscale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2021/7/26
 * <p>
 * Desc:
 */
public class MoneyScaleMainActivity extends AppCompatActivity implements ScaleMoney.MoveScaleInterface {


    private TextView tvValue;
    private ScaleMoney svView;

    public static void launch(Context context) {
        Intent starter = new Intent(context, MoneyScaleMainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_scale_activity_main);
        tvValue = (TextView) findViewById(R.id.tv_value);
        svView = (ScaleMoney) findViewById(R.id.sm_view);
        svView.setMoveScaleInterface(this);

    }

    @Override
    public void getValue(int value) {
        tvValue.setText(value + "");
    }
}
