package com.hm.viewdemo.aibot.voice

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hm.viewdemo.grpc.speech.SpeechRecognizerClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions

/**
 * 把「输入框长按录音 → gRPC 流式上传 → 文本回填」整套能力绑定到输入框。
 *
 * 核心交互约定：
 * - 短按 (< 300ms)：让 EditText 走原逻辑（聚焦 / 移动光标 / 弹键盘 / 选中文本）
 * - 长按 (≥ 300ms)：立刻进入录音态，弹出浮层，后续事件全部拦截不再影响 EditText
 * - 录音时上滑/左滑超阈值 → 松手取消（不发尾帧）
 * - 正常松手 → 发送 is_last=true 尾帧，等 final 结果回填到光标位置
 */
class VoiceInputController(
    private val context: Context,
    private val inputRoot: View,
    private val editText: EditText,
    private val overlayHost: ViewGroup,
    private val lifecycleOwner: LifecycleOwner,
    private val permissionHost: PermissionHost,
) {

    interface PermissionHost {
        fun requireRecordPermission(onGranted: () -> Unit)
    }

    companion object {
        private const val TAG = "VoiceInputController"
    }

    private enum class State { IDLE, PRESSING, RECORDING, RECOGNIZING, CANCELLED }

    private var state = State.IDLE
    private val longPressMs = 300L

    /** 上滑/左滑取消阈值(px)。140dp ≈ 手指上滑一指距离。 */
    private val cancelDyPx = (-context.resources.displayMetrics.density * 140).toInt()
    private val cancelDxPx = (-context.resources.displayMetrics.density * 140).toInt()

    private var pressStartX = 0f
    private var pressStartY = 0f
    private var currentX = 0f
    private var currentY = 0f
    /** 当前手势事件是否真正落在 EditText 区域（便于 inputRoot 兜底时判断是否应该"当作编辑点击"回传） */
    private var downInsideEditText = false

    private val recorder = AudioRecorderManager()
    private val asrClient = SpeechRecognizerClient()
    private var sender: SpeechRecognizerClient.Sender? = null
    private var overlay: VoiceRecordingOverlay? = null

    private val mainHandler = Handler(Looper.getMainLooper())
    private val longPressRunnable = Runnable {
        permissionHost.requireRecordPermission { enterRecording() }
    }

    fun attach() {
        // 同时挂在两个目标上：
        // - editText: 用户绝大多数场景按到这里，是主入口
        // - inputRoot: 按到 EditText 周围留白区域也能触发
        val touchListener = View.OnTouchListener { _, event -> handleTouch(event) }
        editText.setOnTouchListener(touchListener)
        inputRoot.setOnTouchListener(touchListener)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
    ): Boolean = EasyPermissions.hasPermissions(context, Manifest.permission.RECORD_AUDIO)

    // ---------- 手势处理 ----------

    private fun handleTouch(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                pressStartX = e.rawX
                pressStartY = e.rawY
                currentX = e.rawX
                currentY = e.rawY
                downInsideEditText = isPointInsideView(editText, e.rawX, e.rawY)
                state = State.PRESSING
                // 先关键盘，即将进入录音时键盘会遮住浮层
                hideIme()
                mainHandler.removeCallbacks(longPressRunnable)
                mainHandler.postDelayed(longPressRunnable, longPressMs)

                // DOWN 阶段肯定还在 PRESSING，把事件原样交给 EditText，
                // 这样用户正常点击时聚焦/光标跳转/选中 都不丢失。
                dispatchToEditTextIfNeeded(e)
                Log.d(TAG, "ACTION_DOWN insideEt=$downInsideEditText")
            }

            MotionEvent.ACTION_MOVE -> {
                currentX = e.rawX
                currentY = e.rawY

                if (state == State.RECORDING) {
                    // 录音中：不再让 EditText 响应（避免移动光标/弹菜单），
                    // 让父容器（如 RecyclerView）也不要拦截。
                    editText.parent?.requestDisallowInterceptTouchEvent(true)
                    val dx = currentX - pressStartX
                    val dy = currentY - pressStartY
                    val inCancel = dy < cancelDyPx || dx < cancelDxPx
                    overlay?.setCancelHint(inCancel)
                } else {
                    // 未进入录音：EditText 继续响应拖动选字/拖动光标。
                    dispatchToEditTextIfNeeded(e)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mainHandler.removeCallbacks(longPressRunnable)
                val up = state
                when (up) {
                    State.PRESSING -> {
                        // 没触发长按 → 视为普通点击：让 EditText 自己收尾
                        // （比如 ACTION_UP 会真正 requestFocus / 弹键盘）
                        state = State.IDLE
                        dispatchToEditTextIfNeeded(e)
                        editText.post {
                            if (editText.hasWindowFocus()) {
                                showIme(editText)
                            }
                        }
                        Log.d(TAG, "UP/CANCEL -> treated as tap (insideEt=$downInsideEditText)")
                    }
                    State.RECORDING -> {
                        val dx = currentX - pressStartX
                        val dy = currentY - pressStartY
                        val inCancel = dy < cancelDyPx || dx < cancelDxPx
                        if (inCancel) cancelRecording()
                        else stopRecordingAndRecognize()
                    }
                    else -> {
                        state = State.IDLE
                    }
                }
                editText.parent?.requestDisallowInterceptTouchEvent(false)
            }
        }

        // 永远 return true，保证一旦 DOWN 被我们命中，后续 MOVE/UP 都会继续派给我们。
        // 否则如果在 DOWN 返回 true、但 MOVE/UP 返回 false，会出现"事件被父亲抢回去"的诡异现象。
        return true
    }

    /**
     * 对于非录音的触摸事件，原样"喂给" EditText 的 onTouchEvent，
     * 保证用户短按/拖动/双击等原编辑体验完全不丢失。
     * 录音中不会调用此函数，避免 EditText 在录音态乱弹菜单/挪光标。
     */
    private fun dispatchToEditTextIfNeeded(original: MotionEvent) {
        if (state == State.RECORDING || state == State.RECOGNIZING || state == State.CANCELLED) return
        if (!downInsideEditText) return

        // 用副本转换坐标到 EditText 自己的坐标系，保证光标位置正确
        val editLoc = IntArray(2)
        editText.getLocationOnScreen(editLoc)
        val copy = MotionEvent.obtain(original).apply {
            setLocation(original.rawX - editLoc[0], original.rawY - editLoc[1])
        }
        try {
            editText.onTouchEvent(copy)
        } catch (t: Throwable) {
            Log.w(TAG, "dispatchToEditTextIfNeeded failed", t)
        } finally {
            copy.recycle()
        }
    }

    private fun isPointInsideView(v: View, rawX: Float, rawY: Float): Boolean {
        val loc = IntArray(2)
        v.getLocationOnScreen(loc)
        return rawX >= loc[0] && rawX <= loc[0] + v.width &&
            rawY >= loc[1] && rawY <= loc[1] + v.height
    }

    // ---------- 录音 / 识别流程 ----------

    private fun enterRecording() {
        if (state != State.PRESSING) return
        state = State.RECORDING
        overlay = VoiceRecordingOverlay(context).also { overlayHost.addView(it) }
        vibrate(50)
        Log.d(TAG, "enterRecording -> start streaming session")
        startRecorderAndStream()
    }

    private fun startRecorderAndStream() {
        asrClient.startSession(
            language = "zh-CN",
            listener = object : SpeechRecognizerClient.Listener {
                override fun onPartial(text: String, confidence: Float) {
                    overlay?.updatePartialText(text)
                    Log.v(TAG, "asr onPartial: $text (${String.format("%.2f", confidence)})")
                }

                override fun onFinal(text: String, confidence: Float) {
                    Log.d(TAG, "asr onFinal: $text (${String.format("%.2f", confidence)})")
                    insertTextAtCursor(text)
                }

                override fun onError(code: Int, msg: String) {
                    Log.e(TAG, "asr onError code=$code msg=$msg")
                    overlay?.showError(msg)
                    lifecycleOwner.lifecycleScope.launch {
                        delay(900)
                        hideOverlay()
                    }
                }

                override fun onCompleted() {
                    hideOverlay()
                }
            }
        ) { s ->
            sender = s
            recorder.start(object : AudioRecorderManager.Callback {
                override fun onChunk(bytes: ByteArray, size: Int, timestampMs: Long) {
                    sender?.sendChunk(bytes, timestampMs, isLast = false)
                    val db = recorder.calcRmsDb(bytes, size)
                    mainHandler.post { overlay?.updateVolume(db) }
                }

                override fun onMaxDurationReached(totalMs: Long) {
                    mainHandler.post { stopRecordingAndRecognize() }
                }

                override fun onError(code: Int, msg: String) {
                    sender?.cancel("record_error:$code")
                    mainHandler.post {
                        overlay?.showError(msg)
                        lifecycleOwner.lifecycleScope.launch {
                            delay(900)
                            hideOverlay()
                        }
                    }
                }
            })
        }
    }

    private fun stopRecordingAndRecognize() {
        if (state != State.RECORDING) return
        state = State.RECOGNIZING
        overlay?.showRecognizing()
        recorder.stop()
        val ts = System.currentTimeMillis()
        sender?.sendChunk(ByteArray(0), ts, isLast = true)
    }

    private fun cancelRecording() {
        state = State.CANCELLED
        recorder.stop()
        sender?.cancel("user_cancel")
        hideOverlay()
        Log.d(TAG, "recording cancelled by swipe gesture")
    }

    // ---------- 辅助 ----------

    private fun insertTextAtCursor(text: String) {
        val cursor = editText.selectionStart.coerceAtLeast(0)
        val editable = (editText.text ?: SpannableStringBuilder())
        // 光标在中间位置且前面非空白 → 前后都加个空格，视觉不粘连。
        val prefix = if (cursor > 0 && editable.isNotEmpty() &&
            !editable[cursor - 1].isWhitespace()
        ) " " else ""
        editable.insert(cursor, "$prefix$text ")
        editText.text = editable
        val next = (cursor + prefix.length + text.length + 1).coerceAtMost(editable.length)
        editText.setSelection(next)
    }

    private fun hideOverlay() {
        overlay?.let { ov ->
            runCatching { (ov.parent as? ViewGroup)?.removeView(ov) }
        }
        overlay = null
        if (state != State.IDLE) state = State.IDLE
    }

    private fun vibrate(ms: Long) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            v.vibrate(ms)
        }
    }

    private fun showIme(v: android.view.View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideIme() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}
