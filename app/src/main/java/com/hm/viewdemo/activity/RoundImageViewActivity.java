package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.databinding.ActivityRoundImageViewBinding;

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
        binding.btnSetSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.roundIvFirst.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
            }
        });
    }
}
