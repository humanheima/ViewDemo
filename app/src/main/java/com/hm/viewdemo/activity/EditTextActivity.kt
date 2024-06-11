package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.iterator
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityEdittextBinding


/**
 * Created by p_dmweidu on 2024/6/11
 * Desc:
 */
class EditTextActivity : BaseActivity<ActivityEdittextBinding>() {



    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, EditTextActivity::class.java)
            context.startActivity(starter)
        }
    }


    override fun createViewBinding(): ActivityEdittextBinding {
        return ActivityEdittextBinding.inflate(layoutInflater)
    }

    override fun initData() {

        binding.et1.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.iterator()?.forEach {
                    Log.d(
                        TAG,
                        "onPrepareActionMode:title ${it.title}  groupId = ${it.groupId}  order = ${it.order}"
                    )
                }
                menu?.add(Menu.NONE, R.id.insert_a_new_line, 3, "Custom Action")
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                Toast.makeText(this@EditTextActivity, "Custom Action clicked", Toast.LENGTH_SHORT)
                    .show()
                mode?.finish()
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {

            }

        }

    }
}