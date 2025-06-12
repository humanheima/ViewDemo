package com.hm.viewdemo.guide

import android.content.Context
import android.content.Intent
import android.util.Log
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityGuideQueueManagerBinding
import com.hm.viewdemo.guide.FirstTimeTooltipStrategy.Position

/**
 * Created by p_dmweidu on 2025/6/11
 * Desc: 测试引导优先级队列功能
 */
class GuideQueueManagerActivity : BaseActivity<ActivityGuideQueueManagerBinding>() {

    private val prefs by lazy {
        getSharedPreferences("guide_prefs", Context.MODE_PRIVATE)
    }

    companion object {


        private const val TAG = "GuideQueueManagerActivi"

        fun launch(context: Context) {
            val intent = Intent(context, GuideQueueManagerActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun createViewBinding(): ActivityGuideQueueManagerBinding {
        return ActivityGuideQueueManagerBinding.inflate(layoutInflater)
    }

    override fun initData() {
        val firstGuideItem = GuideItem(
            id = "first",
            this,
            priority = 200,
            viewFinder = {
                binding.tvShowGuide
            },
            strategyProvider = { context ->
                FirstTimeTooltipStrategy(
                    context,
                    R.layout.char_guide_point_to_voice_btn,
                    binding.llRootView,
                    "first_time_tooltip"
                )
            }, dismissListener = {
                Log.d(TAG, "initData: first_time_tooltip 消失")
            }
        )

        val secondGuideItem = GuideItem(
            id = "second",
            this,
            priority = 190,
            viewFinder = {
                //binding.tvShowGuide
                 null
            },
            strategyProvider = { context ->
                FirstTimeTooltipStrategy(
                    context,
                    R.layout.char_guide_point_to_intimate,
                    binding.llRootView,
                    "second_time_tooltip",
                    Position.TOP
                )
            }, dismissListener = {
                Log.d(TAG, "initData: second_time_tooltip 消失")
            }
        )

        val thirdGuideItem = GuideItem(
            id = "third",
            this,
            priority = 180,
            viewFinder = {
                binding.tvShowGuide
            },
            strategyProvider = { context ->
                FirstTimeTooltipStrategy(
                    context,
                    R.layout.char_guide_point_to_set_relation,
                    binding.llRootView,
                    "third_time_tooltip",
                    Position.BOTTOM
                )
            }, dismissListener = {
                Log.d(TAG, "initData: third_time_tooltip 消失")
            }
        )

        val fourthGuideItem = GuideItem(
            id = "fourth",
            this,
            priority = 170,
            viewFinder = {
                binding.tvShowGuide
            },
            strategyProvider = { context ->
                FirstTimeTooltipStrategy(
                    context,
                    R.layout.char_guide_point_to_friend_circle,
                    binding.llRootView,
                    "fourth_time_tooltip",
                    Position.BOTTOM
                )
            }, dismissListener = {
                Log.d(TAG, "initData: fourth_time_tooltip 消失")
            }
        )


        val fifthGuideItem = GuideItem(
            id = "fifth",
            this,
            priority = 160,
            viewFinder = {
                binding.tvShowGuide
            },
            strategyProvider = { context ->
                FirstTimeTooltipStrategy(
                    context,
                    R.layout.char_guide_point_to_voice_btn,
                    binding.llRootView,
                    "fifth_time_tooltip",
                    Position.TOP
                )
            }, dismissListener = {
                Log.d(TAG, "initData: fifth_time_tooltip 消失")
            }
        )

        val sixthGuideItem = GuideItem(
            id = "sixth",
            this,
            priority = 150,
            viewFinder = {
                binding.tvShowGuide
            },
            strategyProvider = { context ->
                FirstTimeTooltipStrategy(
                    context,
                    R.layout.char_guide_point_to_intimate,
                    binding.llRootView,
                    "sixth_time_tooltip",
                    Position.TOP
                )
            }, dismissListener = {
                Log.d(TAG, "initData: sixth_time_tooltip 消失")
            }
        )

        binding.tvShowGuide.setOnClickListener {
            GuideQueueManager.addGuide(thirdGuideItem)
            GuideQueueManager.addGuide(fourthGuideItem)
            GuideQueueManager.addGuide(fifthGuideItem)
            GuideQueueManager.addGuide(sixthGuideItem)

            GuideQueueManager.addGuide(firstGuideItem)
            GuideQueueManager.addGuide(secondGuideItem)
        }

        binding.tvAddGuide.setOnClickListener {
            GuideQueueManager.addGuide(thirdGuideItem)
            GuideQueueManager.addGuide(fourthGuideItem)
            GuideQueueManager.addGuide(fifthGuideItem)
            GuideQueueManager.addGuide(sixthGuideItem)
        }

        binding.tvRemoveAllGuide.setOnClickListener {
            GuideQueueManager.clearAllGuides()
        }

        binding.tvRemoveAllSp.setOnClickListener {
            prefs.edit().clear().apply()
        }

    }


}
