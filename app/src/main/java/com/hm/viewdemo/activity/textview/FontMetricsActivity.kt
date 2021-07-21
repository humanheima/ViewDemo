package com.hm.viewdemo.activity.textview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.hm.viewdemo.R


/**
 * Created by dumingwei on 2021/1/26
 *
 * Desc:
 */
class FontMetricsActivity : AppCompatActivity() {


    private lateinit var tvNormal: TextView
    private lateinit var tvChangeFontCenter: TextView

    private val TAG: String = "FontMetricsActivity"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, FontMetricsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font_metrics)
        tvNormal = findViewById(R.id.tvNormal)
        tvChangeFontCenter = findViewById(R.id.tvChangeFontCenter)

        val paint = tvChangeFontCenter.paint

        val fontMetrics = paint.fontMetrics

        Log.d(TAG, "onCreate:  fontMetrics.top = ${fontMetrics.top}")
        Log.d(TAG, "onCreate:  fontMetrics.ascent = ${fontMetrics.ascent}")
        Log.d(TAG, "onCreate:  fontMetrics.descent = ${fontMetrics.ascent}")
        Log.d(TAG, "onCreate:  fontMetrics.ascent = ${fontMetrics.bottom}")

    }
}