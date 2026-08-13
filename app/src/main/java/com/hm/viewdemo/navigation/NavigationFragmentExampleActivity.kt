package com.hm.viewdemo.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Navigation + Fragment 的传统 View 示例入口。
 *
 * NavHostFragment 会根据 navigation_graph.xml 中的 NavGraph 管理 Fragment 的创建、切换和返回栈。
 */
class NavigationFragmentExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_fragment_example)
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, NavigationFragmentExampleActivity::class.java))
        }
    }
}
