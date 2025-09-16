package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import com.hm.viewdemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by p_dmweidu on 2025/9/12
 * Desc:
 */
class CustomActivityPosition @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val imageView: AppCompatImageView
    private val closeButton: ImageView
    private var showCloseButton: Boolean = true
    private var autoHideDelay: Long = 5000 // 5秒延迟

    /**
     * 图片的宽高
     */
    var ivSize: Int = 400

    init {

        // 初始化ImageView
        imageView = AppCompatImageView(context).apply {
            id = View.generateViewId()
            layoutParams = LayoutParams(ivSize, ivSize)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        addView(imageView)

        // 初始化关闭按钮
        closeButton = ImageView(context).apply {
            layoutParams = LayoutParams(
                (32 * context.resources.displayMetrics.density).toInt(),
                (32 * context.resources.displayMetrics.density).toInt()
            ).apply {
                addRule(ALIGN_TOP, imageView.id)
                addRule(ALIGN_END, imageView.id)
                //setMargins(4, -40, -4, 4)
            }
            setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
            setBackgroundResource(android.R.drawable.btn_default_small)
            visibility = if (showCloseButton) View.VISIBLE else View.GONE
            setOnClickListener { setViewVisibility(false) }
        }
        addView(closeButton)

        // 处理自定义属性
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.CustomActivityPosition, 0, 0)
            showCloseButton =
                typedArray.getBoolean(R.styleable.CustomActivityPosition_showCloseButton, true)
            closeButton.visibility = if (showCloseButton) View.VISIBLE else View.GONE
            typedArray.recycle()
        }
    }

    // 设置图片资源
    fun setImageResource(resId: Int) {
        imageView.setImageResource(resId)
    }

    // 设置是否显示关闭按钮
    fun setShowCloseButton(show: Boolean) {
        showCloseButton = show
        closeButton.visibility = if (show) View.VISIBLE else View.GONE
    }

    // 设置自动隐藏
    fun setAutoHide(enabled: Boolean) {
        if (enabled) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(autoHideDelay)
                setViewVisibility(false)
            }
        }
    }

    // 设置视图可见性
    private fun setViewVisibility(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

}

