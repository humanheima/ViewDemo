package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2023/8/21
 * Desc: 测试 Chip的使用
 * https://www.jianshu.com/p/d64a75ec7c74
 */
class MaterialChipActivity : AppCompatActivity() {


    private var chipGroupDynamicAddView: ChipGroup? = null

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, MaterialChipActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_chip)
        chipGroupDynamicAddView = findViewById(R.id.chipGroupDynamicAddView)
        //只需选择一个
        chipGroupDynamicAddView?.isSingleSelection = true
        for (i in 0 until 10) {
            val chip = Chip(this)
            chip.text = "动态添加的Chip$i"
            chip.isCheckable = true
            chipGroupDynamicAddView?.addView(chip)
        }

        //ChipGroup中设置选中监听-- 只有单选的chipGroup才可以使用
        chipGroupDynamicAddView?.setOnCheckedChangeListener { chipGroup, selectedId ->
            val chip = chipGroup.findViewById<Chip>(selectedId)
            val hintStr = chip?.text.toString()
            Toast.makeText(this, hintStr, Toast.LENGTH_SHORT).show()
        }
    }
}