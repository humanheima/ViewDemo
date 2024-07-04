package com.hm.viewdemo.base;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import com.hm.viewdemo.widget.LoadingDialog;

import org.jetbrains.annotations.NotNull;

/**
 * Created by dumingwei on 2017/2/26.
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();
    private LoadingDialog loadingDialog;

    @NotNull
    protected T binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = createViewBinding();
        setContentView(binding.getRoot());
        initData();
        bindEvent();
    }


    protected abstract T createViewBinding();

    protected abstract void initData();

    protected void bindEvent() {

    }

    public final void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public final void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

}
