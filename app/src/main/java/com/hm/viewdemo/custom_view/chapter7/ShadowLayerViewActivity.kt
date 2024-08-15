package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.view.View
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityShadowLayerViewBinding

class ShadowLayerViewActivity : BaseActivity<ActivityShadowLayerViewBinding>() {


    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, ShadowLayerViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityShadowLayerViewBinding {
        return ActivityShadowLayerViewBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.btnAddX -> {
                binding.shadowLayerView.addDx()
            }

            R.id.btnSubtractX -> {
                binding.shadowLayerView.subtractDx()
            }

            R.id.btnAddY -> {
                binding.shadowLayerView.addDy()
            }

            R.id.btnSubtractY -> {
                binding.shadowLayerView.subtractDy()
            }

            R.id.btnAddRadius -> {
                binding.shadowLayerView.addRadius()
            }

            R.id.btnSubtractRadius -> {
                binding.shadowLayerView.subtractRadius()
            }

            R.id.btnShowShadow -> {
                binding.shadowLayerView.setShadow(true)
            }

            R.id.btnClearShadow -> {
                binding.shadowLayerView.setShadow(false)
            }
        }
    }


}
