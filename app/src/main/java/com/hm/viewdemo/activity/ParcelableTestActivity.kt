package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hm.viewdemo.R
import com.hm.viewdemo.bean.Info

/**
 * 测试序列化
 * Serializable
 * Parcelable
 */
class ParcelableTestActivity : AppCompatActivity() {

    private val TAG = "ParcelableTestActivity"

    companion object {

        @JvmStatic
        val LIST_EXTRA = "listExtra"
        @JvmStatic
        val LIST_EXTRA_TWO = "listExtra_two"

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, ParcelableTestActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parcelable_test)

        val arrayList = intent.getParcelableArrayListExtra<Info>(LIST_EXTRA)

        arrayList.forEach {
            Log.d(TAG, "onCreate: $it")
        }
        val arrayListTwo = intent.getSerializableExtra(LIST_EXTRA_TWO) as ArrayList<Info>

        arrayListTwo.forEach {
            Log.d(TAG, "arrayListTwo onCreate: $it")
        }
    }
}
