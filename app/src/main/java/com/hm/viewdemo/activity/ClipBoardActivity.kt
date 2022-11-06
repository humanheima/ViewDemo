package com.hm.viewdemo.activity

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ClipBoardUtil
import com.xx.reader.utils.JsonUtilKt
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by p_dmweidu on 2022/9/5
 * Desc: 1. 测试粘贴板的使用
 *       2. 研究 Uri 和 URLEncode
 */
class ClipBoardActivity : AppCompatActivity() {


    companion object {

        private const val TAG = "ClipBoardActivity"

        @JvmStatic
        fun launch(context: Context) {
            val starter = Intent(context, ClipBoardActivity::class.java)
            context.startActivity(starter)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clip_board)

        testUrlDecode()
    }

    private fun testUrlDecode() {
        val s = "http://www.baidu.com?params1=a&params2=b&params3=+11"
        try {

            val encode = URLEncoder.encode(s, "UTF-8")
            Log.i(TAG, "testUrlDecode: encode = $encode")

            val uri = Uri.parse(URLDecoder.decode(encode, "UTF-8"))
            Log.i(TAG, "uri: $uri")

            Log.i(TAG, "uri params1 = : ${uri.getQueryParameter("params1")}")

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_put_text -> {

                val map: MutableMap<String, String> = hashMapOf()

                map["_report"] = "123456678abcde"
                map["_m"] = "hongxiu"

//                val rawText =
//                    "unitexxreader://nativepage/homepage?actionUrl={\"_m\":\"hongxiu\"," + "\"_report\":\"123456678abcde\"}&param2=1233"
//                Log.i(TAG, "onClick: rawText = $rawText")

                val json = JsonUtilKt.toJson(map)
                val text = "unitexxreader://nativepage/homepage?actionUrl=${json}&param2=1233"
                Log.i(TAG, "onClick: text = $text")


                val jsonEncodeOnce = URLEncoder.encode(json, "UTF-8")
                val textEncodeOnce =
                    "unitexxreader://nativepage/homepage?actionUrl=${jsonEncodeOnce}&param2=1233"
                Log.i(TAG, "onClick: textEncodeOnce = $textEncodeOnce")


                val jsonEncodeTwice = URLEncoder.encode(textEncodeOnce, "UTF-8")

                val textEncodeTwice =
                    "unitexxreader://nativepage/homepage?mUrl=$jsonEncodeTwice"

                Log.i(TAG, "onClick: textEncodeTwice = $textEncodeTwice")


                val textUrl =
                    "unitexxreader://nativepage/client/readepage?bid=2933760400662203&cid=1&ccid=7875308428230768&clearTop=2&murl=unitexxreader%3A%2F%2Fnativepage%2Fhomepage%3Fquery%3D%257B%2522_report%2522%253A%2522xx20220913003%2522%252C%2522_xxm%2522%253A%2522m%25242f82d741-2744-40f5-a280-7c64145dc1c1%2524%2524bookread%25242933760400662203%25247875308428230768%2524%25241666344899259%2524%2524%2522%257D"
                ClipBoardUtil.putToClipboard(this, "simple_label", textUrl)
            }

            R.id.btn_get_text -> {
                val clipData: ClipData? = ClipBoardUtil.getClipboard(this)
                clipData?.let {
                    if (it.itemCount > 0) {
                        val item: ClipData.Item = it.getItemAt(0)
                        val clipText = item.text.toString()
                        Log.i(TAG, "粘贴板内容: $clipText")

                        clipText.let { text ->

                            val uri = Uri.parse(text)
                            val list: List<String>? = text.split("?")

                            if (!list.isNullOrEmpty() && list.size > 1) {
                                val mUrl = list[1]

                                Log.i(TAG, "粘贴板内容: mUrl = $mUrl")
                                val decodeUrl = URLDecoder.decode(mUrl, "UTF-8")
                                Log.i(TAG, "粘贴板内容:  decode之后 mUrl = $decodeUrl")


                                Log.i(TAG, "onClick: ====================")

                                val queryParameter: String? = uri.getQueryParameter("query")
                                Log.i(TAG, "粘贴板内容: mUrl = $queryParameter")


                                val decodeUrl2 = URLDecoder.decode(queryParameter, "UTF-8")
                                Log.i(TAG, "粘贴板内容: decodeUrl2 = $decodeUrl2")

                                val uri2 = Uri.parse(decodeUrl2)

                                val jsonStr = uri2.getQueryParameter("actionUrl")

                                Log.i(TAG, "粘贴板内容: jsonStr = $jsonStr")


                                //val innerList = decodeUrl.split()

                            }

                        }

                    }
                }
            }
            R.id.btn_clear_clipboard -> {
                ClipBoardUtil.clear(this)
            }
            R.id.btn_test_Uri -> {
                val text = "abcdeffffff"
                val uri = Uri.parse(text)
                Log.i(TAG, "onClick: uri = $uri")

                Log.i(TAG, "onClick: uri = ${uri.getQueryParameter("mUrl")}")


            }
            R.id.btn_test_URLEncoder -> {
                val text = "http://www.baidu.com?params=abcdeffffff"
                val encoder = URLEncoder.encode(text, "UTF-8")
                Log.i(TAG, "onClick: encoder = $encoder")
            }
        }

    }
}