package cn.wang.ruler;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author WANG
 * GitHub -> https://github.com/WangcWj/AndroidScrollRuler
 * <p>
 * 提交issues联系作者.
 */
public class RulerActivity extends AppCompatActivity {
    ScrollRulerLayout rulerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler);
        rulerView = findViewById(R.id.ruler_view);
        rulerView.setScope(5000, 15001, 100);
        rulerView.setCurrentItem("10000");
    }
}
