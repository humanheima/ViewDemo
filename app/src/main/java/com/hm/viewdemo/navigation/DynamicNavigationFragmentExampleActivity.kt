package com.hm.viewdemo.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.hm.viewdemo.R

/**
 * Navigation + Fragment 动态创建示例。
 *
 * 布局只提供普通容器，NavHostFragment 和 NavGraph 都由 Activity 在代码中安装。
 */
class DynamicNavigationFragmentExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_navigation_fragment_example)

        // Activity 重建时 FragmentManager 会自动恢复已有 NavHost，不能重复添加。
        if (savedInstanceState == null) {
            val navHost = NavHostFragment.create(
                R.navigation.navigation_fragment_graph
            )

            supportFragmentManager.beginTransaction()
                .replace(R.id.navigation_dynamic_host, navHost)
                // 设置主导航 Fragment，让系统返回键交给这个 NavHost 处理。
                .setPrimaryNavigationFragment(navHost)
                .commit()
        }
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(
                Intent(context, DynamicNavigationFragmentExampleActivity::class.java)
            )
        }
    }
}
