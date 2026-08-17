package com.hm.viewdemo.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityShareDemoBinding

/**
 * Demonstrates Android's system Sharesheet with a text payload containing a link.
 * 使用系统分享能力，分享链接。
 */
class ShareDemoActivity : BaseActivity<ActivityShareDemoBinding>() {

    override fun createViewBinding(): ActivityShareDemoBinding {
        return ActivityShareDemoBinding.inflate(layoutInflater)
    }

    override fun initData() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.share_background)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnShare.setOnClickListener { shareTextLink() }
        binding.btnCopy.setOnClickListener { copyShareText() }

        val previewWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updatePreview()
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }
        binding.etTitle.addTextChangedListener(previewWatcher)
        binding.etMessage.addTextChangedListener(previewWatcher)
        binding.etLink.addTextChangedListener(previewWatcher)
        updatePreview()
    }

    private fun updatePreview() {
        val title = binding.etTitle.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()
        val link = binding.etLink.text.toString().trim()

        binding.tvPreviewTitle.text = title.ifBlank { getString(R.string.share_default_title) }
        binding.tvPreviewMessage.text = message.ifBlank { getString(R.string.share_default_message) }
        binding.tvPreviewLink.text = link.ifBlank { getString(R.string.share_default_link) }
    }

    private fun shareTextLink() {
        val title = binding.etTitle.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()
        val link = binding.etLink.text.toString().trim()

        if (link.isBlank()) {
            binding.etLink.error = getString(R.string.share_link_required)
            binding.etLink.requestFocus()
            return
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title.ifBlank { getString(R.string.share_default_title) })
            putExtra(Intent.EXTRA_TEXT, buildShareText(title, message, link))
        }

        if (shareIntent.resolveActivity(packageManager) == null) {
            Toast.makeText(this, R.string.share_no_app, Toast.LENGTH_SHORT).show()
            return
        }

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)))
    }

    private fun copyShareText() {
        val title = binding.etTitle.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()
        val link = binding.etLink.text.toString().trim()
        if (link.isBlank()) {
            binding.etLink.error = getString(R.string.share_link_required)
            binding.etLink.requestFocus()
            return
        }

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
            ClipData.newPlainText(
                getString(R.string.share_clip_label),
                buildShareText(title, message, link)
            )
        )
        Toast.makeText(this, R.string.share_copied, Toast.LENGTH_SHORT).show()
    }

    private fun buildShareText(title: String, message: String, link: String): String {
        return listOf(
            title.ifBlank { getString(R.string.share_default_title) },
            message.ifBlank { getString(R.string.share_default_message) },
            link
        ).filter(String::isNotBlank).joinToString("\n\n")
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            context.startActivity(Intent(context, ShareDemoActivity::class.java))
        }
    }
}
