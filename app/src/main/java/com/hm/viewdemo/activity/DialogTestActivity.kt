package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivityDialogTestBinding
import com.hm.viewdemo.util.ImageUtil
import com.hm.viewdemo.widget.dialog.CustomDialogFragment
import com.hm.viewdemo.widget.dialog.FireMissilesDialogFragment
import com.hm.viewdemo.widget.dialog.FullScreenDialog2
import com.hm.viewdemo.widget.dialog.FullScrreenDialog
import com.hm.viewdemo.widget.hongyang.RoundImageView
import kotlin.random.Random

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

    private lateinit var binding: ActivityDialogTestBinding

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, DialogTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDialogTestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnFullscreenDialog.setOnClickListener {
            //val dialog = FullScrreenDialog(this)
            val dialog = FullScreenDialog2(this)
            dialog.show()
        }
        binding.btnShowDialog.setOnClickListener {
            showDialog()
        }
        binding.btnShowDialog1.setOnClickListener {
            showCustomDialog()
        }
        binding.btnShowDialog2.setOnClickListener {
            showDialog2()
        }
        binding.btnShowDialog4.setOnClickListener {
            //showDialog2()
            if (Random.nextBoolean()) {
                arrayList.add("https://xxvirtualcharactercdn.rongshuxia.com/7F2B7CCCC90CFD154497A93A7CA147FC.jpg")
            } else {
                arrayList.add("https://xxvirtualcharactercdn.rongshuxia.com/2CD92938CCCDC972D4C3038217AD7FAF.jpg")
            }
        }
        binding.btnShowDialog3.setOnClickListener {
            showCustomDialog()
        }
        arrayList.add("https://xxvirtualcharactercdn.rongshuxia.com/2CD92938CCCDC972D4C3038217AD7FAF.jpg")
    }

    private fun showDialog() {
        if (dialogFragment == null) {
            dialogFragment = FireMissilesDialogFragment()
        }
        dialogFragment?.show(supportFragmentManager, null)
    }

    val arrayList = ArrayList<String>()


    private fun showCustomDialog() {
        //if (alertDialog == null) {
        val builder = AlertDialog.Builder(this)


        val size = arrayList.size

        val lineSize = if (size % 3 == 0) {
            size / 3
        } else {
            size / 3 + 1
        }

        val parentLinearLayout = LinearLayout(this)
        parentLinearLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        parentLinearLayout.orientation = LinearLayout.VERTICAL

        for (i in 0 until lineSize) {
            val oneLineLinearLayout = LinearLayout(this)
            oneLineLinearLayout.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = 10
            }
            oneLineLinearLayout.orientation = LinearLayout.HORIZONTAL

            val startItem = i * 3
            for (j in startItem until (startItem + 3)) {
                if (j >= size) {
                    break
                }
                val oneItemLinearLayout = LinearLayout(this)
                oneItemLinearLayout.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = 10
                    marginEnd = 10
                }

                oneItemLinearLayout.orientation = LinearLayout.VERTICAL

                val imageView = RoundImageView(this)
                imageView.setType(RoundImageView.TYPE_ROUND)
                //imageView.setBorderRadius(ScreenUtil.dpToPx(this, 16))
                val layoutParams = LinearLayout.LayoutParams(160, 160).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                layoutParams.bottomMargin = 10
                imageView.layoutParams = layoutParams
                //添加imageView
                oneItemLinearLayout.addView(imageView)
                ImageUtil.loadImage(this, arrayList[j], imageView)

                val textView: androidx.appcompat.widget.AppCompatTextView =
                    androidx.appcompat.widget.AppCompatTextView(this)
                textView.text = "测试"
                //添加textView
                oneItemLinearLayout.addView(
                    textView, LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply { gravity = Gravity.CENTER_HORIZONTAL }
                )

                oneLineLinearLayout.addView(oneItemLinearLayout)

            }
            parentLinearLayout.addView(oneLineLinearLayout)
        }

        builder.setView(parentLinearLayout)
            .setPositiveButton("confirm") { dialog, which ->
                Toast.makeText(this, "confirm", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("cancel") { dialog, which ->
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            }

        alertDialog = builder.create()
        alertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //}

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
