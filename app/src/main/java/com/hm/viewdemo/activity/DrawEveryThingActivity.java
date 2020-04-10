package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hm.viewdemo.R;
import com.hm.viewdemo.bean.SendOptionsBean;
import com.hm.viewdemo.widget.StageAwardView;
import com.hm.viewdemo.widget.StageAwardViewCopy;

import java.util.ArrayList;
import java.util.List;

public class DrawEveryThingActivity extends AppCompatActivity {


    private StageAwardViewCopy stageAwardView0;

    public static void launch(Context context) {
        Intent intent = new Intent(context, DrawEveryThingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_every_thing);
        stageAwardView0 = findViewById(R.id.stageAwardView0);
        setData();
    }

    private void setData() {
        List<SendOptionsBean> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            SendOptionsBean optionsBean = new SendOptionsBean();
            optionsBean.segValue = 100 * i;
            optionsBean.setSegKey(i * 2 + 0.5);
            if (i == 0) {
                optionsBean.setStatus(1);
            } else if (i < 8) {
                optionsBean.setStatus(2);
            }

            list.add(optionsBean);
        }
        stageAwardView0.setData(list, 8, 0.5f);
    }
}
