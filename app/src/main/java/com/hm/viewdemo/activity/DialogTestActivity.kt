package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.widget.Toast
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.dialog.CustomDialogFragment
import com.hm.viewdemo.widget.dialog.FireMissilesDialogFragment
import kotlinx.android.synthetic.main.activity_dialog_test.*

/**
 * Crete by dumingwei on 2019-10-22
 * Desc:
 * Dialog 类是对话框的基类，但您应避免直接实例化 Dialog，而是使用下列子类之一：
 * AlertDialog 此对话框可显示标题、最多三个按钮、可选择项列表或自定义布局。
 * DatePickerDialog 或 TimePickerDialog 此对话框带有允许用户选择日期或时间的预定义界面。
 *
 */
class DialogTestActivity : AppCompatActivity() {

    private var dialogFragment: FireMissilesDialogFragment? = null
    private var customDialogFragment: CustomDialogFragment? = null

    private val TAG = "DialogTestActivity"

    private var alertDialog: AlertDialog? = null

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, DialogTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_test)
        btnShowDialog.setOnClickListener {
            showDialog()
        }
        btnShowDialog1.setOnClickListener {
            showCustomDialog()
        }
        btnShowDialog2.setOnClickListener {
            showDialog2()
        }
    }

    private fun showDialog() {
        if (dialogFragment == null) {
            dialogFragment = FireMissilesDialogFragment()
        }
        dialogFragment?.show(supportFragmentManager, null)
    }

    private fun showCustomDialog() {
        if (alertDialog == null) {
            val builder = AlertDialog.Builder(this)
            builder.setView(layoutInflater.inflate(R.layout.dialog_signin, null))
                    .setPositiveButton("confirm") { dialog, which ->
                        Toast.makeText(this, "confirm", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("cancel") { dialog, which ->
                        Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
                    }

            alertDialog = builder.create()
            alertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }

        alertDialog?.show()
    }

    private fun showDialog2() {
        if (customDialogFragment == null) {
            customDialogFragment = CustomDialogFragment()
        }
        customDialogFragment?.show(supportFragmentManager, null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        customDialogFragment?.dismiss()
    }
}
