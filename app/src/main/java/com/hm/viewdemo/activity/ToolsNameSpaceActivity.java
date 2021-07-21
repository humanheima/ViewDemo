package com.hm.viewdemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hm.viewdemo.R;

/**
 * tools 命名空间的使用
 */
public class ToolsNameSpaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_name_space);
    }

    public void click(View view) {
        Toast.makeText(this, "ToolsNameSpaceActivity", Toast.LENGTH_SHORT).show();
    }
}
