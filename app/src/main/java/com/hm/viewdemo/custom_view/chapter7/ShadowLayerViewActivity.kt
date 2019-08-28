package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_shadow_layer_view.*

class ShadowLayerViewActivity : AppCompatActivity() {


    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, ShadowLayerViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shadow_layer_view)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnAddX -> {
                shadowLayerView.addDx()
            }
            R.id.btnSubtractX -> {
                shadowLayerView.subtractDx()
            }
            R.id.btnAddY -> {
                shadowLayerView.addDy()
            }
            R.id.btnSubtractY -> {
                shadowLayerView.subtractDy()
            }

            R.id.btnAddRadius -> {
                shadowLayerView.addRadius()
            }
            R.id.btnSubtractRadius -> {
                shadowLayerView.subtractRadius()
            }
            R.id.btnShowShadow -> {
                shadowLayerView.setShadow(true)
            }
            R.id.btnClearShadow -> {
                shadowLayerView.setShadow(false)
            }
        }
    }


}
