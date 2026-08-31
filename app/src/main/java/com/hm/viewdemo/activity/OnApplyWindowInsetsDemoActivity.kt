package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityOnApplyWindowInsetsDemoBinding

/**
 * ViewCompat.setOnApplyWindowInsetsListener 的可视化示例。
 *
 * 页面故意把 Toolbar 和底部操作栏作为两个独立的 Insets 消费者，
 * 方便观察同一份 Insets 如何沿 View 树继续向下分发。
 */
class OnApplyWindowInsetsDemoActivity :
    BaseActivity<ActivityOnApplyWindowInsetsDemoBinding>() {

    private var applyCount = 0
    private var toolbarInitialTop = 0
    private var bottomBarInitialBottom = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // 让页面绘制到系统栏后面，再由 listener 决定具体控件的避让方式。
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
    }

    override fun createViewBinding(): ActivityOnApplyWindowInsetsDemoBinding {
        return ActivityOnApplyWindowInsetsDemoBinding.inflate(layoutInflater)
    }

    override fun initData() {
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }

        toolbarInitialTop = binding.insetsDemoToolbar.paddingTop
        bottomBarInitialBottom = binding.insetsDemoBottomBar.paddingBottom
        installInsetsListeners()
        bindActions()

        // BaseActivity 已经完成 setContentView，此时主动触发一次分发。
        ViewCompat.requestApplyInsets(binding.insetsDemoRoot)
    }

    private fun installInsetsListeners() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.insetsDemoRoot) { _, insets ->
            applyCount++
            updateInsetReadout(insets)

            if (binding.switchConsumeInsets.isChecked) {
                // 根 View 消费时停止常规向子 View 分发，恢复为未避让状态方便对比。
                binding.insetsDemoToolbar.updatePadding(top = toolbarInitialTop)
                binding.insetsDemoBottomBar.updatePadding(bottom = bottomBarInitialBottom)
                updateAppliedPaddingReadout()
                WindowInsetsCompat.CONSUMED
            } else {
                insets
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.insetsDemoToolbar) { _, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val top = maxOf(status.top, cutout.top)
            binding.insetsDemoToolbar.updatePadding(top = toolbarInitialTop + top)
            updateAppliedPaddingReadout()
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.insetsDemoBottomBar) { _, insets ->
            val navigation = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val bottom = maxOf(navigation.bottom, ime.bottom, cutout.bottom)
            binding.insetsDemoBottomBar.updatePadding(
                bottom = bottomBarInitialBottom + bottom
            )
            updateAppliedPaddingReadout()
            insets
        }
    }

    private fun bindActions() {
        binding.btnRequestInsets.setOnClickListener {
            ViewCompat.requestApplyInsets(binding.insetsDemoRoot)
        }

        binding.switchConsumeInsets.setOnCheckedChangeListener { _, checked ->
            binding.tvConsumeHint.text = if (checked) {
                "根 View 返回 CONSUMED，停止常规向子 View 分发（低版本请实机验证）"
            } else {
                "根 View 返回原始 Insets，继续向子 View 分发"
            }
            ViewCompat.requestApplyInsets(binding.insetsDemoRoot)
        }

        binding.btnFocusInput.setOnClickListener {
            binding.etDemoInput.requestFocus()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(binding.etDemoInput, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.btnHideKeyboard.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etDemoInput.windowToken, 0)
            binding.etDemoInput.clearFocus()
        }
    }

    private fun updateInsetReadout(insets: WindowInsetsCompat) {
        val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
        val navigation = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
        val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
        val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
        val gestures = insets.getInsets(WindowInsetsCompat.Type.systemGestures())

        binding.tvStatusInsets.text = formatInsets("Status bar", status)
        binding.tvNavigationInsets.text = formatInsets("Navigation bar", navigation)
        binding.tvImeInsets.text = formatInsets(
            "IME (${if (insets.isVisible(WindowInsetsCompat.Type.ime())) "visible" else "hidden"})",
            ime
        )
        binding.tvCutoutInsets.text = formatInsets("Display cutout", cutout)
        binding.tvGestureInsets.text = formatInsets("System gestures", gestures)
        binding.tvCallbackCount.text = "回调次数：$applyCount"
        binding.tvDispatchState.text = if (binding.switchConsumeInsets.isChecked) {
            "当前状态：已消费 Insets"
        } else {
            "当前状态：继续向子 View 分发"
        }
    }

    private fun updateAppliedPaddingReadout() {
        binding.tvAppliedPadding.text =
            "Toolbar top padding：${binding.insetsDemoToolbar.paddingTop}px    " +
                "底部栏 bottom padding：${binding.insetsDemoBottomBar.paddingBottom}px"
    }

    private fun formatInsets(label: String, value: Insets): String {
        return "$label    L ${value.left}    T ${value.top}    R ${value.right}    B ${value.bottom} px"
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            context.startActivity(Intent(context, OnApplyWindowInsetsDemoActivity::class.java))
        }
    }
}
