package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityViswstubBinding;

/**
 * 测试ViewStub的用法
 */
public class ViewStubActivity extends BaseActivity<ActivityViswstubBinding> {

    ViewStub viewStub;
    private EditText editExtra1;
    private EditText editExtra2;
    private EditText editExtra3;
    private View inflatedView;


    public static void launch(Context context) {
        Intent starter = new Intent(context, ViewStubActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityViswstubBinding createViewBinding() {
        return ActivityViswstubBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {

        binding.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (inflatedView == null) {
//                    viewStub = (ViewStub) findViewById(R.id.view_stub);
//                    if (viewStub != null) {
//                        inflatedView = viewStub.inflate();
//                        editExtra1 = inflatedView.findViewById(R.id.edit_extra1);
//                        editExtra2 = inflatedView.findViewById(R.id.edit_extra2);
//                        editExtra3 = inflatedView.findViewById(R.id.edit_extra3);
//                    }
//                } else {
//                    inflatedView.setVisibility(View.VISIBLE);
//                }

                if (viewStub == null) {
                    viewStub = binding.viewStub;
                }
                if (viewStub != null) {
                    inflatedView = viewStub.inflate();
                    editExtra1 = inflatedView.findViewById(R.id.edit_extra1);
                    editExtra2 = inflatedView.findViewById(R.id.edit_extra2);
                    editExtra3 = inflatedView.findViewById(R.id.edit_extra3);
                }
            }
        });
        binding.btnLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inflatedView != null) {
                    inflatedView.setVisibility(View.GONE);
                }
            }
        });

    }

}
