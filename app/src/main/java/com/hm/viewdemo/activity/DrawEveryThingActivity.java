package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hm.viewdemo.R;
import com.hm.viewdemo.bean.SendOptionsBean;
import com.hm.viewdemo.widget.StageAwardView;

import java.util.ArrayList;
import java.util.List;

public class DrawEveryThingActivity extends AppCompatActivity {


    private StageAwardView stageAwardView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, DrawEveryThingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_every_thing);
        stageAwardView = findViewById(R.id.stageAwardView);
        List<SendOptionsBean> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            SendOptionsBean optionsBean = new SendOptionsBean();
            optionsBean.segValue = 100 * i;
            optionsBean.setSegKey(i * 2);
            if (i == 0) {
                optionsBean.status = 1;
            }
            if (i == 1) {
                optionsBean.status = 2;
            }
            list.add(optionsBean);
        }
        stageAwardView.setStageList(list);
    }
}
