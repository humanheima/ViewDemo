package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.capton.colorfulprogressbar.ColorfulProgressbar;
import com.capton.colorfulprogressbar.DisplayUtil;
import com.hm.viewdemo.R;
import com.hm.viewdemo.bean.MyBean;
import com.hm.viewdemo.widget.ClickSwitchCompat;
import com.xx.reader.utils.JsonUtilKt;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by p_dmweidu on 2023/8/28
 * Desc: 测试 ColorfulProgressBar
 */
public class ColorfulProgressBarActivity extends AppCompatActivity {


    private final String TAG = "ColorfulProgressBarActi";

    private ColorfulProgressbar progressbar;
    private ColorfulProgressbar progressbar2;
    private ColorfulProgressbar progressbar3;
    private ColorfulProgressbar progressbar4;
    private SeekBar controller;
    private SeekBar controller2;
    private SwitchCompat aSwitch;
    private SwitchCompat aSwitch2;
    private ClickSwitchCompat switch3;

    public static void launch(Context context) {
        Intent starter = new Intent(context, ColorfulProgressBarActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorful_progress_bar);
        initView();
        testJson();
    }

    private void initView() {
        progressbar = findViewById(R.id.colorful);
        progressbar2 = findViewById(R.id.colorful2);
        progressbar3 = findViewById(R.id.colorful3);
        progressbar4 = findViewById(R.id.colorful4);

        controller = findViewById(R.id.controller);
        controller2 = findViewById(R.id.controller2);

        aSwitch = findViewById(R.id.switch1);
        aSwitch2 = findViewById(R.id.switch2);

        switch3 = findViewById(R.id.switch3);

        progressbar.setHeight(DisplayUtil.dip2px(this, 20));

        progressbar2.setHeight(DisplayUtil.dip2px(this, 10));
        progressbar2.setStyle(ColorfulProgressbar.STYLE_NORMAL);
        progressbar2.setProgressColorRes(R.color.green);

        progressbar.setMaxProgress(100);
        progressbar2.setMaxProgress(100);

        progressbar.setProgress(50);
        progressbar.setSecondProgress(10);

        progressbar2.setProgress(50);
        progressbar2.setSecondProgress(10);

        controller.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressbar.setProgress(progress);
                progressbar2.setProgress(progress);
                progressbar3.setProgress(progress);
                progressbar4.setProgress(progress);
                ((TextView) findViewById(R.id.tip)).setText("Set Progress : " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        controller2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressbar.setSecondProgress(progress);
                progressbar2.setSecondProgress(progress);
                progressbar3.setSecondProgress(progress);
                progressbar4.setSecondProgress(progress);
                ((TextView) findViewById(R.id.tip2)).setText("Set SecondProgress : " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    progressbar.setAnimation(true);
                    progressbar2.setAnimation(true);
                    progressbar3.setAnimation(true);
                    progressbar4.setAnimation(true);
                } else {
                    progressbar.setAnimation(false);
                    progressbar2.setAnimation(false);
                    progressbar3.setAnimation(false);
                    progressbar4.setAnimation(false);
                }
            }
        });
        aSwitch2.setChecked(true);
        aSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    progressbar.showPercentText(true);
                    progressbar2.showPercentText(true);
                    progressbar3.showPercentText(true);
                    progressbar4.showPercentText(true);
                } else {
                    progressbar.showPercentText(false);
                    progressbar2.showPercentText(false);
                    progressbar3.showPercentText(false);
                    progressbar4.showPercentText(false);
                }
            }
        });

        aSwitch.setEnabled(false);
        aSwitch2.setEnabled(false);

        switch3.setSpecialClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = switch3.isChecked();
                if (checked) {
                    switch3.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switch3.setChecked(false);
                            Toast.makeText(ColorfulProgressBarActivity.this, "开关已关闭", Toast.LENGTH_SHORT).show();
                        }
                    }, 300);

                } else {
                    switch3.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switch3.setChecked(true);
                            Toast.makeText(ColorfulProgressBarActivity.this, "开关已打开", Toast.LENGTH_SHORT).show();
                        }
                    }, 300);
                }
            }
        });
    }

    private void testJson() {
        MyBean bean = new MyBean("标题", "描述");
        //把对象转化成字符串
        String jsonString = JsonUtilKt.toJson(bean);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();

                Object value = jsonObject.opt(key);
                Log.i(TAG, "testJson: key = " + key + " value = " + value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
