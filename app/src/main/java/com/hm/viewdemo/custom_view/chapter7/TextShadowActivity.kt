package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityTextShadowBinding

class TextShadowActivity : BaseActivity<ActivityTextShadowBinding>() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TextShadowActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityTextShadowBinding {
        return ActivityTextShadowBinding.inflate(layoutInflater)
    }

    override fun initData() {
        //通过代码给文字设置阴影
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val dataList = ArrayList<TextAdapter.TextModel>()
        for (i in 0..20) {
            dataList.add(
                TextAdapter.TextModel(
                    "在代码里设置阴影",
                    i.toFloat(),
                    Color.parseColor("#aaaaaa"),
                    i.toFloat(),
                    i.toFloat()
                )
            )
        }
        binding.recyclerView.adapter = TextAdapter(dataList)

        binding.tv.setShadowLayer(3f, 4f, 4f, resources.getColor(R.color.colorPrimary))
        // TODO: 这个跟在xml中设置的效果是一样的
        binding.tv1.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.tv1.setShadowLayer(
            //3.dp2pxFloat(this),
            3f,
            5f,
            5f,
            Color.parseColor("#aaaaaa")
        )
    }

}
