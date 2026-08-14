package com.hm.viewdemo.aibot

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityAiBotBinding

/** A small, local AI chat surface used to demonstrate Markdown responses. */
class AiBotActivity : BaseActivity<ActivityAiBotBinding>() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private val adapter = AiMessageAdapter()
    private var nextMessageId = 0L
    private var isResponding = false

    override fun createViewBinding(): ActivityAiBotBinding = ActivityAiBotBinding.inflate(layoutInflater)

    override fun initData() {
        binding.btnAiBack.setOnClickListener { finish() }

        binding.rvAiMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = false
        }
        binding.rvAiMessages.adapter = adapter

        addMessage(AiMessageRole.ASSISTANT, """
            你好，我是 **AI Bot**。

            我可以帮你整理信息、解释代码，或者生成一份清晰的方案。

            直接输入问题，我会用 Markdown 排版回复。
        """.trimIndent())
        addMessage(AiMessageRole.USER, "帮我总结一下 Markdown 在聊天中的优点")
        addMessage(AiMessageRole.ASSISTANT, """
            ## Markdown 很适合 AI 回复

            - **结构清晰**：标题、列表和引用让长回复更容易扫描。
            - **重点突出**：可以用粗体、*斜体* 和 `行内代码` 标记关键信息。
            - **技术友好**：代码块能保留格式，复制也更方便。

            ```kotlin
            val reply = "可读、可复制、易扩展"
            ```

            | 类型 | 适合场景 | 展示方式 |
            | --- | --- | --- |
            | 列表 | 快速浏览 | 紧凑清晰 |
            | 表格 | 对比数据 | 横向可滚动 |

            发送一条消息试试，下面的回复会在短暂思考后出现。
        """.trimIndent())
    }

    override fun bindEvent() {
        binding.btnAiSend.setOnClickListener { sendMessage() }
        binding.etAiMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }
        binding.etAiMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnAiSend.isEnabled = !isResponding && !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
        binding.btnAiSend.isEnabled = false
    }

    private fun sendMessage() {
        if (isResponding) return
        val question = binding.etAiMessage.text?.toString()?.trim().orEmpty()
        if (question.isEmpty()) return

        addMessage(AiMessageRole.USER, question)
        binding.etAiMessage.text?.clear()
        hideKeyboard()

        isResponding = true
        binding.etAiMessage.isEnabled = false
        binding.btnAiSend.isEnabled = false
        addMessage(AiMessageRole.TYPING, "正在思考  ·  ·  ·")

        mainHandler.postDelayed({
            adapter.removeTypingMessage()
            addMessage(AiMessageRole.ASSISTANT, createDemoReply(question))
            isResponding = false
            binding.etAiMessage.isEnabled = true
            binding.btnAiSend.isEnabled = !binding.etAiMessage.text.isNullOrBlank()
            binding.etAiMessage.requestFocus()
        }, 850L)
    }

    private fun createDemoReply(question: String): String {
        val tableExample = if (question.contains("表格") || question.contains("table", ignoreCase = true)) {
            """

            ### 表格示例

            | 项目 | 状态 | 备注 |
            | --- | --- | --- |
            | Markdown 解析 | 已支持 | 自动识别管道语法 |
            | 横向滚动 | 已支持 | 列较多时使用 |
            """.trimIndent()
        } else {
            ""
        }
        return """
            ## 已收到你的问题

            你刚才输入的是：

            > ${question.replace("\n", " ")}

            我会把回复拆成易读的 Markdown 结构：

            1. 先提炼问题的核心目标。
            2. 再给出可以直接执行的建议。
            3. 最后补充必要的示例或注意事项。

            $tableExample

            **这是本地演示回复**，接入真实模型时，将 `createDemoReply` 替换为网络请求结果即可。
        """.trimIndent()
    }

    private fun addMessage(role: AiMessageRole, content: String) {
        adapter.addMessage(AiMessage(nextMessageId++, role, content))
        binding.rvAiMessages.post {
            if (adapter.itemCount > 0) {
                binding.rvAiMessages.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etAiMessage.windowToken, 0)
    }

    override fun onDestroy() {
        mainHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, AiBotActivity::class.java))
        }
    }
}
