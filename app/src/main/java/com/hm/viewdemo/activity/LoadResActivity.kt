package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2022/6/12
 * Desc: 测试资源加载
 */
class LoadResActivity : AppCompatActivity() {


    private lateinit var iv1: ImageView
    private lateinit var iv2: ImageView
    private lateinit var iv3: ImageView
    private lateinit var iv4: ImageView

    companion object {

        private const val TAG = "LoadResActivity"

        fun launch(context: Context) {
            val starter = Intent(context, LoadResActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_res)

        val assetManager = assets
        Log.i(TAG, "onCreate: assetManager = $assetManager")

        iv1 = findViewById(R.id.iv_1)
        iv2 = findViewById(R.id.iv_2)
        iv3 = findViewById(R.id.iv_3)
        iv4 = findViewById(R.id.iv_4)

        testColorType()

        iv1.post {
            Log.i(TAG, "onCreate: ${iv1.background}")
            Log.i(TAG, "onCreate: ${iv2.background}")
            Log.i(TAG, "onCreate: ${iv3.background}")
            Log.i(TAG, "onCreate: ${iv4.background}")
        }

    }

    fun testColorType() {
        val value = TypedValue()
        val clazz: Class<R.color> = R.color::class.java

        for (declaredField in clazz.declaredFields) {
            declaredField.isAccessible = true
            val name = declaredField.name
            Log.i(TAG, "testColorType: declaredField.name = $name")

            //resources.getIdentifier(name,"color",packageName)
            val resId = resources.getIdentifier(name, "color", packageName)
            resources.getValue(resId, value, true)

            Log.i(TAG, "testColorType: $name value.type = ${value.type}  , ${value.string}")

        }

        Log.i(TAG, "testColorType: --------------------------")

        resources.getValue(R.color.colorPrimary, value, true)

        Log.i(TAG, "testColorType: colorPrimary value.type = ${value.type}  , ${value.string}")

        resources.getValue(R.color.abc_background_cache_hint_selector_material_dark, value, true)

        Log.i(
            TAG,
            "testColorType: cardview_shadow_start_color value.type = ${value.type} , ${value.string}"
        )

        resources.getValue(R.color.color_list, value, true)

        Log.i(TAG, "testColorType: color_list value.type = ${value.type}, ${value.string}")


    }
}