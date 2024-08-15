package com.hm.viewdemo.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE

/**
 * Created by p_dmweidu on 2022/9/5
 * Desc: 剪贴板工具类
 */
class ClipBoardUtil {

    companion object {

        @JvmStatic
        fun putToClipboard(context: Context?, label: String?, text: String?) {
            if (context == null || label.isNullOrEmpty() || text.isNullOrEmpty()) {
                return
            }
            val clipBoardManager =
                context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText(label, text)
            clipBoardManager.setPrimaryClip(clip)
        }

        @JvmStatic
        fun getClipboard(context: Context?): ClipData? {
            if (context == null) {
                return null
            }
            val clipBoardManager =
                context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            return clipBoardManager.primaryClip
        }

        @JvmStatic
        fun clear(context: Context?) {
            if (context == null) {
                return
            }
            val clipBoardManager =
                context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipBoardManager.setPrimaryClip(ClipData.newPlainText("", ""))
        }


    }
}