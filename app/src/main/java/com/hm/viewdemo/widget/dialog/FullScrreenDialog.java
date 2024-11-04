package com.hm.viewdemo.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hm.viewdemo.R;

/**
 * Created by p_dmweidu on 2024/10/31
 * Desc: 全屏弹窗
 */
public class FullScrreenDialog extends Dialog {

    public FullScrreenDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //<!--关键点1-- >
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_signin, null);
        //<!--关键点2-- >
        setContentView(view);
        //<!--关键点3-- >
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        //<!--关键点4-- >
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
}