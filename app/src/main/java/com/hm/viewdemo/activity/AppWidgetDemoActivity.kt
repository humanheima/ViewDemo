package com.hm.viewdemo.activity

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityAppWidgetDemoBinding
import com.hm.viewdemo.widget.DreamChatWidgetProvider
import com.hm.viewdemo.widget.WidgetData
import java.util.concurrent.Executors

/**
 * 桌面小组件功能演示页
 *  - 添加小组件到桌面
 *  - 模拟网络请求后用新数据刷新小组件
 */
class AppWidgetDemoActivity : BaseActivity<ActivityAppWidgetDemoBinding>() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    override fun createViewBinding() = ActivityAppWidgetDemoBinding.inflate(layoutInflater)

    override fun initData() {
        binding.btnPinWidget.setOnClickListener { requestPinWidget() }
        binding.btnSimulateRefresh.setOnClickListener { simulateNetworkRefresh() }
    }

    // ──────────────────────────────────────────────────────────────────────
    // Step 1：添加小组件到桌面
    // ──────────────────────────────────────────────────────────────────────
    private fun requestPinWidget() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            toast("Android 8.0+ 才支持应用内请求固定小组件")
            return
        }
        val manager = getSystemService(AppWidgetManager::class.java)
        val provider = ComponentName(this, DreamChatWidgetProvider::class.java)

        if (manager.isRequestPinAppWidgetSupported) {
            manager.requestPinAppWidget(provider, null, null)
            updateStatus("已发起固定请求，请在桌面弹窗中选择位置")
        } else {
            if (!tryLaunchMiuiWidgetPicker()) {
                toast("请长按桌面空白处 → 小组件 → 找到 ViewDemo → 拖到桌面")
                updateStatus("当前桌面不支持应用内固定，请手动添加")
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // Step 2：模拟网络请求，返回后用新数据刷新小组件
    // ──────────────────────────────────────────────────────────────────────
    private fun simulateNetworkRefresh() {
        setLoading(true)
        updateStatus("正在请求网络数据...")
        appendLog("⏳ 发起网络请求（模拟 1.5s 延迟）...")

        executor.execute {
            // ── 模拟网络延迟 ──
            Thread.sleep(1500)

            // ── 模拟服务器返回的新数据（使用新的图片 URL）──
            val newData = WidgetData(
                title = "跟你的梦中人聊聊",
                brandName = "筑梦岛",
                characters = listOf(
                    WidgetData.Character("红羽", NEW_IMAGE_URL),
                    WidgetData.Character("君山", NEW_IMAGE_URL),
                    WidgetData.Character("摄羽", NEW_IMAGE_URL),
                    WidgetData.Character("鹤白", NEW_IMAGE_URL)
                )
            )

            mainHandler.post { appendLog("✅ 网络数据返回，开始更新小组件...") }

            // ── 刷新所有已添加的桌面小组件 ──
            DreamChatWidgetProvider.refreshWithData(this, newData)

            mainHandler.post {
                setLoading(false)
                updateStatus("✅ 更新完成！新图片已下发到桌面小组件")
                appendLog("🎉 小组件更新成功\n   新图片 URL：\n   $NEW_IMAGE_URL")
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // UI helpers
    // ──────────────────────────────────────────────────────────────────────
    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnSimulateRefresh.isEnabled = !loading
        binding.btnPinWidget.isEnabled = !loading
    }

    private fun updateStatus(text: String) {
        binding.tvStatus.text = text
    }

    private fun appendLog(msg: String) {
        val current = binding.tvLog.text.toString()
        binding.tvLog.text = if (current.isEmpty()) msg else "$current\n$msg"
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    // ──────────────────────────────────────────────────────────────────────
    // MIUI 兜底：尝试打开系统小组件选择器
    // ──────────────────────────────────────────────────────────────────────
    private fun tryLaunchMiuiWidgetPicker(): Boolean {
        val candidates = listOf(
            "com.miui.home.launcher.action.APPWIDGET_PICKER",
            "miui.intent.action.ADD_SHORTCUT"
        )
        for (action in candidates) {
            try {
                val intent = Intent(action).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                    return true
                }
            } catch (_: Exception) { /* 继续下一个 */ }
        }
        return false
    }

    companion object {
        private const val NEW_IMAGE_URL =
            "https://imgservices-1252317822.image.myqcloud.com/coco/s03172025/5c9f9dde.96zd2r.png"

        fun launch(context: Context) {
            context.startActivity(Intent(context, AppWidgetDemoActivity::class.java))
        }
    }
}
