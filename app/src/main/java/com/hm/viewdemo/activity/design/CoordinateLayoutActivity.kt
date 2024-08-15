package com.hm.viewdemo.activity.design

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivityCoordinateLayoutBinding
import com.hm.viewdemo.databinding.ItemNewsListBinding

class CoordinateLayoutActivity : AppCompatActivity() {


    private val TAG = "CoordinateLayoutActivit"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, CoordinateLayoutActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityCoordinateLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoordinateLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgTop.setOnClickListener {
            val curTranslationY = binding.appBar.translationY
            Log.i(TAG, "curTranslationY: $curTranslationY")
            val oa = ObjectAnimator.ofFloat(binding.appBar, "translationY", curTranslationY, 300f)
            oa.duration = 2000
            oa.start()
        }
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = RvAdapter()
    }

    class RvAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<RvAdapter.VH>() {


        override fun onCreateViewHolder(parent: ViewGroup, position: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(com.hm.viewdemo.R.layout.item_news_list, parent, false)
            return VH(view)
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            //holder.itemView.text_news.text = "hello world$position"
            holder.binding.textNews.text = "hello world$position"
        }


        class VH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

            val binding = ItemNewsListBinding.bind(itemView)

        }
    }

}
