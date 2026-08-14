package com.hm.viewdemo.aibot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hm.viewdemo.R

internal enum class AiMessageRole {
    USER,
    ASSISTANT,
    TYPING
}

internal data class AiMessage(
    val id: Long,
    val role: AiMessageRole,
    val content: String
)

internal class AiMessageAdapter : RecyclerView.Adapter<AiMessageAdapter.MessageViewHolder>() {

    private val messages = mutableListOf<AiMessage>()

    fun addMessage(message: AiMessage) {
        messages += message
        notifyItemInserted(messages.lastIndex)
    }

    fun removeTypingMessage() {
        val index = messages.indexOfLast { it.role == AiMessageRole.TYPING }
        if (index >= 0) {
            messages.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int = messages[position].role.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layout = when (viewType) {
            AiMessageRole.USER.ordinal -> R.layout.item_ai_message_user
            AiMessageRole.TYPING.ordinal -> R.layout.item_ai_message_typing
            else -> R.layout.item_ai_message_assistant
        }
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(layout, parent, false),
            viewType
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    class MessageViewHolder(
        itemView: View,
        private val viewType: Int
    ) : RecyclerView.ViewHolder(itemView) {

        private val messageView: View = itemView.findViewById(R.id.tv_ai_message)

        fun bind(message: AiMessage) {
            when (viewType) {
                AiMessageRole.USER.ordinal -> {
                    (messageView as TextView).text = message.content
                }

                AiMessageRole.TYPING.ordinal -> {
                    (messageView as TextView).text = message.content
                }

                else -> {
                    (messageView as AiMarkdownView).setMarkdown(message.content)
                }
            }
        }
    }
}
