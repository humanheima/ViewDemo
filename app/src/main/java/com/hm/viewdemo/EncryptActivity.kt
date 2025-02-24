package com.hm.viewdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityEncryptBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by p_dmweidu on 2025/2/23
 * Desc: 测试各种加密算法的使用
 */
class EncryptActivity : BaseActivity<ActivityEncryptBinding>() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, EncryptActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding(): ActivityEncryptBinding {
        return ActivityEncryptBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }


    
}


object MD5Utils {
    fun getMD5(input: String): String? {
        try {
            // 创建MD5算法实例
            val md = MessageDigest.getInstance("MD5")


            // 将输入字符串转换为字节数组并计算哈希
            val array = md.digest(input.toByteArray())


            // 将字节数组转换为16进制字符串
            val sb = StringBuilder()
            for (b in array) {
                // 转换为16进制，确保每字节是2位
                val hex = Integer.toHexString(b.toInt() and 0xFF)
                if (hex.length == 1) {
                    sb.append('0')
                }
                sb.append(hex)
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }
    }
}