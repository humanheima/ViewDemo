package com.hm.viewdemo.custom_view.chapter8

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2019-08-29
 * Desc:
 *
 */

class PorterDuffXfermodeViewActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, PorterDuffXfermodeViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val modes = listOf(
            (PorterDuff.Mode.CLEAR),
            (PorterDuff.Mode.SRC),
            (PorterDuff.Mode.DST),
            (PorterDuff.Mode.SRC_OVER),
            (PorterDuff.Mode.DST_OVER),
            (PorterDuff.Mode.SRC_IN),
            (PorterDuff.Mode.DST_IN),
            (PorterDuff.Mode.SRC_OUT),
            (PorterDuff.Mode.DST_OUT),
            (PorterDuff.Mode.SRC_ATOP),
            (PorterDuff.Mode.DST_ATOP),
            (PorterDuff.Mode.XOR),
            (PorterDuff.Mode.DARKEN),
            (PorterDuff.Mode.LIGHTEN),
            (PorterDuff.Mode.MULTIPLY),
            (PorterDuff.Mode.SCREEN)
    )

    private val sLabels = listOf(
            "Clear",
            "Src",
            "Dst",
            "SrcOver",
            "DstOver",
            "SrcIn",
            "DstIn",
            "SrcOut",
            "DstOut",
            "SrcATop",
            "DstATop",
            "Xor",
            "Darken",
            "Lighten",
            "Multiply",
            "Screen")


    private var adapter: XfermodeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_porter_duff_xfermode_view)

        /* rvModes.layoutManager = LinearLayoutManager(this)
         rvModes.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

         adapter = XfermodeAdapter(this, modes)
         rvModes.adapter = adapter*/
    }


}
