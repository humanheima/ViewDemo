package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.hm.viewdemo.R;

public class RoundImageViewXfermodeActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, RoundImageViewXfermodeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_image_view_xfermode);
    }
}
