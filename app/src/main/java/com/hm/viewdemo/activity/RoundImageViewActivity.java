package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.databinding.ActivityRoundImageViewBinding;
import com.hm.viewdemo.widget.hongyang.RoundImageView;

public class RoundImageViewActivity extends AppCompatActivity {

    private ActivityRoundImageViewBinding binding;

    public static void launch(Context context) {
        Intent intent = new Intent(context, RoundImageViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_round_image_view);
        binding.idQiqiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.idQiqiu.setType(RoundImageView.TYPE_ROUND);
            }
        });
        binding.idMeinv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.idMeinv.setType(RoundImageView.TYPE_CIRCLE);
            }
        });
    }
}
