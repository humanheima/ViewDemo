package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.databinding.ActivityRoundImageViewBinding;

public class RoundImageViewActivity extends AppCompatActivity {

    private ActivityRoundImageViewBinding binding;

    private Animation rotationAnimation;

    private ImageView imageView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            addRotation();
        }
    };

    public static void launch(Context context) {
        Intent intent = new Intent(context, RoundImageViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoundImageViewBinding.inflate(getLayoutInflater());
        rotationAnimation = AnimationUtils.loadAnimation(this
                , R.anim.anim_image_rotate_two);

        binding.btnSetSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.roundIvFirst.setImageDrawable(getResources().getDrawable(R.drawable.ic_soft_avatar));

                //binding.roundIvFirst.setRotation(45);
                binding.roundIvBalloon.setAnimation(rotationAnimation);
                rotationAnimation.start();
                //addRotation();
            }
        });

        imageView = binding.roundIvBalloon;

    }


    public void addRotation() {
        float rotation = imageView.getRotation();
        rotation += 20;
        imageView.setRotation(rotation);
        handler.sendEmptyMessageDelayed(1, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rotationAnimation.cancel();
        handler.removeCallbacksAndMessages(null);
    }
}
