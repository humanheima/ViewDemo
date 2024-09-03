package com.hm.viewdemo.activity.design

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

        Glide.with(this)
            //.load("https://xxvirtualcharactercdn.xxsypro.com/8B0C6E618460014119F1537BBBEFEA18.jpg")
            .load("https://imgservices-1252317822.image.myqcloud.com/coco/s11152023/bf3b4a97.jzoi4c.jpg")
            .into(binding.ivBg)


        val lp = binding.ivBgMask.layoutParams as ViewGroup.MarginLayoutParams

        val initialTopMargin = lp.topMargin

        Log.i(TAG, "onCreate:  initialTopMargin = $initialTopMargin")

        binding.appBarLayout.post {

            val appBarLp =
                binding.appBarLayout.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams

            val behavior =
                appBarLp.behavior as? com.google.android.material.appbar.AppBarLayout.Behavior?
            Log.i(TAG, "onCreate: behavior = $behavior")
        }

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            Log.i(TAG, "verticalOffset: $verticalOffset")
            lp.topMargin = initialTopMargin + verticalOffset

            binding.ivBgMask.layoutParams = lp

        }
//        binding.imgTop.setOnClickListener {
//            val curTranslationY = binding.appBar.translationY
//            Log.i(TAG, "curTranslationY: $curTranslationY")
//            val oa = ObjectAnimator.ofFloat(binding.appBar, "translationY", curTranslationY, 300f)
//            oa.duration = 2000
//            oa.start()
//        }

//        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
//        binding.recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = RvAdapter()
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
